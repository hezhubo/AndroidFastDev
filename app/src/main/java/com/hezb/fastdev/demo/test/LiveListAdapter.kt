package com.hezb.fastdev.demo.test

import android.view.View
import android.view.ViewGroup
import com.hezb.fastdev.demo.R
import com.hezb.fastdev.demo.model.LiveInfo
import com.hezb.framework.ui.recyclerview.RecyclerViewAdapter
import com.hezb.framework.ui.recyclerview.empty.BaseEmptyViewHolder

/**
 * Project Name: AndroidFastDev
 * File Name:    LiveListAdapter
 *
 * Description: TODO.
 *
 * @author  hezhubo
 * @date    2023年03月27日 01:40
 */
class LiveListAdapter : RecyclerViewAdapter<LiveInfo, LiveViewHolder>() {

    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.demo_layout_holder_live_item
    }

    override fun createItemViewHolder(itemView: View, viewType: Int): LiveViewHolder {
        return LiveViewHolder(itemView)
    }

//    override fun hasEmpty(): Boolean {
//        return true
//    }
//
//    override fun createEmptyViewHolder(parent: ViewGroup): BaseEmptyViewHolder {
//        return EmptyViewHolder(parent).apply {
//            ypTipsView.setOnClickRefreshListener {
//                iRefreshLoadMoreCallback?.toRefresh()
//            }
//        }
//    }

}
