package com.hezb.framework.permission

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Project Name: AndroidFastDev
 * File Name:    PermissionUtil
 *
 * Description: 权限控制工具类.
 *
 * @author  hezhubo
 * @date    2023年03月29日 15:00
 */
object PermissionUtil {

    /**
     * 获取权限中文名
     *
     * @param context
     * @param permission 权限
     * @return
     */
    fun getPermissionName(context: Context, permission: String): String {
        return when (permission) {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                context.getString(R.string.framework_permission_external_storage)
            }
            Manifest.permission.READ_PHONE_STATE -> {
                context.getString(R.string.framework_permission_read_phone_state)
            }
            Manifest.permission.CAMERA -> {
                context.getString(R.string.framework_permission_camera)
            }
            Manifest.permission.RECORD_AUDIO -> {
                context.getString(R.string.framework_permission_record_audio)
            }
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                context.getString(R.string.framework_permission_access_fine_location)
            }
            else -> {
                permission.removePrefix("android.permission.")
            }
        }
    }

    /**
     * 获取权限中文名
     *
     * @param context
     * @param permissions 权限集合
     * @return
     */
    fun getPermissionName(context: Context, permissions: MutableList<String>): String {
        if (permissions.isEmpty()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        for ((index, permission) in permissions.withIndex()) {
            stringBuilder.append(getPermissionName(context, permission))
            if (index != permissions.size - 1) {
                stringBuilder.append("、")
            }
        }
        return stringBuilder.toString()
    }


    /**
     * 检查缺少该权限
     *
     * @param permission 需要检查的权限名
     * @return 是否缺少该权限
     */
    fun lackPermission(context: Context, permission: String): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            false
        } else ContextCompat.checkSelfPermission(
            context,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查是否所有权限已授予
     *
     * @param context
     * @param permissions
     *
     * @return
     */
    fun isAllGranted(context: Context, permissions: Array<String>): Boolean {
        if (permissions.isNotEmpty()) {
            for (permission in permissions) {
                if (lackPermission(context, permission)) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 跳转系统应用设置页intent
     *
     * @param context
     */
    fun enterSettingIntent(context: Context): Intent {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", context.packageName, null)
        return intent
    }

    /**
     * 跳转允许安装未知源应用设置页intent
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun enterInstallPackagesIntent(context: Context): Intent {
        val intent = Intent()
        intent.action = Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
        intent.data = Uri.fromParts("package", context.packageName, null)
        return intent
    }

    /**
     * 创建开启权限引导对话框
     *
     * @param context
     * @param tips    提示语
     */
    private fun createPermissionGuideDialog(context: Context, tips: String): PermissionDialog {
        return PermissionDialog(
            context,
            content = context.getString(R.string.framework_permission_open_setting_tips, tips)
        ).apply {
            setCancelText(context.getString(R.string.framework_permission_open_later))
            setConfirmText(context.getString(R.string.framework_permission_open_setting))
        }
    }

    /**
     * 显示开启权限引导对话框
     *
     * @param context
     * @param permission
     * @param dialogCallback
     * @param cancelable     能否按返回键关闭弹窗
     */
    fun showPermissionGuideDialog(
        context: Context,
        permission: String,
        dialogCallback: PermissionDialog.DialogCallback,
        cancelable: Boolean = true
    ) {
        val dialog = createPermissionGuideDialog(context, getPermissionName(context, permission))
        dialog.setCancelable(cancelable)
        dialog.setDialogCallback(dialogCallback)
        dialog.show()
    }

    /**
     * 显示开启权限引导对话框
     *
     * @param context
     * @param permissions
     * @param dialogCallback
     * @param cancelable     能否按返回键关闭弹窗
     */
    fun showPermissionGuideDialog(
        context: Context,
        permissions: MutableList<String>,
        dialogCallback: PermissionDialog.DialogCallback,
        cancelable: Boolean = true
    ) {
        val dialog = createPermissionGuideDialog(context, getPermissionName(context, permissions))
        dialog.setCancelable(cancelable)
        dialog.setDialogCallback(dialogCallback)
        dialog.show()
    }

    /**
     * 校验当前承载权限申请弹窗的页面的状态，返回null则无法弹出申请弹窗
     *
     * @return currentActivity
     */
    private fun checkActivityState(context: Context): FragmentActivity? {
        val currentActivity: FragmentActivity = when {
            (context is FragmentActivity) -> {
                context
            }
            (context is ContextWrapper && context.baseContext is FragmentActivity) -> {
                context.baseContext as FragmentActivity
            }
            else -> {
                null
            }
        } ?: return null
        if (currentActivity.isFinishing || currentActivity.isDestroyed) {
            return null
        }
        return currentActivity
    }

    /**
     * 校验是否需要显示权限申请弹窗
     *
     * @param context
     * @param tips
     * @param permissions
     * @param iPermissionCallback
     *
     * @return 弹窗实例，若为null则无需申请权限
     */
    fun checkPermissionRequest(
        context: Context,
        tips: String,
        permissions: Array<String>,
        iPermissionCallback: IPermissionCallback? = null
    ): PermissionDialog? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return null // 6.0 以下，无需弹窗
        }
        if (isAllGranted(context, permissions)) {
            return null // 已有权限，无需弹窗
        }
        val currentActivity = checkActivityState(context) ?: return null

        val dialog = PermissionDialog(context, content = tips)
        dialog.setDialogCallback(object : PermissionDialog.DialogCallback {
            override fun onConfirm() {
                requestPermissionCallback(currentActivity, permissions, iPermissionCallback)
            }

            override fun onCancel() {
                iPermissionCallback?.onCancel()
            }

            override fun onDismiss() {
            }
        })
        dialog.show()
        return dialog
    }

    /**
     * 申请权限
     *
     * @param fragmentActivity
     * @param permissions
     * @param iPermissionCallback
     */
    fun requestPermissionCallback(
        fragmentActivity: FragmentActivity,
        permissions: Array<String>,
        iPermissionCallback: IPermissionCallback? = null
    ) {
        PermissionCallbackFragment.requestPermission(
            fragmentActivity,
            permissions,
            iPermissionCallback
        )
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
        PermissionCallbackFragment.goSettingGrantPermissions(
            fragmentActivity,
            permissions,
            iPermissionCallback
        )
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
        PermissionCallbackFragment.goSettingGrantInstallPackage(
            fragmentActivity,
            iPermissionCallback
        )
    }

}