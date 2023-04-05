package com.hezb.framework.utils

import android.util.Log

/**
 * Project Name: AndroidFastDev
 * File Name:    LogUtil
 *
 * Description: 日志工具（封装统一开关）.
 *
 * @author  hezhubo
 * @date    2023年04月05日 19:47
 */
object LogUtil {

    private const val TAG = "LogUtil"

    @Volatile
    var writeLogs = false // 默认不打印日志

    private fun log(priority: Int, tag: String, msg: Any?, tr: Throwable?) {
        if (writeLogs) {
            if (tr == null) {
                Log.println(priority, tag, msg.toString())
            } else {
                Log.println(priority, tag, "$msg\n${Log.getStackTraceString(tr)}")
            }
        }
    }

    @JvmStatic
    @JvmOverloads
    fun v(msg: String?, tr: Throwable? = null) {
        v(TAG, msg, tr)
    }

    @JvmStatic
    @JvmOverloads
    fun v(tag: String, msg: String?, tr: Throwable? = null) {
        log(Log.VERBOSE, tag, msg, tr)
    }

    @JvmStatic
    @JvmOverloads
    fun d(msg: String?, tr: Throwable? = null) {
        d(TAG, msg, tr)
    }

    @JvmStatic
    @JvmOverloads
    fun d(tag: String, msg: String?, tr: Throwable? = null) {
        log(Log.DEBUG, tag, msg, tr)
    }

    @JvmStatic
    @JvmOverloads
    fun i(msg: String?, tr: Throwable? = null) {
        i(TAG, msg, tr)
    }

    @JvmStatic
    @JvmOverloads
    fun i(tag: String, msg: String?, tr: Throwable? = null) {
        log(Log.INFO, tag, msg, tr)
    }

    @JvmStatic
    @JvmOverloads
    fun w(msg: String?, tr: Throwable? = null) {
        w(TAG, msg, tr)
    }

    @JvmStatic
    @JvmOverloads
    fun w(tag: String, msg: String?, tr: Throwable? = null) {
        log(Log.WARN, tag, msg, tr)
    }

    @JvmStatic
    @JvmOverloads
    fun e(msg: String?, tr: Throwable? = null) {
        e(TAG, msg, tr)
    }

    @JvmStatic
    @JvmOverloads
    fun e(tag: String, msg: String?, tr: Throwable? = null) {
        log(Log.ERROR, tag, msg, tr)
    }

    /**
     * 打印当前调用栈信息
     *
     * @param tag
     * @param priority
     */
    @JvmStatic
    @JvmOverloads
    fun printStackTrace(tag: String = TAG, priority: Int = Log.DEBUG) {
        Thread.currentThread().stackTrace.forEach {
            log(
                priority,
                tag,
                "\t ${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})",
                null
            )
        }
    }

}