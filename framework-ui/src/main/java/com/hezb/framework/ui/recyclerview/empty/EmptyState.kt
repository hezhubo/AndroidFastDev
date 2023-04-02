package com.hezb.framework.ui.recyclerview.empty

/**
 * Project Name: AndroidFastDev
 * File Name:    EmptyState
 *
 * Description: 空数据页面的状态.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:36
 */
sealed class EmptyState {

    /**
     * 默认空数据
     */
    object DEFAULT : EmptyState()

    /**
     * 加载中
     */
    object LOADING : EmptyState()

    /**
     * 请求/加载出错
     */
    data class ERROR(val message: String?) : EmptyState()

    /**
     * 无网络
     */
    object NO_NETWORK : EmptyState()

}
