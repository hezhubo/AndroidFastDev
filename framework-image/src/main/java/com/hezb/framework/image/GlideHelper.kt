package com.hezb.framework.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.*
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File

/**
 * Project Name: AndroidFastDev
 * File Name:    GlideHelper
 *
 * Description: Glide辅助工具.
 *
 * @author  hezhubo
 * @date    2023年03月28日 15:49
 */
object GlideHelper {

    /**
     * 获取对应类型的RequestBuilder
     *
     * @param ResourceType 支持Bitmap、Drawable、File、GifDrawable
     * @param requestManager
     * @param source 图片源
     * @return
     */
    @JvmStatic
    inline fun <reified ResourceType> getRequestBuilder(
        requestManager: RequestManager,
        source: Any?
    ): RequestBuilder<ResourceType> {
        val builder = requestManager.`as`(ResourceType::class.java)
        return when (source) {
            is String -> {
                builder.load(source)
            }
            is Uri -> {
                builder.load(source)
            }
            is File -> {
                builder.load(source)
            }
            is Int -> {
                builder.load(source)
            }
            is ByteArray -> {
                builder.load(source)
            }
            is Bitmap -> {
                builder.load(source)
            }
            is Drawable -> {
                builder.load(source)
            }
            else -> {
                builder.load(source)
            }
        }
    }

