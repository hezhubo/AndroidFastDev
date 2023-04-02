package com.hezb.framework.network.download

import com.hezb.framework.repository.IRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Project Name: AndroidFastDev
 * File Name:    FileResponseRepository
 *
 * Description: 文件下载数据源.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:20
 */
class FileResponseRepository(
    private val service: DownloadService,
    private val downloadInfo: DownloadInfo
) : IRepository {

    private var downloadJob: Job? = null

    var downloadListener: DownloadListener? = null

    fun startDownload() {
        downloadJob?.let {
            if (it.isActive) {
                return
            }
        }
        downloadListener?.onStart()
        downloadJob = CoroutineScope(Dispatchers.IO).launch {
            val range = if (downloadInfo.downloadedSize > 0 && downloadInfo.totalSize > 0) {
                "bytes=${downloadInfo.downloadedSize}-${downloadInfo.totalSize}"
            } else {
                null
            }
            try {
                var currentPercent = if (downloadInfo.totalSize != 0L) {
                    (downloadInfo.downloadedSize * 100 / downloadInfo.totalSize).toInt()
                } else {
                    0
                }
                val writeResult = service.download(downloadInfo.url, range).writeToFile(
                    downloadInfo.filePath,
                    downloadInfo.downloadedSize,
                    downloadInfo.totalSize,
                    onProgress = { downloaded, total ->
                        downloadInfo.downloadedSize = downloaded
                        downloadInfo.totalSize = total
                        val percent = (downloaded * 100 / total).toInt()
                        if (currentPercent != percent) {
                            currentPercent = percent
                            launch(Dispatchers.Main) {
                                downloadListener?.onProgress(currentPercent)
                            }
                        }
                    }
                )
                launch(Dispatchers.Main) {
                    if (writeResult) {
                        downloadListener?.onCompleted()
                    } else {
                        downloadListener?.onError()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    downloadListener?.onError()
                }
            }
        }
    }

    fun pauseDownload() {
        downloadJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        downloadListener?.onPause()
    }

}