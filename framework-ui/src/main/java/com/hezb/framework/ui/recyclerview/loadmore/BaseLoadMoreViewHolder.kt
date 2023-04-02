package com.hezb.framework.ui.recyclerview.loadmore

import android.view.View
import com.hezb.framework.ui.recyclerview.BaseRecyclerViewHolder

/**
 * Project Name: AndroidFastDev
 * File Name:    BaseLoadMoreViewHolder
 *
 * Description: 加载更多列表视图基类.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:36
 */
abstract class BaseLoadMoreViewHolder(itemView: View) : BaseRecyclerViewHolder(itemView) {

    private var state: LoadMoreState = LoadMoreState.DEFAULT

    /**
     * 绑定加载更多状态
     *
     * @param state
     * @param isEmpty 是否空数据
     */
    open fun bindState(state: LoadMoreState, isEmpty: Boolean) {
        this.state = state
        when (state) {
            LoadMoreState.DEFAULT -> onDefault()
            LoadMoreState.LOADING -> onLoading()
            is LoadMoreState.FAILURE -> onLoadFailure(state.message)
            LoadMoreState.END -> onLoadEnd()
        }
    }

    protected abstract fun onDefault()

    protected abstract fun onLoading()

    protected abstract fun onLoadFailure(message: String?)

    protected abstract fun onLoadEnd()

}