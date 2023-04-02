package com.hezb.framework.viewmodel

import androidx.lifecycle.ViewModel
import com.hezb.framework.repository.IRepository

/**
 * Project Name: AndroidFastDev
 * File Name:    BaseViewModel
 *
 * Description: ViewModel基类.
 *
 * @author  hezhubo
 * @date    2023年03月24日 10:30
 */
abstract class BaseViewModel<T: IRepository> : ViewModel() {

    protected abstract val repository: T

}