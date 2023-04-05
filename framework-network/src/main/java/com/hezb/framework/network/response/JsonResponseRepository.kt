package com.hezb.framework.network.response

import com.google.gson.*
import com.hezb.framework.base.AppManager
import com.hezb.framework.network.R
import com.hezb.framework.network.RequestException
import com.hezb.framework.repository.IRepository
import com.hezb.framework.utils.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Project Name: AndroidFastDev
 * File Name:    JsonResponseRepository
 *
 * Description: 封装通用解析逻辑的网络请求Json响应数据源.
 *
 * @author  hezhubo
 * @date    2023年03月24日 18:55
 */
abstract class JsonResponseRepository<T> : IRepository {

    /**
     * Gson实例，方便子类做数据解析
     */
    protected val gson by lazy { Gson() }

    /**
     * 服务端返回的数据
     */
    var response: JsonResponse<T>? = null
        private set

    /**
     * 是否为本地缓存数据
     */
    var isLocalData = false
        private set

    /**
     * 统一处理数据解析
     *
     * @param response
     * @param onSuccess
     * @param onFailure
     */
    private suspend fun parseData(
        response: JsonResponse<T>,
        onSuccess: () -> Unit,
        onFailure: (e: RequestException) -> Unit
    ) {
        LogUtil.d("json response : \n${gson.toJson(response)}")
        if (checkResponseToParse(response)) {
            val exception: RequestException? = if (response.result is JsonElement) {
                withContext(Dispatchers.Default) {
                    try {
                        val jsonElement = response.result as? JsonElement
                        jsonElement?.let {
                            when {
                                it.isJsonObject -> parseResponseData(it.asJsonObject)
                                it.isJsonArray -> parseResponseData(it.asJsonArray)
                                it.isJsonPrimitive -> parseResponseData(it.asJsonPrimitive)
                                it.isJsonNull -> {}
                            }
                        }
                        return@withContext null
                    } catch (e: Exception) {
                        LogUtil.e("json response parse data error!", e)
                        return@withContext getCustomException(
                            null,
                            RequestException.CODE_DATA_PARSE_ERROR,
                            AppManager.INSTANCE.getApplication()
                                .getString(R.string.framework_network_data_parse_error)
                        )
                    }
                }
            } else {
                null
            }

            if (exception == null) {
                onSuccess()
            } else {
                onFailure(exception)
            }

        } else {
            val responseMessage = response.message
            if (responseMessage.isNullOrEmpty()) { // 服务端返回空message时
                onFailure(RequestException(response.code, getEmptyMessage()))
            } else {
                onFailure(RequestException(response.code, responseMessage))
            }
        }
    }

    /**
     * 执行数据请求
     *
     * @param requestResponse 请求数据
     * @param onStart         开始请求
     * @param onSuccess       请求成功
     * @param onFailure       请求失败
     */
    suspend fun startRequest(
        requestResponse: suspend () -> JsonResponse<T>?,
        onStart: () -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailure: (e: RequestException) -> Unit = {}
    ) {
        isLocalData = false
        onStart()
        try {
            response = requestResponse()
            response?.let {
                parseData(it, onSuccess, onFailure)
                if (hasLocalData()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        // 新建协程写入缓存
                        saveResponseIO(it)
                    }
                }
            } ?: throw NullPointerException("response null!")

        } catch (e: Exception) {
            LogUtil.e("request error!", e)
            if (hasLocalData()) { // 加载本地缓存数据
                try {
                    val localResponse = withContext(Dispatchers.IO) {
                        getLocalResponseIO()
                    } ?: throw e
                    isLocalData = true
                    parseData(localResponse, onSuccess, onFailure)
                } catch (e: Exception) {
                    onFailure(getCustomException(e))
                }
            } else {
                onFailure(getCustomException(e))
            }
        }
    }

    /**
     * 重写此方法确定是否需要进入默认解析逻辑
     *
     * @param response
     * @return
     */
    open fun checkResponseToParse(response: JsonResponse<T>): Boolean {
        return response.code == 100
    }

    /**
     * 重写此方法设置是否有缓存
     *
     * @return
     */
    protected open fun hasLocalData(): Boolean {
        return false
    }

    /**
     * 子类实现数据缓存
     * 此方法在IO线程执行
     *
     * @param response
     */
    protected open fun saveResponseIO(response: JsonResponse<T>) {}

    /**
     * 子类实现缓存读取
     * 此方法在IO线程执行
     *
     * @return
     */
    protected open fun getLocalResponseIO(): JsonResponse<T>? {
        return null
    }

    /**
     * 覆盖此方法，自定义异常消息提示
     */
    protected open fun getCustomException(
        e: Throwable? = null,
        code: Int = 0,
        message: String? = null
    ): RequestException {
        return if (message.isNullOrEmpty()) {
            RequestException(e)
        } else {
            RequestException(code, message)
        }
    }

    /**
     * 覆盖此方法，自定义服务端返回空message时的提示
     */
    protected open fun getEmptyMessage(): String {
        return ""
    }

    /**
     * 数据解析
     *
     * @param result json对象
     * @throws JsonParseException
     */
    @Throws(JsonParseException::class)
    protected open fun parseResponseData(result: JsonObject) {}

    /**
     * 数据解析
     *
     * @param result json数组
     * @throws JsonParseException
     */
    @Throws(JsonParseException::class)
    protected open fun parseResponseData(result: JsonArray) {}

    /**
     * 数据解析
     *
     * @param result json基础数据类型
     * @throws JsonParseException
     */
    @Throws(JsonParseException::class)
    protected open fun parseResponseData(result: JsonPrimitive) {}

}