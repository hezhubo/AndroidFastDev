package com.hezb.framework.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Project Name: AndroidFastDev
 * File Name:    OkHttpRequestClient
 *
 * Description: Okhttp请求客户端.
 * 内部维护一个默认配置的okHttpClient（默认携带请求所需的头信息）
 *
 * @author  hezhubo
 * @date    2023年03月24日 18:43
 */
open class OkHttpRequestClient(builder: OkHttpClient.Builder) : RequestClient<OkHttpClient>() {

    companion object {
        const val DEFAULT_CONNECT_TIMEOUT = 10_000L
        const val DEFAULT_READ_TIMEOUT = 10_000L
        const val DEFAULT_WRITE_TIMEOUT = 10_000L

        fun getDefaultBuilder(): OkHttpClient.Builder {
            return OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
        }
    }

    private val okHttpClient: OkHttpClient = builder.build()

    override fun getClient(): OkHttpClient {
        return okHttpClient
    }

}