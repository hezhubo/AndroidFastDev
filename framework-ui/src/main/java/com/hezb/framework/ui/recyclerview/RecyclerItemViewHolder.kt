package com.hezb.framework.ui.recyclerview

import android.view.View

/**
 * Project Name: AndroidFastDev
 * File Name:    RecyclerItemViewHolder
 *
 * Description: BaseRecyclerViewHolder实例.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:32
 */
abstract class RecyclerItemViewHolder(itemView: View) : BaseRecyclerViewHolder(itemView) {

    /**
     * 统一绑定数据
     *
     * @param model 数据实例
     */
    abstract fun bindModel(model: Any)

}