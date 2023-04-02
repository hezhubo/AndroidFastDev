package com.hezb.framework.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.hezb.framework.R

/**
 * Project Name: AndroidFastDev
 * File Name:    BaseActivity
 *
 * Description: Activity基类.
 *
 * @author  hezhubo
 * @date    2023年03月24日 09:57
 */
abstract class BaseActivity : AppCompatActivity() {

    private val contentView: FrameLayout by lazy {
        FrameLayout(this).apply {
            id = R.id.fl_container
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    private var mContentFragment: BaseFragment? = null

    private var isRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView()

        initView(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        isRunning = true
    }

    override fun onPause() {
        super.onPause()
        isRunning = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mContentFragment = null
    }

    fun getContext(): Context {
        return this
    }

    fun getContentFragment(): BaseFragment? {
        return mContentFragment
    }

    fun isRunning(): Boolean {
        return isRunning
    }

    /**
     * 页面布局id
     */
    protected open fun setContentView() {
        setContentView(contentView)
    }

    /**
     * 初始化视图
     *
     * @param savedInstanceState
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 设置当前页面的fragment
     *
     * @param id       默认布局id {R.id.fl_container}
     * @param fragment
     */
    @JvmOverloads
    fun setContentFragment(id: Int = R.id.fl_container, fragment: BaseFragment) {
        if (findViewById<View>(id) == null) {
            return
        }
        if (mContentFragment == null) {
            mContentFragment = fragment
            supportFragmentManager.beginTransaction().replace(id, fragment).commit()
        }
    }

}