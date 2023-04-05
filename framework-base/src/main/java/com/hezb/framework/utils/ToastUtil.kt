package com.hezb.framework.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import java.lang.ref.WeakReference

/**
 * Project Name: AndroidFastDev
 * File Name:    ToastUtil
 *
 * Description: Toast工具类，用于防止连续弹出Toast时间过长.
 *
 * @author  hezhubo
 * @date    2023年03月27日 20:59
 */
object ToastUtil {

    private var mToastWeakReference: WeakReference<Toast>? = null

    @JvmStatic
    @JvmOverloads
    fun show(
        context: Context?,
        stringId: Int,
        duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.NO_GRAVITY,
        x: Int = 0,
        y: Int = 0
    ) {
        context?.let {
            show(context, it.getString(stringId), duration, gravity, x, y)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun show(
        context: Context?,
        msg: String?,
        duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.NO_GRAVITY,
        x: Int = 0,
        y: Int = 0
    ) {
        if (msg.isNullOrEmpty()) {
            return // 空消息不执行Toast
        }
        context?.let {
            cancel()
            val toast = Toast.makeText(it.applicationContext, msg, duration)
            if (gravity != Gravity.NO_GRAVITY) {
                toast.setGravity(gravity, x, y)
            }
            toast.show()
            mToastWeakReference = WeakReference(toast)
        }
    }

    @JvmStatic
    fun cancel() {
        mToastWeakReference?.let {
            it.get()?.cancel()
            it.clear()
        }
        mToastWeakReference = null
    }

}