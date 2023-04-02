package com.hezb.fastdev.demo

import android.os.Bundle
import com.hezb.framework.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentFragment(fragment = MainFragment())
    }

}