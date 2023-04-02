package com.hezb.framework.network

/**
 * Project Name: AndroidFastDev
 * File Name:    RequestClient
 *
 * Description: 网络请求客户端.
 *
 * @author  hezhubo
 * @date    2023年03月24日 18:43
 */
abstract class RequestClient<T> {

    /**
     * 实际的网络请求的客户端
     */
    abstract fun getClient(): T

}