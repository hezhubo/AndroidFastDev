package com.hezb.framework.network

/**
 * Project Name: AndroidFastDev
 * File Name:    NetworkState
 *
 * Description: 网络状态类型.
 *
 * @author  hezhubo
 * @date    2023年03月24日 18:37
 */
sealed class NetworkState(val text: String) {
    /**
     * 无网络
     */
    object NONE : NetworkState("NONE")

    /**
     * 移动网络
     */
    object MOBILE : NetworkState("MOBILE")

    /**
     * WIFI网络
     */
    object WIFI : NetworkState("WIFI")

    /**
     * 其他网络
     */
    object OTHER : NetworkState("UNKNOWN")
}