    /**
     * 把配置项设置到RequestBuilder中
     *
     * @param ResourceType 支持Bitmap、Drawable、File、GifDrawable
     * @param requestBuilder
     * @param options 配置项
     * @return
     */
    @JvmStatic
    fun <ResourceType> setBuilderOption(requestBuilder: RequestBuilder<ResourceType>, options: ImageOptions) : RequestBuilder<ResourceType> {
        var builder = when (options.diskCacheType) {
            ImageOptions.DiskCacheType.ALL -> {
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL)
            }
            ImageOptions.DiskCacheType.DATA -> {
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.DATA)
            }
            ImageOptions.DiskCacheType.NONE -> {
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE)
            }
        }

        if (!options.memoryCacheable) {
            builder = builder.skipMemoryCache(true)
        }

        builder = when (options.priority) {
            ImageOptions.Priority.NORMAL -> {
                builder.priority(Priority.NORMAL)
            }
            ImageOptions.Priority.IMMEDIATE -> {
                builder.priority(Priority.IMMEDIATE)
            }
            ImageOptions.Priority.HIGH -> {
                builder.priority(Priority.HIGH)
            }
            ImageOptions.Priority.LOW -> {
                builder.priority(Priority.LOW)
            }
        }

        when (options.scaleType) {
            ImageOptions.ScaleType.None -> {}
            ImageOptions.ScaleType.FitCenter -> {
                builder = builder.fitCenter()
            }
            ImageOptions.ScaleType.CenterCrop -> {
                builder = builder.centerCrop()
            }
            ImageOptions.ScaleType.CenterInside -> {
                builder = builder.centerInside()
            }
        }

        if (options.placeholderResId != 0) {
            builder = builder.placeholder(options.placeholderResId)
        } else if (options.placeholderDrawable != null) {
            builder = builder.placeholder(options.placeholderDrawable)
        }

        if (options.errorResId != 0) {
            builder = builder.error(options.errorResId)
        } else if (options.errorDrawable != null) {
            builder = builder.error(options.errorDrawable)
        }

        if (options.size != null) {
            builder = builder.override(options.size.width, options.size.height)
        }

        when (options.bitmapConfig) {
            Bitmap.Config.ARGB_8888 -> {
                builder = builder.format(DecodeFormat.PREFER_ARGB_8888)
            }
            Bitmap.Config.RGB_565 -> {
                builder = builder.format(DecodeFormat.PREFER_RGB_565)
            }
            else -> {
                // format(DecodeFormat.DEFAULT) 无需再设置
            }
        }

        if (options.sizeMultiplier > 0) {
            builder = builder.thumbnail(options.sizeMultiplier)
        }

        builder = if (options.showAnim) {
            builder.transition(GenericTransitionOptions.with(options.animResId))
        } else {
            builder.dontAnimate()
        }

        if (options.roundRadius > 0) {
            builder = builder.transform(RoundedCornersTransformation(options.roundRadius, options.roundMargin))
        }

        if (options.blurRadius > 0) {
            builder = builder.transform(BlurTransformation(options.blurRadius, options.blurSampling))
        }

        return builder
    }

    /**
     * 设置图片加载监听器
     *
     * @param ResourceType 支持Bitmap、Drawable、File、GifDrawable
     * @param requestBuilder
     * @param requestListener 加载监听器
     * @return
     */
    @JvmStatic
    fun <ResourceType> setRequestListener(
        requestBuilder: RequestBuilder<ResourceType>,
        requestListener: ImageLoader.ImageRequestListener<ResourceType>
    ) : RequestBuilder<ResourceType> {
        requestListener.onBefore()
        return requestBuilder.listener(object : RequestListener<ResourceType> {
            override fun onResourceReady(
                resource: ResourceType?,
                model: Any?,
                target: Target<ResourceType>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return requestListener.onResourceReady(
                    resource,
                    dataSource == DataSource.MEMORY_CACHE,
                    isFirstResource
                )
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<ResourceType>?,
                isFirstResource: Boolean
            ): Boolean {
                return requestListener.onException(e)
            }
        })
    }

    @JvmStatic
    inline fun <reified ResourceType> load(
        context: Context,
        imageView: ImageView,
        source: Any?,
        options: ImageOptions,
        requestListener: ImageLoader.ImageRequestListener<ResourceType>?
    ) {
        load(Glide.with(context), imageView, source, options, requestListener)
    }

    @JvmStatic
    inline fun <reified ResourceType> load(
        activity: FragmentActivity,
        imageView: ImageView,
        source: Any?,
        options: ImageOptions,
        requestListener: ImageLoader.ImageRequestListener<ResourceType>?
    ) {
        load(Glide.with(activity), imageView, source, options, requestListener)
    }

    @JvmStatic
    inline fun <reified ResourceType> load(
        fragment: Fragment,
        imageView: ImageView,
        source: Any?,
        options: ImageOptions,
        requestListener: ImageLoader.ImageRequestListener<ResourceType>?
    ) {
        load(Glide.with(fragment), imageView, source, options, requestListener)
    }

    @JvmStatic
    inline fun <reified ResourceType> load(
        imageView: ImageView,
        source: Any?,
        options: ImageOptions,
        requestListener: ImageLoader.ImageRequestListener<ResourceType>?
    ) {
        load(Glide.with(imageView), imageView, source, options, requestListener)
    }

    @JvmStatic
    inline fun <reified ResourceType> load(
        requestManager: RequestManager,
        imageView: ImageView,
        source: Any?,
        options: ImageOptions,
        requestListener: ImageLoader.ImageRequestListener<ResourceType>?
    ) {
        var builder = getRequestBuilder<ResourceType>(requestManager, source)
        builder = setBuilderOption(builder, options)
        if (requestListener != null) {
            builder = setRequestListener(builder, requestListener)
        }
        builder.into(imageView)
    }

    @JvmStatic
    inline fun <reified ResourceType> load(
        context: Context,
        target: Target<ResourceType>,
        source: Any?,
        options: ImageOptions,
        requestListener: ImageLoader.ImageRequestListener<ResourceType>?
    ) {
        load(Glide.with(context), target, source, options, requestListener)
    }

    @JvmStatic
    inline fun <reified ResourceType> load(
        requestManager: RequestManager,
        target: Target<ResourceType>,
        source: Any?,
        options: ImageOptions,
        requestListener: ImageLoader.ImageRequestListener<ResourceType>?
    ) {
        var builder = getRequestBuilder<ResourceType>(requestManager, source)
        builder = setBuilderOption(builder, options)
        if (requestListener != null) {
            builder = setRequestListener(builder, requestListener)
        }
        builder.into(target)
    }

    @JvmStatic
    inline fun <reified ResourceType : Any> load(
        context: Context,
        source: Any?,
        options: ImageOptions,
        requestListener: ImageLoader.ImageRequestListener<ResourceType>?
    ): CustomTarget<ResourceType> {
        return load(Glide.with(context), source, options, requestListener)
    }

    @JvmStatic
    inline fun <reified ResourceType : Any> load(
        requestManager: RequestManager,
        source: Any?,
        options: ImageOptions,
        requestListener: ImageLoader.ImageRequestListener<ResourceType>?
    ): CustomTarget<ResourceType> {
        var builder = getRequestBuilder<ResourceType>(requestManager, source)
        builder = setBuilderOption(builder, options)
        if (requestListener != null) {
            builder = setRequestListener(builder, requestListener)
        }
        val width: Int
        val height:Int
        if (options.size != null) {
            width = options.size.width
            height = options.size.height
        } else {
            width = Target.SIZE_ORIGINAL
            height = Target.SIZE_ORIGINAL
        }
        return builder.into(object : CustomTarget<ResourceType>(width, height) {
            override fun onResourceReady(resource: ResourceType, transition: Transition<in ResourceType>?) {
                // 在requestListener中处理资源
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                requestManager.clear(this)
            }
        })
    }

    @JvmStatic
    inline fun <reified ResourceType> loadSync(
        context: Context,
        source: Any?,
        options: ImageOptions,
        requestListener: ImageLoader.ImageRequestListener<ResourceType>?
    ): ResourceType? {
        var builder = getRequestBuilder<ResourceType>(Glide.with(context), source)
        builder = setBuilderOption(builder, options)
        if (requestListener != null) {
            builder = setRequestListener(builder, requestListener)
        }
        return try {
            if (options.size != null) {
                builder.submit(options.size.width, options.size.height).get()
            } else {
                builder.submit().get()
            }
        } catch (e: Exception) {
            requestListener?.onException(e)
            null
        }
    }

    /**
     * 清理内存
     *
     * @param view
     */
    @JvmStatic
    fun clear(view: View) {
        Glide.with(view).clear(view)
    }

    /**
     * 清除内存缓存
     *
     * @param context
     */
    @JvmStatic
    fun clearMemory(context: Context) {
        Glide.get(context).clearMemory()
    }

    /**
     * 清除磁盘缓存
     *
     * @param context
     */
    @JvmStatic
    fun clearDiskCache(context: Context) {
        Glide.get(context).clearDiskCache()
    }

}