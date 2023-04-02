package com.hezb.framework.ui.recyclerview.empty

import android.view.View
import com.hezb.framework.ui.recyclerview.BaseRecyclerViewHolder

/**
 * Project Name: AndroidFastDev
 * File Name:    BaseEmptyViewHolder
 *
 * Description: 空数据/无网络/刷新出错 列表视图基类.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:36
 */
abstract class BaseEmptyViewHolder(itemView: View) : BaseRecyclerViewHolder(itemView) {

    private var state: EmptyState = EmptyState.DEFAULT

    /**
     * 绑定空数据状态
     *
     * @param state
     */
    fun bindState(state: EmptyState) {
        this.state = state
        when (state) {
            EmptyState.DEFAULT -> onDefault()
            EmptyState.LOADING -> onLoading()
            is EmptyState.ERROR -> onError(state.message)
            EmptyState.NO_NETWORK -> onNoNetwork()
        }
    }

    protected abstract fun onDefault()

    protected abstract fun onLoading()

    protected abstract fun onError(message: String?)

    protected abstract fun onNoNetwork()

}