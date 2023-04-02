package com.hezb.framework.permission

/**
 * Project Name: AndroidFastDev
 * File Name:    IPermissionCallback
 *
 * Description: 权限请求回调.
 *
 * @author  hezhubo
 * @date    2023年03月29日 14:48
 */
interface IPermissionCallback {

    /**
     * 单个权限授予成功
     *
     * @param permission
     */
    fun onPermissionGranted(permission: String)

    /**
     * 所有权限授予成功
     */
    fun onAllGranted()

    /**
     * 取消申请
     */
    fun onCancel()

    /**
     * 拒绝不再询问
     *
     * @param permissions 需要去设置页才能开启的权限集合
     */
    fun onRejectRequest(permissions: MutableList<String>)

}