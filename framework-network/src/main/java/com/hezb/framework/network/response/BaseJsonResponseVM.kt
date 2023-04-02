package com.hezb.framework.network.response

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hezb.framework.network.RequestException
import com.hezb.framework.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

/**
 * Project Name: AndroidFastDev
 * File Name:    BaseJsonResponseVM
 *
 * Description: TODO.
 *
 * @author  hezhubo
 * @date    2023年03月24日 18:58
 */
abstract class BaseJsonResponseVM<T> : BaseViewModel<JsonResponseRepository<T>>() {

    sealed class State {
        /**
         * 开始请求
         */
        object START : State()

        /**
         * 请求成功
         */
        object SUCCESS : State()

        /**
         * 请求失败
         *
         * @param exception 请求异常时的异常信息
         */
        data class FAILURE(val exception: RequestException) : State()
    }

    /**
     * 当前请求状态
     */
    protected val stateLiveData: MutableLiveData<State> = MutableLiveData()

    /**
     * 开始请求
     */
    protected open fun onStart() {
        stateLiveData.value = State.START
    }

    /**
     * 数据获取并解析成功（code == 100）
     */
    protected open fun onSuccess() {
        stateLiveData.value = State.SUCCESS
    }

    /**
     * 数据获取失败
     *
     * @param e 异常
     */
    protected open fun onFailure(e: RequestException) {
        stateLiveData.value = State.FAILURE(e)
    }

    /**
     * 执行请求
     *
     * @param requestResponse
     */
    fun startRequest(requestResponse: suspend () -> JsonResponse<T>?) {
        viewModelScope.launch {
            repository.startRequest(
                requestResponse,
                onStart = { onStart() },
                onSuccess = { onSuccess() },
                onFailure = { e -> onFailure(e) }
            )
        }
    }

    fun getStateLiveData(): LiveData<State> {
        return stateLiveData
    }

}