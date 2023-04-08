package com.hezb.framework.utils

import android.content.Context
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * Project Name: AndroidFastDev
 * File Name:    FileUtil
 *
 * Description: 文件处理工具类.
 *
 * @author  hezhubo
 * @date    2023年04月08日 22:55
 */
object FileUtil {

    /**
     * 复制文件
     *
     * @param srcFile
     * @param destFile
     * @param delSrcFile 是否删除原文件（默认不删除）
     * @return
     */
    @JvmStatic
    @JvmOverloads
    fun copyFile(srcFile: File, destFile: File, delSrcFile: Boolean = false): Boolean {
        if (!srcFile.exists()) {
            return false
        }
        try {
            srcFile.copyTo(destFile, true)
            if (delSrcFile) {
                srcFile.delete()
            }
            return true
        } catch (e : Exception) {
            LogUtil.e("copy file error!", e)
        }
        return false
    }

    /**
     * 获取目录大小
     * 递归遍历子目录文件
     *
     * @param file
     * @return
     */
    @JvmStatic
    fun getFolderSize(file: File): Long {
        return if (file.isDirectory) {
            val fileList = file.listFiles() ?: return 0
            var size: Long = 0
            for (childFile in fileList) {
                // 如果下面还有文件
                size = if (childFile.isDirectory) {
                    size + getFolderSize(childFile)
                } else {
                    size + childFile.length()
                }
            }
            size
        } else {
            file.length()
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return
     */
    @JvmStatic
    fun exist(path: String?): Boolean {
        if (path.isNullOrEmpty()) {
            return false
        }
        return File(path).exists()
    }

    /**
     * 写文件
     *
     * @param destFile 目标存储文件
     * @param content 写入内容
     * @param content 是否为追加写入
     * @return
     */
    @JvmStatic
    @JvmOverloads
    fun writeToFile(destFile: File, content: String, append: Boolean = false): Boolean {
        try {
            if (!destFile.exists()) {
                destFile.parentFile?.let {
                    if (!it.exists()) {
                        it.mkdirs()
                    }
                }
                destFile.createNewFile()
            } else {
                if (!append) { // 非追加写入，删除旧文件，创建新文件
                    destFile.delete()
                    destFile.createNewFile()
                }
            }
            return FileOutputStream(destFile, append).use {
                it.write(content.toByteArray())
                true
            }
        } catch (e: Exception) {
            LogUtil.e("write to file error!", e)
        }
        return false
    }

    /**
     * 通过uri读取文件
     *
     * @param context 上下文
     * @param uri     文件uri
     * @return
     */
    @JvmStatic
    fun readByteArrayFromUri(context: Context, uri: Uri): ByteArray? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use { ins ->
                ByteArrayOutputStream().use { baos ->
                    val flush = ByteArray(2048)
                    var len: Int
                    while (ins.read(flush).also { len = it } != -1) {
                        baos.write(flush, 0, len)
                    }
                    baos.toByteArray()
                }
            }
        } catch (e: Exception) {
            LogUtil.e("read byteArray from uri error! uri = $uri", e)
            null
        }
    }

    /**
     * 解zip文件到指定目录
     *
     * @param zipFile 压缩包路径
     * @param dstDirectory 目标目录文件
     * @throws IOException
     * @return
     */
    @JvmStatic
    @Throws(IOException::class)
    fun unzipFile(zipFile: File, dstDirectory: File) : Boolean {
        val zip = ZipFile(zipFile)
        val dstPath = dstDirectory.absolutePath
        val entries: Enumeration<*> = zip.entries()
        var zipEntry: ZipEntry
        var fileName: String
        var file: File
        while (entries.hasMoreElements()) {
            zipEntry = entries.nextElement() as ZipEntry
            fileName = "$dstPath${File.separator}${zipEntry.name}"
            if (zipEntry.isDirectory) { // 如果是文件夹
                file = File(fileName)
                file.mkdirs()
                continue
            }
            // 判断路径是否存在,不存在则创建文件路径
            file = File(fileName.substring(0, fileName.lastIndexOf('/')))
            if (!file.exists()) {
                file.mkdirs()
            }
            file = File(fileName)
            if (file.exists()) {
                // 文件存在，删除掉
                file.delete()
            }
            file.createNewFile()

            zip.getInputStream(zipEntry)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        return true
    }

}