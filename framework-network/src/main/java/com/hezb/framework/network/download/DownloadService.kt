package com.hezb.framework.network.download

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Project Name: AndroidFastDev
 * File Name:    DownloadService
 *
 * Description: 通用下载接口.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:20
 */
interface DownloadService {

    /**
     * 下载文件
     *
     * @param url   下载地址（全路径）
     * @param range 断点位置
     * @return
     */
    @Streaming
    @GET
    suspend fun download(@Url url: String, @Header("Range") range: String? = null): ResponseBody

}