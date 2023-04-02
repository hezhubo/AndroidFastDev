package com.hezb.framework.base

import android.app.Application

/**
 * Project Name: AndroidFastDev
 * File Name:    BaseApplication
 *
 * Description: Application基类.
 *
 * @author  hezhubo
 * @date    2023年03月24日 09:55
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 可以不继承此基类，但需要创建AppManager
        AppManager.INSTANCE.onCreate(this)
    }

}