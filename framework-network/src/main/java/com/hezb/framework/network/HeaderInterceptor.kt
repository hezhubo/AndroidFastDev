package com.hezb.framework.network

import com.hezb.framework.utils.LogUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Project Name: AndroidFastDev
 * File Name:    HeaderInterceptor
 *
 * Description: 统一添加请求头拦截器.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:00
 */
class HeaderInterceptor : Interceptor {

    companion object {
        private const val UA = "User-Agent"
    }

    private val headers: MutableMap<String, String> = HashMap()

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if (headers.isNotEmpty()) {
            for ((key, value) in headers) {
                builder.addHeader(key, value)
            }
        }
        LogUtil.d(chain.request().url().toString())
        return chain.proceed(builder.build())
    }

    /**
     * @return 获取请求头
     */
    fun getHeaders(): MutableMap<String, String> {
        return headers
    }

    /**
     * 设置UA
     *
     * @param userAgent
     */
    fun setUserAgent(userAgent: String) {
        addHeader(UA, userAgent)
    }

    /**
     * @return 获取当前设置的UA
     */
    fun getUserAgent(): String? {
        return headers[UA]
    }

    /**
     * 添加/更新请求头
     *
     * @param name
     * @param value
     */
    fun addHeader(name: String, value: String) {
        headers[name] = value
    }

    /**
     * 移除指定的请求头
     *
     * @param name
     */
    fun removeHeader(name: String) {
        headers.remove(name)
    }

    /**
     * 移除请求头
     */
    fun cleanHeader() {
        headers.clear()
    }

}