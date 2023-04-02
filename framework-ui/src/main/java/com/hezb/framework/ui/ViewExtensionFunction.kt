package com.hezb.framework.ui

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Project Name: AndroidFastDev
 * File Name:    ViewExtensionFunction
 *
 * Description: View相关扩展函数.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:30
 */

/**
 * 单击，处理连续点击问题
 *
 * @param minInterval   两次单击的最小间隔时间 默认1秒
 * @param onSingleClick
 */
fun View.setOnSingleClickListener(minInterval: Long = 1000, onSingleClick: (v: View) -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        /**
         * 上次单击时间
         */
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            val now = System.currentTimeMillis()
            if (now - lastClickTime >= minInterval) {
                onSingleClick(v)
            }
            lastClickTime = now
        }
    })
}

/**
 * ViewHolder findViewById
 *
 * @param id
 */
fun <T : View> RecyclerView.ViewHolder.findViewById(@IdRes id: Int): T? {
    return itemView.findViewById(id)
}