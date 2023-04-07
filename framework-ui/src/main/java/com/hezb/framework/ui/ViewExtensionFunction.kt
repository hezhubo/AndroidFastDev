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

/*
 * Context Receivers
 * 1.6.20新增实验功能，开启需要配置 kotlinOptions 的 freeCompilerArgs = listOf("-Xcontext-receivers")
 * 上下文接收器，限定执行方法的上下文
 */
/**
 * dp 转 px
 * 返回Float
 */
context(View)
val Float.dp
    get() = this * resources.displayMetrics.density

context(View)
val Int.dp
    get() = this.toFloat().dp

/**
 * dp 转 px
 * 返回Int 四舍五入计算px整数
 */
context(View)
val Float.dp2px
    get() = (this.dp + 0.5).toInt()

context(View)
val Int.dp2px
    get() = this.toFloat().dp2px

/**
 * sp 转 px
 * 返回Float
 */
context(View)
val Float.sp
    get() = this * resources.displayMetrics.scaledDensity

context(View)
val Int.sp
    get() = this.toFloat().sp

/**
 * sp 转 px
 * 返回Int 四舍五入计算px整数
 */
context(View)
val Float.sp2px
    get() = (this.sp + 0.5).toInt()

context(View)
val Int.sp2px
    get() = this.toFloat().sp2px