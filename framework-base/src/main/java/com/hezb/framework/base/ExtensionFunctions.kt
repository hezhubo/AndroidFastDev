package com.hezb.framework.base

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

/**
 * Project Name: AndroidFastDev
 * File Name:    ExtensionFunctions
 *
 * Description: 扩展函数.
 *
 * @author  hezhubo
 * @date    2023年03月24日 10:28
 */

/**
 * 页面是否已销毁
 */
fun Activity.isDead(): Boolean {
    return isFinishing || isDestroyed
}

/**
 * 上下文所在页面是否已销毁
 */
fun Context.isDead(): Boolean {
    return when (this) {
        is Activity -> {
            return isDead()
        }
        is ContextWrapper -> {
            baseContext?.isDead() ?: false
        }
        else -> {
            false
        }
    }
}

/**
 * fragment findViewById
 */
fun <T : View> Fragment.findViewById(@IdRes id: Int): T? {
    return view?.findViewById(id)
}