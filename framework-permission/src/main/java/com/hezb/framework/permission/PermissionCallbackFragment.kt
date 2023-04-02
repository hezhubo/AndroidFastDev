package com.hezb.framework.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

/**
 * Project Name: AndroidFastDev
 * File Name:    PermissionCallbackFragment
 *
 * Description: 不可见Fragment, 用于统一处理权限回调.
 *
 * @author  hezhubo
 * @date    2023年03月29日 14:50
 */
class PermissionCallbackFragment : Fragment() {

    companion object {
        private const val TAG = "PermissionCallbackFragment"

        private fun getPermissionFragment(fragmentActivity: FragmentActivity) : PermissionCallbackFragment {
            val fragmentManager = fragmentActivity.supportFragmentManager
            val permissionFragment: PermissionCallbackFragment
            val fragment = fragmentManager.findFragmentByTag(TAG)
            if (fragment != null) {
                permissionFragment = fragment as PermissionCallbackFragment
            } else {
                permissionFragment = PermissionCallbackFragment()
                fragmentManager.beginTransaction()
                    .add(permissionFragment, TAG)
                    .commitNowAllowingStateLoss()
            }
            return permissionFragment
        }

        /**
         * 申请权限
         *
         * @param fragmentActivity
         * @param permissions
         * @param iPermissionCallback
         */
        fun requestPermission(
            fragmentActivity: FragmentActivity,
            permissions: Array<String>,
            iPermissionCallback: IPermissionCallback? = null
        ) {
            fragmentActivity.lifecycleScope.launch {
                getPermissionFragment(fragmentActivity).requestPermissions(
                    permissions,
                    iPermissionCallback
                )
            }
        }

        /**
         * 跳转设置页开启权限
         *
         * @param fragmentActivity
         * @param permissions
         * @param iPermissionCallback
         */
        fun goSettingGrantPermissions(
            fragmentActivity: FragmentActivity,
            permissions: Array<String>,
            iPermissionCallback: IPermissionCallback? = null
        ) {
            fragmentActivity.lifecycleScope.launch {
                getPermissionFragment(fragmentActivity).goSettingGrantPermissions(
                    permissions,
                    iPermissionCallback
                )
            }
        }

        /**
         * 跳转设置页开启允许安装未知源应用权限
         *
         * @param fragmentActivity
         * @param iPermissionCallback
         */
        fun goSettingGrantInstallPackage(
            fragmentActivity: FragmentActivity,
            iPermissionCallback: IPermissionCallback? = null
        ) {
            fragmentActivity.lifecycleScope.launch {
                getPermissionFragment(fragmentActivity).goSettingGrantInstallPackage(
                    iPermissionCallback
                )
            }
        }

    }

    /** 需要申请的权限 */
    private var toRequestPermissions: Array<String>? = null

    /** 权限申请回调 */
    private var iPermissionCallback: IPermissionCallback? = null

    /**
     * 请求权限
     *
     * @param permissions
     * @param callback
     */
    fun requestPermissions(permissions: Array<String>, callback: IPermissionCallback?) {
        toRequestPermissions = permissions
        lifecycleScope.launchWhenStarted {
            iPermissionCallback = callback
            permissionsLauncher.launch(permissions)
        }
    }

    /**
     * 去设置页开启权限
     *
     * @param permissions
     * @param callback
     */
    fun goSettingGrantPermissions(permissions: Array<String>, callback: IPermissionCallback?) {
        lifecycleScope.launchWhenStarted {
            iPermissionCallback = callback
            activity?.let { activity ->
                toRequestPermissions = permissions
                activityResultLauncher.launch(PermissionUtil.enterSettingIntent(activity))
            }
        }
    }

    /**
     * 跳转设置页开启允许安装未知源应用权限
     *
     * @param callback
     */
    fun goSettingGrantInstallPackage(callback: IPermissionCallback?) {
        lifecycleScope.launchWhenStarted {
            iPermissionCallback = callback
            activity?.let { activity ->
                // 8.0以上版本允许安装未知源应用权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    toRequestPermissions = arrayOf(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                    activityResultLauncher.launch(PermissionUtil.enterInstallPackagesIntent(activity))
                } else {
                    iPermissionCallback?.onAllGranted()
                    removeFragment()
                }
            }
        }
    }

    /**
     * 移除当前fragment
     */
    private fun removeFragment() {
        try {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        iPermissionCallback = null
        permissionsLauncher.unregister()
        activityResultLauncher.unregister()
    }

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var isAllPermissionGranted = true
            val goSettingPermissionList: MutableList<String> = ArrayList()
            for ((permission, isGranted) in it) {
                if (isGranted) {
                    iPermissionCallback?.onPermissionGranted(permission) // 回调单个已同意权限
                    continue
                }
                // 拒绝授权但可再次询问
                if (shouldShowRequestPermissionRationale(permission)) {
                    isAllPermissionGranted = false
                    continue
                }
                // 拒绝且不再询问
                goSettingPermissionList.add(permission)
            }
            if (goSettingPermissionList.isNotEmpty()) {
                // 引导跳转设置手动开启权限
                iPermissionCallback?.onRejectRequest(goSettingPermissionList)
            } else if (isAllPermissionGranted) {
                iPermissionCallback?.onAllGranted()
            }
            removeFragment()
        }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            toRequestPermissions?.let {
                if (it.isNotEmpty()) {
                    activity?.let { activity ->
                        var isAllGranted = true
                        for (permission in it) {
                            if (permission == Manifest.permission.REQUEST_INSTALL_PACKAGES) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                                    && !activity.packageManager.canRequestPackageInstalls()) {
                                    isAllGranted = false
                                }
                            } else if (ContextCompat.checkSelfPermission(
                                    activity,
                                    permission
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                isAllGranted = false
                            } else {
                                iPermissionCallback?.onPermissionGranted(permission) // 回调单个已同意权限
                            }
                        }
                        if (isAllGranted) {
                            iPermissionCallback?.onAllGranted()
                        }
                    }
                }
            }
            removeFragment()
        }

}