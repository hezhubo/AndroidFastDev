package com.hezb.framework.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange

/**
 * Project Name: AndroidFastDev
 * File Name:    ImageOptions
 *
 * Description: 图片加载配置项.
 *
 * @author  hezhubo
 * @date    2023年03月28日 11:32
 */
class ImageOptions private constructor(builder: Builder) {

    /** 磁盘缓存规则 */
    val diskCacheType: DiskCacheType = builder.diskCacheType
    /** 是否缓存到内存中 */
    val memoryCacheable: Boolean = builder.memoryCacheable

    /** 加载优先级 */
    val priority: Priority = builder.priority

    /** 缩放方式 */
    val scaleType: ScaleType = builder.scaleType

    /** 默认图id */
    val placeholderResId: Int = builder.placeholderResId
    /** 默认图 */
    val placeholderDrawable: Drawable? = builder.placeholderDrawable

    /** 加载出错图 */
    val errorResId: Int = builder.errorResId
    /** 加载出错图 */
    val errorDrawable: Drawable? = builder.errorDrawable

    /** 图片目标尺寸 */
    val size: Size? = builder.size

    /** 图片解码配置 */
    val bitmapConfig: Bitmap.Config? = builder.bitmapConfig

    /** 缩略图缩放大小 0~1f */
    val sizeMultiplier: Float = builder.sizeMultiplier

    /** 是否显示加载动画 */
    val showAnim: Boolean = builder.showAnim
    /** 加载动画id */
    val animResId: Int = builder.animResId

    /** 圆角大小 */
    val roundRadius: Int = builder.roundRadius
    /** 圆角外边距 */
    val roundMargin: Int = builder.roundMargin

    /** 高斯模糊值 0~25 */
    val blurRadius: Int = builder.blurRadius
    /** 高斯模糊采样倍数 */
    val blurSampling: Int = builder.blurSampling

    fun newBuilder(): Builder {
        return Builder(this)
    }

    class Builder {

        internal var diskCacheType: DiskCacheType = DiskCacheType.ALL
        internal var memoryCacheable: Boolean = true
        internal var priority: Priority = Priority.NORMAL
        internal var scaleType: ScaleType = ScaleType.None
        internal var placeholderResId: Int = 0
        internal var placeholderDrawable: Drawable? = null
        internal var errorResId = 0
        internal var errorDrawable: Drawable? = null
        internal var size: Size? = null
        internal var bitmapConfig: Bitmap.Config? = null
        internal var sizeMultiplier: Float = 0f
        internal var showAnim: Boolean = false
        internal var animResId: Int = 0
        internal var roundRadius: Int = 0
        internal var roundMargin: Int = 0
        internal var blurRadius: Int = 0
        internal var blurSampling: Int = 0

        constructor()

        internal constructor(options: ImageOptions) {
            diskCacheType = options.diskCacheType
            memoryCacheable = options.memoryCacheable
            priority = options.priority
            scaleType = options.scaleType
            placeholderResId = options.placeholderResId
            placeholderDrawable = options.placeholderDrawable
            errorResId = options.errorResId
            errorDrawable = options.errorDrawable
            size = options.size
            bitmapConfig = options.bitmapConfig
            sizeMultiplier = options.sizeMultiplier
            showAnim = options.showAnim
            animResId = options.animResId
            roundRadius = options.roundRadius
            roundMargin = options.roundMargin
            blurRadius = options.blurRadius
            blurSampling = options.blurSampling
        }

        fun diskCacheStrategy(diskCacheType: DiskCacheType) = apply {
            this.diskCacheType = diskCacheType
        }

        fun memoryCache(enable: Boolean) = apply {
            memoryCacheable = enable
        }

        fun priority(priority: Priority) = apply {
            this.priority = priority
        }

        fun scaleType(scaleType: ScaleType) = apply {
            this.scaleType = scaleType
        }

        fun placeholder(@DrawableRes id: Int) = apply {
            placeholderResId = id
        }

        fun placeholder(drawable: Drawable?) = apply {
            placeholderDrawable = drawable
        }

        fun error(@DrawableRes id: Int) = apply {
            errorResId = id
        }

        fun error(drawable: Drawable?) = apply {
            errorDrawable = drawable
        }

        fun override(width: Int, height: Int) = apply {
            size = Size(width, height)
        }

        fun format(config: Bitmap.Config) = apply {
            bitmapConfig = config
        }

        fun thumbnail(@FloatRange(from = 0.0, to = 1.0) multiplier: Float) = apply {
            sizeMultiplier = multiplier
        }

        fun showAnim(@AnimRes id: Int = android.R.anim.fade_in) = apply {
            showAnim = true
            animResId = id
        }

        fun dontAnimate() = apply {
            showAnim = false
        }

        fun round(radius: Int, margin: Int = 0) = apply {
            roundRadius = radius
            roundMargin = margin
        }

        fun blur(@IntRange(from = 1, to = 25) radius: Int, sampling: Int = 1) = apply {
            blurRadius = radius
            blurSampling = sampling
        }

        fun build(): ImageOptions {
            return ImageOptions(this)
        }

    }

    /**
     * 磁盘缓存规则
     */
    sealed class DiskCacheType {
        object DATA : DiskCacheType()
        object ALL : DiskCacheType()
        object NONE : DiskCacheType()
    }

    /**
     * 加载优先级
     * {@link com.bumptech.glide.Priority}
     */
    sealed class Priority {
        object IMMEDIATE : Priority()
        object HIGH : Priority()
        object NORMAL : Priority()
        object LOW : Priority()
    }

    /**
     * 缩放方式
     * {@link com.bumptech.glide.load.resource.bitmap.DownsampleStrategy}
     */
    sealed class ScaleType {
        object FitCenter : ScaleType()
        object CenterCrop : ScaleType()
        object CenterInside : ScaleType()
        object None : ScaleType()
    }

}