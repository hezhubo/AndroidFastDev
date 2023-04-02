package com.hezb.framework.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hezb.framework.ui.recyclerview.empty.BaseEmptyViewHolder
import com.hezb.framework.ui.recyclerview.empty.EmptyState
import com.hezb.framework.ui.recyclerview.loadmore.BaseLoadMoreViewHolder
import com.hezb.framework.ui.recyclerview.loadmore.LoadMoreState
import com.hezb.framework.ui.recyclerview.loadmore.LoadMoreViewHolder
import com.hezb.framework.ui.setOnSingleClickListener
import java.util.*
import kotlin.collections.ArrayList

/**
 * Project Name: AndroidFastDev
 * File Name:    RecyclerViewAdapter
 *
 * Description: 内部维护单个数据集合的RecyclerView适配器.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:35
 */
abstract class RecyclerViewAdapter<T : Any, VH : RecyclerItemViewHolder> :
    BaseRecyclerAdapter<BaseRecyclerViewHolder>() {

    companion object {
        const val VIEW_TYPE_EMPTY  = 1
        const val VIEW_TYPE_HEADER = 2
        const val VIEW_TYPE_FOOTER = 3
        const val VIEW_TYPE_LOAD_MORE = 4
    }

    protected val mModelList: MutableList<T> = ArrayList()
    /** 头部项高度 */
    protected var headerItemHeight = 0
    /** 尾部项高度 */
    protected var footerItemHeight = 0
    /** 当前空数据状态 */
    private var emptyState: EmptyState = EmptyState.DEFAULT
    /** 是否有当前加载更多项 */
    private var isHasLoadMoreItem = false
    /** 当前加载更多状态 */
    private var loadMoreState: LoadMoreState = LoadMoreState.DEFAULT

    /** 下拉刷新加载更多适配器交互接口 */
    var iRefreshLoadMoreCallback: IRefreshLoadMoreCallback? = null

    /** 项点击防连点时间间隔 */
    var itemClickInterval: Long = 1000
    /** 项点击监听 */
    var onItemClickListener: OnItemClickListener? = null
    /** 长按监听 */
    var onItemLongClickListener: OnItemLongClickListener? = null

    /**
     * 当前绑定的recyclerView
     */
    var attachedRecyclerView: RecyclerView? = null
        private set

    override fun getItemViewType(position: Int): Int {
        if (position == 0 && hasHeader()) {
            return VIEW_TYPE_HEADER
        }
        if (position < 2 && mModelList.isEmpty() && hasEmpty()) {
            if (hasHeader() && position == 1) {
                return VIEW_TYPE_EMPTY
            } else if (position == 0) {
                return VIEW_TYPE_EMPTY
            }
        }
        if (hasFooter() && position >= mModelList.size) {
            if (hasHeader()) {
                if (mModelList.isEmpty() && hasEmpty()) {
                    if (position == 2) {
                        return VIEW_TYPE_FOOTER
                    }
                } else {
                    if (position == mModelList.size + 1) {
                        return VIEW_TYPE_FOOTER
                    }
                }
            } else {
                if (mModelList.isEmpty() && hasEmpty()) {
                    if (position == 1) {
                        return VIEW_TYPE_FOOTER
                    }
                } else {
                    if (position == mModelList.size) {
                        return VIEW_TYPE_FOOTER
                    }
                }
            }
        }
        if (mModelList.isNotEmpty() && hasLoadMore() && position == itemCount - 1) {
            return VIEW_TYPE_LOAD_MORE
        }
        return getContentItemViewType(getModelIndex(position))
    }

    override fun getItemCount(): Int {
        var count = mModelList.size
        if (hasHeader()) {
            count++
        }
        if (hasFooter()) {
            count++
        }
        if (mModelList.isEmpty() && hasEmpty()) {
            count++
        }
        if (mModelList.isNotEmpty() && hasLoadMore()) {
            count++
        }
        return count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
        val typeHolder = when (viewType) {
            VIEW_TYPE_EMPTY -> createEmptyViewHolder(parent)?.apply {
                if (headerItemHeight > 0 && footerItemHeight > 0) {
                    itemView.layoutParams.height =
                        parent.height - (headerItemHeight + footerItemHeight)
                }
            }
            VIEW_TYPE_HEADER -> createHeaderViewHolder(parent)
            VIEW_TYPE_FOOTER -> createFooterViewHolder(parent)
            VIEW_TYPE_LOAD_MORE -> createLoadMoreViewHolder(parent)
            else -> null
        }
        if (typeHolder != null) {
            return typeHolder
        }
        val itemView =
            LayoutInflater.from(parent.context).inflate(getItemLayoutId(viewType), parent, false)
        val holder = createItemViewHolder(itemView, viewType)
        onItemClickListener?.let { listener ->
            holder.itemView.setOnSingleClickListener(itemClickInterval) {
                listener.onItemClick(it, holder.bindingAdapterPosition)
            }
        }
        onItemLongClickListener?.let { listener ->
            holder.itemView.setOnLongClickListener {
                listener.onItemLongClick(it, holder.bindingAdapterPosition)
                true
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        if (holder is RecyclerItemViewHolder) {
            holder.bindModel(mModelList[position])
        } else {
            when (holder.itemViewType) {
                VIEW_TYPE_EMPTY -> {
                    if (holder is BaseEmptyViewHolder) {
                        holder.bindState(emptyState)
                    }
                }
                VIEW_TYPE_HEADER -> {
                    onBindHeaderViewHolder(holder)
                }
                VIEW_TYPE_FOOTER -> {
                    onBindFooterViewHolder(holder)
                }
                VIEW_TYPE_LOAD_MORE -> {
                    if (holder is BaseLoadMoreViewHolder) {
                        holder.bindState(loadMoreState, mModelList.isEmpty())
                    }
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: BaseRecyclerViewHolder) {
        if (holder is BaseLoadMoreViewHolder && hasLoadMore()) {
            iRefreshLoadMoreCallback?.toLoadMore()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        attachedRecyclerView = recyclerView
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (spanOneRow(position)) {
                        return layoutManager.spanCount
                    }
                    return when (getItemViewType(position)) {
                        VIEW_TYPE_LOAD_MORE, VIEW_TYPE_HEADER, VIEW_TYPE_EMPTY -> {
                            layoutManager.spanCount
                        }
                        else -> {
                            1
                        }
                    }
                }
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        attachedRecyclerView = null
    }

    /**
     * 当前项是否为占满一行
     *
     * @param position
     */
    protected open fun spanOneRow(position: Int): Boolean {
        return false
    }

    /**
     * 获取指定adapter位置的数据项所在的集合位置
     *
     * @param position adapter位置
     */
    protected fun getModelIndex(position: Int): Int {
        val index = if (hasHeader()) {
            position - 1
        } else {
            position
        }
        if (index in 0 until mModelList.size) {
            return index
        }
        return -1
    }

    /**
     * 获取数据项在adapter的位置
     *
     * @param modelIndex 数据集合中的位置
     */
    protected fun getModelItemPosition(modelIndex: Int): Int {
        if (modelIndex >= mModelList.size || mModelList.isEmpty()) {
            return -1
        }
        if (hasHeader()) {
            return modelIndex + 1
        }
        return modelIndex
    }

    /**
     * 获取空数据项在adapter的位置
     */
    private fun getEmptyItemPosition(): Int {
        if (mModelList.isNotEmpty() || !hasEmpty()) {
            return -1
        }
        if (hasHeader()) {
            return 1
        }
        return 0
    }

    /**
     * 获取加载更多据项在adapter的位置
     */
    private fun getLoadMoreItemPosition(): Int {
        if (hasLoadMore()) {
            return itemCount - 1
        }
        return -1
    }

    /**
     * 插入项
     *
     * @param index 插入位置
     * @param model
     */
    fun add(index: Int, model: T) {
        if (mModelList.isEmpty()) {
            mModelList.add(model)
            notifyItemInserted(getModelItemPosition(0))
        } else {
            mModelList.add(index, model)
            notifyItemInserted(getModelItemPosition(index))
        }
    }

    /**
     * 插入列表项
     *
     * @param modelList
     */
    fun addAll(modelList: List<T>) {
        if (modelList.isNotEmpty()) {
            val index = mModelList.size
            mModelList.addAll(modelList)
            notifyItemRangeInserted(getModelItemPosition(index), modelList.size)
        }
    }

    /**
     * 移除某位置的项
     *
     * @param index
     */
    fun remove(index: Int) {
        if (index >= mModelList.size) {
            return
        }
        mModelList.removeAt(index)
        val removedIndex = if (hasHeader()) {
            index + 1
        } else {
            index
        }
        notifyItemRemoved(removedIndex)
    }

    /**
     * 替换所有项
     *
     * @param modelList
     */
    fun replaceAll(modelList: List<T>) {
        clear()
        addAll(modelList)
    }

    /**
     * 清空数据
     */
    fun clear() {
        if (mModelList.isNotEmpty()) {
            val count = itemCount
            mModelList.clear()
            val startIndex = if (hasHeader()) {
                1
            } else {
                0
            }
            notifyItemRangeRemoved(startIndex, count)
        }
    }

    /**
     * 交换位置
     *
     * @param fromPosition
     * @param toPosition
     */
    fun swap(fromPosition: Int, toPosition: Int) {
        Collections.swap(mModelList, fromPosition, toPosition)
        notifyItemMoved(getModelItemPosition(fromPosition), getModelItemPosition(toPosition))
    }

    /**
     * 判断是否包含
     *
     * @param model
     * @return
     */
    operator fun contains(model: T): Boolean {
        return mModelList.contains(model)
    }

    /**
     * 获取某项数据对象
     *
     * @param index
     * @return
     */
    fun getModel(index: Int): T? {
        return if (mModelList.isNotEmpty() && (index in 0 until mModelList.size)) {
            mModelList[index]
        } else null
    }

    /**
     * 设置空数据状态
     *
     * @param state
     */
    fun setEmptyState(state: EmptyState) {
        this.emptyState = state
        val position = getEmptyItemPosition()
        if (position != -1) {
            notifyItemChanged(position)
        }
    }

    /**
     * 设置加载更多状态
     *
     * @param state
     */
    fun setLoadMoreState(state: LoadMoreState) {
        this.loadMoreState = state
        val position = getLoadMoreItemPosition()
        if (position != -1) {
            attachedRecyclerView?.let {
                if (it.isComputingLayout) {
                    it.post {
                        notifyItemChanged(position)
                    }
                } else {
                    notifyItemChanged(position)
                }
            }
        }
    }

    /**
     * 是否需要空数据项，默认需要
     *
     * @return 是否有空数据视图
     */
    protected open fun hasEmpty(): Boolean {
        return false
    }

    /**
     * 是否添加头部项，默认不添加
     * 若添加，请实现头部项创建方法
     *
     * @return 是否有头部
     */
    protected open fun hasHeader(): Boolean {
        return false
    }

    /**
     * 是否添加尾部项，默认不添加
     * 若添加，请实现尾部项创建方法
     * 当前设计的footer不可与加载更多共用
     *
     * @return 是否有尾部
     */
    protected open fun hasFooter(): Boolean {
        return false
    }

    /**
     * @return 是否需要展示加载更多
     */
    protected fun hasLoadMore(): Boolean {
        return isHasLoadMoreItem
    }

    /**
     * 设置是否需要加载更多
     */
    fun setHasLoadMore(hasLoadMore: Boolean) {
        isHasLoadMoreItem = hasLoadMore
    }

    /**
     * 子类可重写此方法实现自定义的空页面
     *
     * @return 空数据ViewHolder
     */
    protected open fun createEmptyViewHolder(parent: ViewGroup): BaseEmptyViewHolder? {
        return null
    }

    /**
     * 子类可重写此方法实现自定义的头部
     *
     * @return 头部ViewHolder
     */
    protected open fun createHeaderViewHolder(parent: ViewGroup): BaseRecyclerViewHolder? {
        return null
    }

    /**
     * 子类可重写此方法实现自定义的尾部
     *
     * @return 尾部ViewHolder
     */
    protected open fun createFooterViewHolder(parent: ViewGroup): BaseRecyclerViewHolder? {
        return null
    }

    /**
     * 子类可重写此方法实现自定义加载更多
     *
     * @return 加载更多ViewHolder
     */
    protected open fun createLoadMoreViewHolder(parent: ViewGroup): BaseLoadMoreViewHolder? {
        return LoadMoreViewHolder(parent).apply {
            failureLayout.setOnClickListener {
                iRefreshLoadMoreCallback?.toLoadMore()
            }
        }
    }

    /**
     * 子类可重写此方法实现头部项数据绑定
     *
     * @param holder
     */
    protected open fun onBindHeaderViewHolder(holder: BaseRecyclerViewHolder) {}

    /**
     * 子类可重写此方法实现尾部项数据绑定
     *
     * @param holder
     */
    protected open fun onBindFooterViewHolder(holder: BaseRecyclerViewHolder) {}

    /**
     * 列表项的Item类型
     * 子类实现不同的列表项
     *
     * @param index 数据集合的位置
     * @return
     */
    protected open fun getContentItemViewType(index: Int): Int {
        return 0
    }

    /**
     * 重写该方法，根据viewType设置item的布局id
     *
     * @param viewType 通过重写 getItemViewType() 设置，默认type是0
     * @return
     */
    protected abstract fun getItemLayoutId(viewType: Int): Int

    /**
     * 重写该方法，根据viewType创建对应的列表示图对象
     *
     * @param itemView
     * @param viewType 列表示图类型
     * @return
     */
    protected abstract fun createItemViewHolder(itemView: View, viewType: Int): VH

}