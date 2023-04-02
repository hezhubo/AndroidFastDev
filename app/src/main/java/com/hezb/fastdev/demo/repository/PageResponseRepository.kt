package com.hezb.fastdev.demo.repository

import com.google.gson.JsonObject
import com.hezb.framework.network.response.JsonResponseRepository

/**
 * Project Name: AndroidFastDev
 * File Name:    PageResponseRepository
 *
 * Description: TODO.
 *
 * @author  hezhubo
 * @date    2023年03月27日 01:42
 */
abstract class PageResponseRepository<T> : JsonResponseRepository<T>() {
    companion object {
        private const val KEY_MODE = "more"
        private const val KEY_START_KEY = "startKey"
    }

    var hasMore = false
        protected set
    var startKey: String? = null
        protected set

    override fun parseResponseData(result: JsonObject) {
        super.parseResponseData(result)
        hasMore = result.get(KEY_MODE).asBoolean
        startKey = result.get(KEY_START_KEY).asString
    }

}