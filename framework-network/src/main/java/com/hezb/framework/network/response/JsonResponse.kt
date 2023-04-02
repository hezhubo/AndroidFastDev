package com.hezb.framework.network.response

import com.google.gson.annotations.SerializedName

/**
 * Project Name: AndroidFastDev
 * File Name:    JsonResponse
 *
 * Description: 网络请求Json响应数据.
 *
 * @author  hezhubo
 * @date    2023年03月24日 18:55
 */
class JsonResponse<T> {

    @SerializedName("code")
    var code: Int = 0

    @SerializedName("message")
    var message: String? = null

    @SerializedName("result")
    var result: T? = null

}