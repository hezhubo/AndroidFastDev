package com.hezb.framework.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Project Name: AndroidFastDev
 * File Name:    RetrofitRequestClient
 *
 * Description: Retrofit请求客户端.
 *
 * @author  hezhubo
 * @date    2023年03月24日 18:55
 */
class RetrofitRequestClient {

    private val retrofitMap: MutableMap<String, Retrofit> = HashMap()
    private val headerInterceptor = HeaderInterceptor()
    private val okHttpRequestClient = OkHttpRequestClient(
        OkHttpRequestClient.getDefaultBuilder().addInterceptor(headerInterceptor)
    )

    /**
     * @return 请求头拦截器
     */
    fun getHeaderInterceptor(): HeaderInterceptor {
        return headerInterceptor
    }

    /**
     * 创建Retrofit
     *
     * @param baseUrl 接口前缀
     * @param client  Okhttp请求客户端
     * @return
     */
    fun createRetrofit(
        baseUrl: String,
        client: OkHttpRequestClient = okHttpRequestClient
    ): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl) // 服务器地址
            .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
            .client(client.getClient())
        return builder.build()
    }

    /**
     * 获取默认配置的缓存中的Retrofit
     *
     * @param baseUrl 接口前缀
     * @return
     */
    fun getRetrofit(baseUrl: String): Retrofit {
        val retrofit = retrofitMap[baseUrl]
        if (retrofit != null) {
            return retrofit
        }
        return createRetrofit(baseUrl).also {
            retrofitMap[baseUrl] = it
        }
    }

    /**
     * 创建API请求客户端
     */
    inline fun <reified T> create(retrofit: Retrofit): T {
        return retrofit.create(T::class.java)
    }

}