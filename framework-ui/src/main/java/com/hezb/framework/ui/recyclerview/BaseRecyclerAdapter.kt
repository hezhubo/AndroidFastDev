package com.hezb.framework.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView

/**
 * Project Name: AndroidFastDev
 * File Name:    BaseRecyclerAdapter
 *
 * Description: RecyclerView.Adapter基类，泛型统一为BaseRecyclerViewHolder子类.
 *
 * @author  hezhubo
 * @date    2023年03月24日 19:30
 */
abstract class BaseRecyclerAdapter<VH : BaseRecyclerViewHolder> : RecyclerView.Adapter<VH>()