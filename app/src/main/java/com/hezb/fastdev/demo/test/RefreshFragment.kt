package com.hezb.fastdev.demo.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.hezb.fastdev.demo.databinding.DemoFragmentRefreshBinding
import com.hezb.fastdev.demo.network.RequestManager
import com.hezb.fastdev.demo.viewmodel.LiveInfoListViewModel
import com.hezb.framework.base.BaseFragment
import com.hezb.framework.network.response.BaseJsonResponseVM
import com.hezb.framework.network.utlis.NetworkUtil
import com.hezb.framework.ui.recyclerview.IRefreshLoadMoreCallback
import com.hezb.framework.ui.recyclerview.OnItemClickListener
import com.hezb.framework.ui.recyclerview.RefreshLoadMoreManager
import com.hezb.framework.utils.ToastUtil

/**
 * Project Name: AndroidFastDev
 * File Name:    RefreshFragment
 *
 * Description: TODO.
 *
 * @author  hezhubo
 * @date    2023年03月27日 01:23
 */
class RefreshFragment : BaseFragment() {

    private var _binding: DemoFragmentRefreshBinding? = null

    private var refreshLoadMoreManager: RefreshLoadMoreManager? = null

    private var liveInfoListViewModel: LiveInfoListViewModel? = null

    private fun getBinding() = _binding!!

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        return DemoFragmentRefreshBinding.inflate(inflater, container, false).also { _binding = it }
    }

    override fun initAllMember(savedInstanceState: Bundle?) {
        super.initAllMember(savedInstanceState)

        val adapter = LiveListAdapter()
        adapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(itemView: View, position: Int) {
                ToastUtil.show(context, adapter.getModel(position)?.liveTitle)
            }
        }
        getBinding().recyclerView.layoutManager = LinearLayoutManager(activity)
        getBinding().recyclerView.adapter = adapter
        refreshLoadMoreManager = RefreshLoadMoreManager().apply {
            onAttached(
                RefreshLoadMoreManager.Mode.BOTH,
                getBinding().swipeRefreshLayout,
                adapter,
                object : IRefreshLoadMoreCallback {
                    override fun toRefresh() {
                        liveInfoListViewModel?.refresh(RequestManager.INSTANCE.getApiService())
                    }

                    override fun toLoadMore() {
                        liveInfoListViewModel?.loadMore(RequestManager.INSTANCE.getApiService())
                    }
                })
        }

        liveInfoListViewModel =
            ViewModelProvider(this)[LiveInfoListViewModel::class.java].also {
                it.getLiveInfoListLiveData().observe(this) { liveInfoList ->
                    refreshLoadMoreManager?.currentState?.let { state ->
                        if (state == RefreshLoadMoreManager.STATE_REFRESHING) {
                            if (liveInfoList != null) {
                                adapter.replaceAll(liveInfoList)
                            } else {
                                adapter.clear()
                            }
                        } else if (state == RefreshLoadMoreManager.STATE_LOADING_MORE) {
                            if (liveInfoList != null) {
                                adapter.addAll(liveInfoList)
                            }
                        }
                        refreshLoadMoreManager?.onRequestComplete()
                    }
                }
                it.getHasMoreLiveData().observe(this) { hasMore ->
                    if (hasMore) {
                        refreshLoadMoreManager?.setLoadMoreEnable()
                    } else {
                        refreshLoadMoreManager?.setLoadMoreEnd()
                    }
                }
                it.getStateLiveData().observe(this) { state ->
                    when (state) {
                        is BaseJsonResponseVM.State.START -> {
                        }
                        is BaseJsonResponseVM.State.SUCCESS -> {
                            refreshLoadMoreManager?.onRequestSuccess()
                        }
                        is BaseJsonResponseVM.State.FAILURE -> {
                            refreshLoadMoreManager?.onRequestFailure(
                                NetworkUtil.hasConnect(requireContext()), state.exception.message
                            )
                            refreshLoadMoreManager?.onRequestComplete()
                        }
                    }
                }
            }
    }

    override fun lazyLoad() {
        super.lazyLoad()
        refreshLoadMoreManager?.toRefresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        refreshLoadMoreManager?.onDetached()
        _binding = null
    }

}