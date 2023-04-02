package com.hezb.framework.network

import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.hezb.framework.base.AppManager
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.ParseException

/**
 * Project Name: AndroidFastDev
 * File Name:    RequestException
 *
 * Description: 网络请求的相关异常.
 *
 * @author  hezhubo
 * @date    2023年03月24日 18:55
 */
class RequestException {

    companion object {
        const val CODE_UNKNOWN_ERROR = -999
        const val CODE_DATA_PARSE_ERROR = -1000
        const val CODE_CONNECT_ERROR = -1001
        const val CODE_TIMEOUT_ERROR = -1003
    }

    /**
     * 异常代码
     */
    var code: Int = 0

    /**
     * 异常信息
     */
    var message: String

    constructor(e: Throwable?) {
        if (e == null) {
            this.code = CODE_UNKNOWN_ERROR
            this.message = AppManager.INSTANCE.getApplication()
                .getString(R.string.framework_network_unknown_error)
        } else {
            when (e) {
                is HttpException -> { // HTTP错误
                    this.code = e.code()
                    this.message = AppManager.INSTANCE.getApplication()
                        .getString(R.string.framework_network_http_error)
                }
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> { // 解析数据错误
                    this.code = CODE_DATA_PARSE_ERROR
                    this.message = AppManager.INSTANCE.getApplication()
                        .getString(R.string.framework_network_data_parse_error)
                }
                is ConnectException -> { // 连接网络错误
                    this.code = CODE_CONNECT_ERROR
                    this.message = AppManager.INSTANCE.getApplication()
                        .getString(R.string.framework_network_connect_error)
                }
                is SocketTimeoutException -> { // 网络超时
                    this.code = CODE_TIMEOUT_ERROR
                    this.message = AppManager.INSTANCE.getApplication()
                        .getString(R.string.framework_network_timeout_error)
                }
                else -> {
                    this.code = CODE_UNKNOWN_ERROR
                    this.message = AppManager.INSTANCE.getApplication()
                        .getString(R.string.framework_network_unknown_error)
                }
            }
        }
    }

    /**
     * 自定义异常
     */
    constructor(code: Int, message: String) {
        this.code = code
        this.message = message
    }

}