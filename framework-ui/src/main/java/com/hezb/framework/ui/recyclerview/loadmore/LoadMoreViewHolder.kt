package com.hezb.framework.ui.recyclerview.loadmore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hezb.framework.ui.R
import com.hezb.framework.ui.findViewById

/**
 * Project Name: AndroidFastDev
 * File Name:    LoadMoreViewHolder
 *
 * Description: 加载更多ViewHolder.
 *
 * @author  hezhubo
 * @date    2023年03月26日 03:35
 */
class LoadMoreViewHolder(parent: ViewGroup) : BaseLoadMoreViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.framework_ui_recyclerview_load_more_item, parent, false)
) {
    private val height = itemView.layoutParams.height

    val loadingLayout: View by lazy { findViewById(R.id.loading_view)!! }
    val failureLayout: View by lazy { findViewById(R.id.load_failure_view)!! }
    val endLayout: View by lazy { findViewById(R.id.load_end_view)!! }

    val loadingText: TextView by lazy { findViewById(R.id.tv_loading)!! }
    val failureText: TextView by lazy { findViewById(R.id.tv_failure)!! }
    val endText: TextView by lazy { findViewById(R.id.tv_end)!! }

    override fun bindState(state: LoadMoreState, isEmpty: Boolean) {
        if (state is LoadMoreState.DEFAULT && isEmpty) {
            itemView.layoutParams.height = 1 // 1px
        } else {
            if (itemView.layoutParams.height != height) {
                itemView.layoutParams.height = height
            }
        }
        super.bindState(state, isEmpty)
    }

    override fun onDefault() {
        loadingLayout.visibility = View.GONE
        failureLayout.visibility = View.GONE
        endLayout.visibility = View.GONE
    }

    override fun onLoading() {
        loadingLayout.visibility = View.VISIBLE
        failureLayout.visibility = View.GONE
        endLayout.visibility = View.GONE
    }

    override fun onLoadFailure(message: String?) {
        loadingLayout.visibility = View.GONE
        failureLayout.visibility = View.VISIBLE
        endLayout.visibility = View.GONE
    }

    override fun onLoadEnd() {
        loadingLayout.visibility = View.GONE
        failureLayout.visibility = View.GONE
        endLayout.visibility = View.VISIBLE
    }

}