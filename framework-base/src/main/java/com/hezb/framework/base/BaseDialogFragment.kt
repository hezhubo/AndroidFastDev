package com.hezb.framework.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding

/**
 * Project Name: AndroidFastDev
 * File Name:    BaseDialogFragment
 *
 * Description: DialogFragment基类.
 *
 * @author  hezhubo
 * @date    2023年03月24日 09:58
 */
abstract class BaseDialogFragment : DialogFragment() {

    var onDismissListener: OnDismissListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 获取activity中的intent传递的bundle
        val params = Bundle()
        activity?.let {
            it.intent.extras?.let { extras->
                if (!extras.isEmpty) {
                    params.putAll(extras)
                }
            }
        }
        // 获取传入fragment的数据
        arguments?.let {
            if (!it.isEmpty) {
                params.putAll(arguments)
            }
        }

        initData(params)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = getViewBinding(inflater, container)
        return if (getLayoutId() == 0 && binding != null) {
            binding.root
        } else {
            inflater.inflate(getLayoutId(), container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAllMember(savedInstanceState)

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss()
    }

    /**
     * 统一处理fragment add时的状态问题
     *
     * @param manager
     * @param tag
     */
    override fun show(manager: FragmentManager, tag: String?) {
        val ft = manager.beginTransaction()
        val prev = manager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev) // 移除已存在的tag的fragment
        }
        ft.addToBackStack(null)
        if (isAdded) {
            ft.remove(this).commit() // 防止异常的连续add
        } else {
            ft.commit()
        }
        try {
            super.show(manager, tag)
        } catch (_: Exception) {}
    }

    /**
     * 布局id
     * 与getViewBinding冲突，优先使用getViewBinding创建视图
     *
     * @return
     */
    protected open fun getLayoutId(): Int {
        return 0
    }

    /**
     * 子类实现使用的ViewBinding创建视图
     *
     * @param inflater
     * @param container
     * @return
     */
    protected open fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding? {
        return null
    }

    /**
     * 初始化传入数据
     * 此方法在onCreate中被调用
     *
     * @param params 含intent中的extras
     */
    protected open fun initData(params: Bundle) {}

    /**
     * 初始化所有成员
     * 此方法在onActivityCreated中，懒加载前被调用
     *
     * @param savedInstanceState
     */
    protected open fun initAllMember(savedInstanceState: Bundle?) {}

    /**
     * 弹窗关闭监听器
     */
    interface OnDismissListener {
        fun onDismiss()
    }

}