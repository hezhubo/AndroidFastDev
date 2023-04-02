package com.hezb.framework.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.fragment.app.Fragment

/**
 * Project Name: AndroidFastDev
 * File Name:    ImageLoader
 *
 * Description: 图片加载工具.
 *
 * @author  hezhubo
 * @date    2023年03月28日 00:23
 */
object ImageLoader {

    /** 默认配置项 */
    val defaultOptions: ImageOptions by lazy { ImageOptions.Builder().build() }

    /**
     * 获取配置项
     *
     * @param options 额外的加载参数
     */
    fun getImageOptions(options: (ImageOptions.Builder.() -> Unit)? = null): ImageOptions {
        return if (options == null) {
            defaultOptions
        } else {
            defaultOptions.newBuilder().apply(options).build()
        }
    }

    /**
     * 加载图片
     *
     * @param imageView 图片显示的容器
     * @param url 图片地址
     * @param options 加载参数
     */
    fun load(
        imageView: ImageView,
        url: String?,
        options: (ImageOptions.Builder.() -> Unit)? = null
    ) {
        GlideHelper.load<Drawable>(imageView, url, getImageOptions(options), null)
    }

    /**
     * 加载图片
     *
     * @param imageView 图片显示的容器
     * @param url 图片地址
     * @param requestListener 加载监听器
     * @param options 加载参数
     */
    inline fun <reified ResourceType> load(
        imageView: ImageView,
        url: String?,
        requestListener: ImageRequestListener<ResourceType>,
        noinline options: (ImageOptions.Builder.() -> Unit)? = null
    ) {
        GlideHelper.load(imageView, url, getImageOptions(options), requestListener)
    }

    /**
     * 加载图片
     *
     * @param fragment 容器所在fragment
     * @param imageView 图片显示的容器
     * @param url 图片地址
     * @param options 加载参数
     */
    fun load(
        fragment: Fragment,
        imageView: ImageView,
        url: String?,
        options: (ImageOptions.Builder.() -> Unit)? = null
    ) {
        GlideHelper.load<Drawable>(fragment, imageView, url, getImageOptions(options), null)
    }

    /**
     * 加载图片
     *
     * @param fragment 容器所在fragment
     * @param imageView 图片显示的容器
     * @param url 图片地址
     * @param requestListener 加载监听器
     * @param options 加载参数
     */
    inline fun <reified ResourceType> load(
        fragment: Fragment,
        imageView: ImageView,
        url: String?,
        requestListener: ImageRequestListener<ResourceType>,
        noinline options: (ImageOptions.Builder.() -> Unit)? = null
    ) {
        GlideHelper.load(fragment, imageView, url, getImageOptions(options), requestListener)
    }

    /**
     * 异步加载位图，结果回调至requestListener
     *
     * @param context 上下文
     * @param url 图片地址
     * @param requestListener 图片加载监听器
     * @param options 加载参数
     */
    fun loadBitmap(
        context: Context,
        url: String?,
        requestListener: ImageRequestListener<Bitmap>,
        options: (ImageOptions.Builder.() -> Unit)? = null
    ) {
        GlideHelper.load(context, url, getImageOptions(options), requestListener)
    }

    /**
     * 同步加载位图
     *
     * @param context 上下文
     * @param url 图片地址
     * @param options 加载参数
     */
    fun loadBitmapSync(
        context: Context,
        url: String?,
        options: (ImageOptions.Builder.() -> Unit)? = null
    ): Bitmap? {
        if (url.isNullOrEmpty()) {
            return null
        }
        return GlideHelper.loadSync(context, url, getImageOptions(options), null)
    }

    /**
     * 图片加载监听器
     *
     * @property T 加载的类型
     */
    interface ImageRequestListener<T> {
        /**
         * 当加载资源图片开始的时候
         */
        fun onBefore()

        /**
         * 当资源图片准备好的时候
         *
         * @param resource
         * @param isFromMemoryCache
         * @param isFirstResource
         */
        fun onResourceReady(
            resource: T?,
            isFromMemoryCache: Boolean,
            isFirstResource: Boolean
        ): Boolean

        /**
         * 当加载图片出错的时候
         *
         * @param error
         */
        fun onException(error: Exception?): Boolean
    }

}