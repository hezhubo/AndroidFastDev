package com.hezb.framework.permission

import android.Manifest
import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.provider.Settings
import com.hezb.framework.utils.LogUtil
import com.hezb.framework.utils.RomUtil

/**
 * Project Name: AndroidFastDev
 * File Name:    AlertWindowHelper
 *
 * Description: 悬浮窗权限辅助工具.
 *
 * @author  hezhubo
 * @date    2023年04月10日 00:00
 */
object AlertWindowHelper {

    /**
     * 是否有悬浮窗权限
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun canDrawOverlays(context: Context): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> Settings.canDrawOverlays(context)
            else -> checkOp(context)
        }
    }

    /**
     * 6.0以下校验是否有悬浮窗权限
     */
    @SuppressLint("DiscouragedPrivateApi")
    private fun checkOp(context: Context): Boolean {
        (context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager)?.let {
            try {
                val method = AppOpsManager::class.java.getDeclaredMethod(
                    "checkOp",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    String::class.java
                )
                return AppOpsManager.MODE_ALLOWED == method.invoke(
                    it,
                    24, // OP_SYSTEM_ALERT_WINDOW = 24 （对应悬浮窗的权限）
                    Binder.getCallingUid(),
                    context.packageName
                ) as Int
            } catch (e: Exception) {
                LogUtil.e("check op error!", e)
            }
        }
        return true
    }

    /**
     * 创建跳转悬浮窗设置页intent
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun enterDrawOverlaysIntent(context: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
        } else {
            if (RomUtil.isXiaomi()) {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
            } else if (RomUtil.isHuawei()) {
                Intent().apply {
                    setClassName(
                        "com.huawei.systemmanager",
                        "com.huawei.notificationmanager.ui.NotificationManagmentActivity"
                    )
                    putExtra("showTabsNumber", 1)
                }
            } else if (RomUtil.isVivo()) {
                Intent("com.vivo.permissionmanager").apply {
                    setClassName(
                        "com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.PurviewTabActivity"
                    )
                }
            } else if (RomUtil.isOppo()) {
                Intent().apply {
                    setClassName(
                        "com.color.safecenter",
                        "com.color.safecenter.permission.floatwindow.FloatWindowListActivity"
                    )
                    putExtra("packageName", context.packageName)
                    action = "com.color.safecenter"
                }
            } else if (RomUtil.isMeizu()) {
                Intent("com.meizu.safe.security.SHOW_APPSEC").apply {
                    setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity")
                    putExtra("packageName", context.packageName)
                }
            } else if (RomUtil.isSmartisan()) {
                Intent("com.smartisanos.security.action.SWITCHED_PERMISSIONS").apply {
                    setClassName(
                        "com.smartisanos.security",
                        "com.smartisanos.security.SwitchedPermissions"
                    )
                    putExtra("permission", arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW))
                }
            } else {
                PermissionUtil.enterSettingIntent(context)
            }
        }
    }

}