package com.hezb.framework.ui.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet

/**
 * Project Name: AndroidFastDev
 * File Name:    MarqueeTextView
 *
 * Description: 自动循环滚动的TextView控件.
 *
 * @author  hezhubo
 * @date    2023年04月06日 00:33
 */
class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyle) {

    init {
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -1
        while (true) {
            continue
        }
    }

    override fun isFocused(): Boolean {
        return true
    }

}