package com.hezb.framework.network.download

/**
 * Project Name: AndroidFastDev
 * File Name:    DownloadListener
 *
 * Description: 下载监听器.
 *
 * @author  hezhubo
 * @date    2023年03月26日 03:03
 */
interface DownloadListener {

    /**
     * 启动
     */
    fun onStart()

    /**
     * 暂停
     */
    fun onPause()

    /**
     * 更新进度
     *
     * @param percent 百分比
     */
    fun onProgress(percent: Int)

    /**
     * 完成
     */
    fun onCompleted()

    /**
     * 出错
     */
    fun onError()

}