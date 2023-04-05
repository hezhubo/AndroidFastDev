package com.hezb.framework.network.download

import com.hezb.framework.utils.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Project Name: AndroidFastDev
 * File Name:    FileResponseExt
 *
 * Description: 文件写入扩展函数.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:20
 */

/**
 * ResponseBody扩展函数
 * 写入本地文件
 *
 * @param filePath       文件保存路径
 * @param downloadedSize 已下载的文件大小
 * @param totalSize      总文件大小
 * @param onProgress     更新下载进度
 * @return 是否写入成功
 */
suspend fun ResponseBody.writeToFile(
    filePath: String,
    downloadedSize: Long,
    totalSize: Long,
    onProgress: (downloaded: Long, total: Long) -> Unit
): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val downloadFile = File(filePath)

            try {
                // 处理下载文件父目录不存在导致的无法下载问题
                downloadFile.parentFile?.let {
                    if (!it.exists()) {
                        it.mkdirs()
                    }
                }
            } catch (e: Exception) {
                LogUtil.e("parent file mkdirs error!", e)
            }

            val fileReader = ByteArray(4096)
            val inputStream = byteStream()

            val outputStream: FileOutputStream
            val fileSize = if (totalSize == 0L) {
                outputStream = FileOutputStream(downloadFile) // 复写
                contentLength()
            } else {
                outputStream = FileOutputStream(downloadFile, true) // 追加
                totalSize
            }

            inputStream.use { inputS ->
                outputStream.use { outputS ->
                    var downloaded = downloadedSize
                    while (true) {
                        val read = inputS.read(fileReader)
                        if (read == -1) {
                            break
                        }
                        outputS.write(fileReader, 0, read)

                        downloaded += read.toLong()

                        onProgress(downloaded, fileSize)
                    }
                }
            }

            downloadFile.length() == fileSize
        } catch (e: IOException) {
            LogUtil.e( "write error!", e)
            false
        }
    }
}