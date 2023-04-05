package com.hezb.fastdev.demo

import android.os.Build
import com.hezb.fastdev.demo.network.RequestManager
import com.hezb.framework.base.BaseApplication
import com.hezb.framework.network.utlis.NetworkMonitorHelper
import com.hezb.framework.utils.LogUtil

/**
 * Project Name: AndroidFastDev
 * File Name:    DemoApplication
 *
 * Description: Application.
 *
 * @author  hezhubo
 * @date    2023年03月26日 22:17
 */
class DemoApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        LogUtil.writeLogs = BuildConfig.DEBUG
        RequestManager.INSTANCE.addHeader("test", "123")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkMonitorHelper.registerNetworkCallback(this)
        } else {
            NetworkMonitorHelper.registerBroadcastReceiver(this)
        }
    }

}