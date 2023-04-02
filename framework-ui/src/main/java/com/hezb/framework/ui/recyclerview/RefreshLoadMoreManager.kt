package com.hezb.framework.ui.recyclerview

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hezb.framework.ui.recyclerview.empty.EmptyState
import com.hezb.framework.ui.recyclerview.loadmore.LoadMoreState

/**
 * Project Name: AndroidFastDev
 * File Name:    RefreshLoadMoreManager
 *
 * Description: 下拉刷新，加载更多管理者.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:45
 */
class RefreshLoadMoreManager {

    enum class Mode {
        /**
         * 下拉刷新模式
         */
        REFRESH,
        /**
         * 加载更多模式
         */
        LOAD_MORE,
        /**
         * 下拉刷新和加载更多模式
         */
        BOTH
    }

    companion object {
        /** 默认状态 */
        const val STATE_DEFAULT = 0
        /** 正在刷新 */
        const val STATE_REFRESHING = 1
        /** 正在加载更多 */
        const val STATE_LOADING_MORE = 2
    }

    /** 当前状态 */
    var currentState = STATE_DEFAULT
        private set
    /** 是否有下一页 */
    private var hasMore = false

    /** 下拉刷新控件 */
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    /** 列表适配器 */
    private var recyclerViewAdapter: RecyclerViewAdapter<*, out RecyclerItemViewHolder>? = null

    private var refreshLoadMoreCallback: IRefreshLoadMoreCallback? = null

    fun onAttached(
        mode: Mode,
        swipeRefreshLayout: SwipeRefreshLayout?,
        recyclerViewAdapter: RecyclerViewAdapter<*, out RecyclerItemViewHolder>?,
        refreshLoadMoreCallback: IRefreshLoadMoreCallback?
    ) {
        this.swipeRefreshLayout = swipeRefreshLayout
        this.recyclerViewAdapter = recyclerViewAdapter
        this.refreshLoadMoreCallback = refreshLoadMoreCallback

        swipeRefreshLayout?.let {
            if (mode == Mode.BOTH || mode == Mode.REFRESH) {
                it.setOnRefreshListener {
                    manualRefresh()
                }
            } else {
                it.isEnabled = false
            }
        }

        recyclerViewAdapter?.let {
            if (mode == Mode.BOTH || mode == Mode.LOAD_MORE) {
                it.setHasLoadMore(true)
            } else {
                it.setHasLoadMore(false)
            }
            it.iRefreshLoadMoreCallback = object : IRefreshLoadMoreCallback {

                override fun toRefresh() {
                    manualRefresh()
                }

                override fun toLoadMore() {
                    manualLoadMore()
                }

            }
        }

    }

    fun onDetached() {
        this.swipeRefreshLayout = null
        this.recyclerViewAdapter = null
        this.refreshLoadMoreCallback = null
        this.currentState = STATE_DEFAULT
    }

    /**
     * 请求完成
     */
    fun onRequestComplete() {
        currentState = STATE_DEFAULT
    }

    /**
     * 请求成功
     */
    fun onRequestSuccess() {
        swipeRefreshLayout?.isRefreshing = false
        recyclerViewAdapter?.setEmptyState(EmptyState.DEFAULT)
    }

    /**
     * 请求失败
     */
    fun onRequestFailure(hasConnect: Boolean, message: String?) {
        if (currentState == STATE_LOADING_MORE) {
            setLoadMoreError(message)
        }
        swipeRefreshLayout?.isRefreshing = false
        if (hasConnect) {
            recyclerViewAdapter?.setEmptyState(EmptyState.ERROR(message))
        } else {
            recyclerViewAdapter?.setEmptyState(EmptyState.NO_NETWORK)
        }
    }

    /**
     * 重置为可刷新状态
     */
    fun setLoadMoreEnable() {
        hasMore = true
        recyclerViewAdapter?.setLoadMoreState(LoadMoreState.DEFAULT)
    }

    /**
     * 已全部加载完成
     */
    fun setLoadMoreEnd() {
        hasMore = false
        recyclerViewAdapter?.setLoadMoreState(LoadMoreState.END)
    }

    /**
     * 加载更多出错
     */
    private fun setLoadMoreError(message: String?) {
        recyclerViewAdapter?.setLoadMoreState(LoadMoreState.FAILURE(message))
    }

    /**
     * 执行刷新（带动画）
     */
    fun toRefresh() {
        swipeRefreshLayout?.isRefreshing = true
        manualRefresh()
    }

    /**
     * 手动执行刷新
     */
    fun manualRefresh() {
        if (currentState != STATE_DEFAULT) {
            swipeRefreshLayout?.isRefreshing = false
            return
        }
        currentState = STATE_REFRESHING
        recyclerViewAdapter?.setEmptyState(EmptyState.LOADING)
        refreshLoadMoreCallback?.toRefresh()
    }

    /**
     * 加载更多
     */
    fun manualLoadMore() {
        if (!hasMore || currentState != STATE_DEFAULT) {
            return
        }
        currentState = STATE_LOADING_MORE
        recyclerViewAdapter?.setLoadMoreState(LoadMoreState.LOADING)
        refreshLoadMoreCallback?.toLoadMore()
    }

}