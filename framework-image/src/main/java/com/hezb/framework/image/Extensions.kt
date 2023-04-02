package com.hezb.framework.image

import android.widget.ImageView
import androidx.fragment.app.Fragment

/**
 * Project Name: AndroidFastDev
 * File Name:    Extensions
 *
 * Description: 扩展函数.
 *
 * @author  hezhubo
 * @date    2023年03月28日 20:54
 */

/**
 * 加载图片
 *
 * @param url 图片地址
 * @param options 加载参数
 */
fun ImageView.load(url: String?, options: (ImageOptions.Builder.() -> Unit)? = null) {
    ImageLoader.load(this, url, options)
}

/**
 * 加载图片
 *
 * @param fragment 容器所在fragment
 * @param url 图片地址
 * @param options 加载参数
 */
fun ImageView.loadWithFragment(
    fragment: Fragment,
    url: String?,
    options: (ImageOptions.Builder.() -> Unit)? = null
) {
    ImageLoader.load(fragment, this, url, options)
}