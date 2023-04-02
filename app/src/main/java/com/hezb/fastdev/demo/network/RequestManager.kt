package com.hezb.fastdev.demo.network

import com.hezb.framework.network.RetrofitRequestClient

/**
 * Project Name: AndroidFastDev
 * File Name:    RequestManager
 *
 * Description: 请求管理者.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:20
 */
class RequestManager private constructor() {

    companion object {
        val INSTANCE: RequestManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RequestManager()
        }
    }

    private val apiServiceMap: MutableMap<String, ApiService> = HashMap()
    private val baseUrl = "https://mapi.4399youpai.com/app/android/" // 暂时借用游拍线上接口
    private val retrofitRequestClient = RetrofitRequestClient()

    fun getApiService(url: String = baseUrl): ApiService {
        val api = apiServiceMap[url]
        return if (api != null) {
            api
        } else {
            val retrofit = retrofitRequestClient.getRetrofit(url)
            val apiService = retrofitRequestClient.create<ApiService>(retrofit)
            apiServiceMap[url] = apiService
            apiService
        }
    }

    fun getHeaders(): MutableMap<String, String> {
        return retrofitRequestClient.getHeaderInterceptor().getHeaders()
    }

    fun setUserAgent(userAgent: String) {
        retrofitRequestClient.getHeaderInterceptor().setUserAgent(userAgent)
    }

    fun getUserAgent(): String? {
        return retrofitRequestClient.getHeaderInterceptor().getUserAgent()
    }


    fun addHeader(name: String, value: String) {
        retrofitRequestClient.getHeaderInterceptor().addHeader(name, value)
    }

    fun removeHeader(name: String) {
        retrofitRequestClient.getHeaderInterceptor().removeHeader(name)
    }

    fun cleanHeader() {
        retrofitRequestClient.getHeaderInterceptor().cleanHeader()
    }

}