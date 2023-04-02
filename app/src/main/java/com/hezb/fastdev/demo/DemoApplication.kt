package com.hezb.fastdev.demo

import com.hezb.fastdev.demo.network.RequestManager
import com.hezb.framework.base.BaseApplication

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

        RequestManager.INSTANCE.addHeader("test", "123")
    }

}