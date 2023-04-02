package com.hezb.framework.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import kotlin.properties.Delegates

/**
 * Project Name: AndroidFastDev
 * File Name:    AppManager
 *
 * Description: App管理者，持有Application.
 *
 * @author  hezhubo
 * @date    2023年03月24日 09:55
 */
class AppManager private constructor() {

    companion object {
        val INSTANCE: AppManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppManager()
        }
    }

    private lateinit var app: Application
    private var currentActivity: Activity? = null
    private var runningActivityCount: Int = 0

    fun onCreate(application: Application) {
        app = application
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
                runningActivityCount++
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
                runningActivityCount--
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                if (currentActivity === activity) { // 比较地址
                    currentActivity = null
                }
            }
        })
    }

    /**
     * @return Application
     */
    fun getApplication(): Application {
        return app
    }

    /**
     * @return 应用是否在前台
     */
    fun isForeground(): Boolean {
        return runningActivityCount > 0
    }

    /**
     * @return 当前应用最上层页面
     */
    fun getCurrentActivity(): Activity? {
        if (currentActivity?.isDead() == false) {
            return currentActivity
        }
        return null
    }

}