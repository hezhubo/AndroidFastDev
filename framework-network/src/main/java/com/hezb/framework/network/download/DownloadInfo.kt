package com.hezb.framework.network.download

/**
 * Project Name: AndroidFastDev
 * File Name:    DownloadInfo
 *
 * Description: 下载信息.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:20
 */
data class DownloadInfo(
    val url: String,
    val filePath: String,
    var downloadedSize: Long = 0,
    var totalSize: Long = 0
)
