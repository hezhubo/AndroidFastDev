package com.hezb.fastdev.demo.test

import android.view.View
import com.hezb.fastdev.demo.R
import com.hezb.fastdev.demo.databinding.DemoLayoutHolderLiveItemBinding
import com.hezb.fastdev.demo.model.LiveInfo
import com.hezb.framework.image.load
import com.hezb.framework.ui.recyclerview.RecyclerItemViewHolder

/**
 * Project Name: AndroidFastDev
 * File Name:    LiveViewHolder
 *
 * Description: TODO.
 *
 * @author  hezhubo
 * @date    2023年03月27日 01:33
 */
class LiveViewHolder(itemView: View) : RecyclerItemViewHolder(itemView) {

    private val binding = DemoLayoutHolderLiveItemBinding.bind(itemView)

    override fun bindModel(model: Any) {
        if (model is LiveInfo) {
            binding.livePicture.load(model.logo) {
                placeholder(R.drawable.ic_launcher_background)
            }
            binding.liveTitle.text = model.liveTitle
            binding.liveAnchor.text = model.nickName
        }
    }

}