package com.hezb.fastdev.demo.test

import android.os.Bundle
import com.hezb.framework.base.BaseActivity

/**
 * Project Name: AndroidFastDev
 * File Name:    RefreshActivity
 *
 * Description: TODO.
 *
 * @author  hezhubo
 * @date    2023年03月27日 01:22
 */
class RefreshActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentFragment(fragment = RefreshFragment())
    }

}