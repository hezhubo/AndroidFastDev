package com.hezb.framework.ui.recyclerview

import android.view.View

/**
 * Project Name: AndroidFastDev
 * File Name:    OnItemLongClickListener
 *
 * Description: 列表项长按监听器.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:35
 */
interface OnItemLongClickListener {

    fun onItemLongClick(itemView: View, position: Int)

}