package com.hezb.framework.utils

import android.os.Build
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/**
 * Project Name: AndroidFastDev
 * File Name:    RomUtil
 *
 * Description: 系统固件工具类.
 *
 * @author  hezhubo
 * @date    2023年04月09日 23:31
 */
object RomUtil {

    private val ROM_MIUI = "MIUI"
    private val ROM_EMUI = "EMUI"
    private val ROM_FLYME = "FLYME"
    private val ROM_OPPO = "OPPO"
    private val ROM_VIVO = "VIVO"
    private val ROM_SMARTISAN = "SMARTISAN"
    private val ROM_SAMSUNG = "SAMSUNG"
    // TODO realme Oneplus

    private val KEY_NAME_MIUI = "Xiaomi"
    private val KEY_NAME_EMUI = "HUAWEI"
    private val KEY_NAME_FLYME = "Meizu"
    private val KEY_NAME_OPPO = "OPPO"
    private val KEY_NAME_VIVO = "vivo"
    private val KEY_NAME_SAMSUNG = "samsung"

    private val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
    private val KEY_VERSION_EMUI = "ro.build.version.emui"
    private val KEY_VERSION_OPPO = "ro.build.version.opporom"
    private val KEY_VERSION_VIVO = "ro.vivo.os.version"
    private val KEY_VERSION_SMARTISAN = "ro.smartisan.version"

    private var romName: String? = null
    private fun check(rom: String): Boolean {
        romName?.let {
            return it == rom
        }

        romName = if (Build.MANUFACTURER.equals(KEY_NAME_MIUI, true)) {
            ROM_MIUI
        } else if (Build.MANUFACTURER.equals(KEY_NAME_EMUI, true)) {
            ROM_EMUI
        } else if (Build.MANUFACTURER.equals(KEY_NAME_FLYME, true) || Build.DISPLAY.uppercase(Locale.ROOT).contains(ROM_FLYME)) {
            ROM_FLYME
        } else if (Build.MANUFACTURER.equals(KEY_NAME_OPPO, true)) {
            ROM_OPPO
        } else if (Build.MANUFACTURER.equals(KEY_NAME_VIVO, true)) {
            ROM_VIVO
        } else if (Build.MANUFACTURER.equals(KEY_NAME_SAMSUNG, true)) {
            ROM_SAMSUNG
        } else if (!getProp(KEY_VERSION_MIUI).isNullOrEmpty()) {
            ROM_MIUI
        } else if (!getProp(KEY_VERSION_EMUI).isNullOrEmpty()) {
            ROM_EMUI
        } else if (!getProp(KEY_VERSION_OPPO).isNullOrEmpty()) {
            ROM_OPPO
        } else if (!getProp(KEY_VERSION_VIVO).isNullOrEmpty()) {
            ROM_VIVO
        } else if (!getProp(KEY_VERSION_SMARTISAN).isNullOrEmpty()) {
            ROM_SMARTISAN
        } else if (getProp(Build.BRAND)?.equals(KEY_NAME_SAMSUNG, true) == true) {
            ROM_SAMSUNG
        } else {
            Build.MANUFACTURER.uppercase(Locale.ROOT)
        }
        return romName == rom
    }

    private fun getProp(name: String): String? {
        try {
            val p = Runtime.getRuntime().exec("getprop $name")
            val reader = BufferedReader(InputStreamReader(p.inputStream), 1024)
            return reader.use {
                readLine()?.trim()
            }
        } catch (e: Exception) {
            LogUtil.e("exec getprop $name error!", e)
        }
        return null
    }

    /**
     * @return 是否为小米MIUI系统
     */
    @JvmStatic
    fun isXiaomi(): Boolean {
        return check(ROM_MIUI)
    }

    /**
     * @return 是否为华为ENUI系统
     */
    @JvmStatic
    fun isHuawei(): Boolean {
        return check(ROM_EMUI)
    }

    /**
     * @return 是否为魅族flyme系统
     */
    @JvmStatic
    fun isMeizu(): Boolean {
        return check(ROM_FLYME)
    }

    /**
     * @return 是否为OPPO系统
     */
    @JvmStatic
    fun isOppo(): Boolean {
        return check(ROM_OPPO)
    }

    /**
     * @return 是否为VIVO系统
     */
    @JvmStatic
    fun isVivo(): Boolean {
        return check(ROM_VIVO)
    }

    /**
     * @return 是否为锤子Smartisan系统
     */
    @JvmStatic
    fun isSmartisan(): Boolean {
        return check(ROM_SMARTISAN)
    }

    /**
     * @return 是否为三星Samsung系统
     */
    @JvmStatic
    fun isSamsung(): Boolean {
        return check(ROM_SAMSUNG)
    }

}