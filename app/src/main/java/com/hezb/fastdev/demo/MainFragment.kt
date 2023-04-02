package com.hezb.fastdev.demo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.hezb.fastdev.demo.databinding.DemoFragmentMainBinding
import com.hezb.fastdev.demo.test.RefreshActivity
import com.hezb.framework.base.BaseFragment

/**
 * Project Name: AndroidFastDev
 * File Name:    MainFragment
 *
 * Description: TODO.
 *
 * @author  hezhubo
 * @date    2023年03月27日 01:19
 */
class MainFragment : BaseFragment() {

    private var _binding: DemoFragmentMainBinding? = null

    private fun getBinding() = _binding!!

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        return DemoFragmentMainBinding.inflate(inflater, container, false).also { _binding = it }
    }

    override fun initAllMember(savedInstanceState: Bundle?) {
        super.initAllMember(savedInstanceState)

        getBinding().btnRefresh.setOnClickListener {
            startActivity(Intent(requireActivity(), RefreshActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}