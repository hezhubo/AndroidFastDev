package com.hezb.framework.ui.recyclerview

import android.view.View

/**
 * Project Name: AndroidFastDev
 * File Name:    OnItemClickListener
 *
 * Description: 列表项点击监听器.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:35
 */
interface OnItemClickListener {

    fun onItemClick(itemView: View, position: Int)

}