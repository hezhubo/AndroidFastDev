package com.hezb.fastdev.demo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.hezb.fastdev.demo.model.LiveInfo
import com.hezb.fastdev.demo.network.ApiService
import com.hezb.fastdev.demo.repository.PageResponseRepository
import com.hezb.framework.network.response.BaseJsonResponseVM

/**
 * Project Name: AndroidFastDev
 * File Name:    LiveInfoListViewModel
 *
 * Description: TODO.
 *
 * @author  hezhubo
 * @date    2023年03月27日 01:43
 */
class LiveInfoListViewModel : BaseJsonResponseVM<JsonElement>() {

    override val repository = LiveInfoListRepository()

    private val liveInfoListLiveData: MutableLiveData<MutableList<LiveInfo>?> = MutableLiveData()

    private val hasMoreLiveData: MutableLiveData<Boolean> = MutableLiveData()

    override fun onSuccess() {
        super.onSuccess()
        liveInfoListLiveData.postValue(repository.liveInfoList)
        hasMoreLiveData.postValue(repository.hasMore)
    }

    fun getLiveInfoListLiveData(): LiveData<MutableList<LiveInfo>?> {
        return liveInfoListLiveData
    }

    fun getHasMoreLiveData(): LiveData<Boolean> {
        return hasMoreLiveData
    }

    private fun requestLiveInfoList(apiService: ApiService, startKey: String?) {
        startRequest {
            val params = HashMap<String, String>()
            if (!startKey.isNullOrEmpty()) {
                params["startKey"] = startKey
            }
            apiService.getLiveList(params)
        }
    }

    fun refresh(apiService: ApiService) {
        requestLiveInfoList(apiService, null)
    }

    fun loadMore(apiService: ApiService) {
        requestLiveInfoList(apiService, repository.startKey)
    }

    class LiveInfoListRepository : PageResponseRepository<JsonElement>() {
        var liveInfoList: MutableList<LiveInfo>? = null

        override fun parseResponseData(result: JsonObject) {
            super.parseResponseData(result)
            if (result.has("data")) {
                liveInfoList = gson.fromJson(
                    result.getAsJsonArray("data"),
                    object : TypeToken<MutableList<LiveInfo>>() {}.type
                )
            }
        }
    }

}