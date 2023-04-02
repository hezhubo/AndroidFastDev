package com.hezb.fastdev.demo.network

import com.google.gson.JsonElement
import com.hezb.framework.network.response.JsonResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Project Name: AndroidFastDev
 * File Name:    ApiServices
 *
 * Description: TODO.
 *
 * @author  hezhubo
 * @date    2023年03月26日 22:32
 */
interface ApiService {

    companion object {
        private const val API_VERSION = "v4.0"
    }

    /**
     * 获取直播列表数据
     *
     * @return
     */
    @GET("$API_VERSION/tvList.html")
    suspend fun getLiveList(@QueryMap params: Map<String, String>): JsonResponse<JsonElement>

}