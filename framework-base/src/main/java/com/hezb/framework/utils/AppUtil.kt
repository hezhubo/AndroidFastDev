package com.hezb.framework.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import java.io.BufferedReader
import java.io.FileReader

/**
 * Project Name: AndroidFastDev
 * File Name:    AppUtil
 *
 * Description: 应用工具类.
 *
 * @author  hezhubo
 * @date    2023年04月07日 12:56
 */
object AppUtil {

    /**
     * 获取应用的包信息
     *
     * @param context
     * @param packageName 应用包名
     * @return PackageInfo
     */
    @JvmStatic
    @JvmOverloads
    fun getPackageInfo(context: Context, packageName: String = context.packageName): PackageInfo? {
        try {
            return context.packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            LogUtil.e("package info error!", e)
        }
        return null
    }

    /**
     * 获取应用的版本号
     *
     * @param context
     * @return versionCode
     */
    @JvmStatic
    fun getVersionCode(context: Context): Int {
        return getPackageInfo(context)?.versionCode ?: 0
    }

    /**
     * 获取应用的版本名
     *
     * @param context
     * @return versionName
     */
    @JvmStatic
    fun getVersionName(context: Context): String? {
        return getPackageInfo(context)?.versionName
    }

    /**
     * 获取软件版本信息
     *
     * @param context
     * @return versionName.versionCode
     */
    @JvmStatic
    fun getFullVersionName(context: Context): String? {
        return getPackageInfo(context)?.let {
            "${it.versionName}.${it.versionCode}"
        }
    }

    /**
     * 获取应用名称
     *
     * @param context
     * @return APP name
     */
    @JvmStatic
    fun getAppName(context: Context): String? {
        return getPackageInfo(context)?.applicationInfo?.labelRes?.let { labelRes ->
            if (labelRes != 0) {
                context.getString(labelRes)
            } else {
                null
            }
        }
    }

    /**
     * 获取应用图标资源id
     *
     * @param context
     * @return APP icon res id
     */
    @JvmStatic
    fun getAppIcon(context: Context): Int {
        return getPackageInfo(context)?.applicationInfo?.icon ?: 0
    }

    /**
     * 判断某个应用是否已经安装
     *
     * @param context
     * @param packageName 应用包名
     * @return
     */
    @JvmStatic
    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return getPackageInfo(context, packageName) != null
    }

    /**
     * 判断某个Intent是否可用
     *
     * @param context
     * @param intent
     * @return
     */
    @JvmStatic
    fun isIntentAvailable(context: Context, intent: Intent): Boolean {
        return context.packageManager.queryIntentActivities(intent, 0).size > 0
    }

    /**
     * 获取进程号对应的进程名（linux）
     *
     * @param pid 进程号
     * @return 进程名
     */
    @JvmStatic
    fun getProcessName(pid: Int): String? {
        var processName: String? = null
        try {
            val br = BufferedReader(FileReader("/proc/$pid/cmdline"))
            br.use {
                val lineString = br.readLine()
                if (!lineString.isNullOrEmpty()) {
                    processName = lineString.trim()
                }
            }
        } catch (e: Exception) {
            LogUtil.e("process name error!", e)
        }
        return processName
    }

    /**
     * 获取应用当前进程名
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getProcessName(context: Context): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName()
        }
        val pid = android.os.Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        activityManager?.runningAppProcesses?.forEach {
            if (it.pid == pid) {
                return it.processName
            }
        }
        return getProcessName(pid)
    }

    /**
     * 通过上下文获取Activity
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getContextActivity(context: Context): Activity? {
        return if (context is Activity) {
            context
        } else if (context is ContextWrapper && context.baseContext is Activity) {
            context.baseContext as Activity
        } else {
            null
        }
    }

    /**
     * 判断某服务是否正在运行
     * 8.0以下返回系统运行的所有服务，8.0及以上仅返回自身的服务
     *
     * @param context
     * @param serviceName
     * @return
     */
    @JvmStatic
    fun isServiceRunning(context: Context, serviceName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val serviceInfoList = activityManager?.getRunningServices(1000)
        if (serviceInfoList.isNullOrEmpty()) {
            return false
        }
        for (i in serviceInfoList.indices) {
            if (serviceName == serviceInfoList[i].service.className) {
                return true
            }
        }
        return false
    }

}