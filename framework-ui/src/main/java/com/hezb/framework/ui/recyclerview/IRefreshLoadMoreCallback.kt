package com.hezb.framework.ui.recyclerview

/**
 * Project Name: AndroidFastDev
 * File Name:    IRefreshLoadMoreCallback
 *
 * Description: 刷新、加载更多回调.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:32
 */
interface IRefreshLoadMoreCallback {

    /**
     * 执行刷新
     */
    fun toRefresh()

    /**
     * 执行加载更多
     */
    fun toLoadMore()

}