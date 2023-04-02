package com.hezb.framework.ui.recyclerview.loadmore

/**
 * Project Name: AndroidFastDev
 * File Name:    LoadMoreState
 *
 * Description: 加载更多的状态.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:36
 */
sealed class LoadMoreState {

    /**
     * 默认状态
     */
    object DEFAULT : LoadMoreState()

    /**
     * 加载中
     */
    object LOADING : LoadMoreState()

    /**
     * 加载失败
     */
    data class FAILURE(val message: String?) : LoadMoreState()

    /**
     * 加载完成
     */
    object END : LoadMoreState()

}
