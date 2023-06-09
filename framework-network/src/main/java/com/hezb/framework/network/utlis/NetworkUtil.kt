package com.hezb.framework.network.utlis

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import com.hezb.framework.base.AppManager
import com.hezb.framework.network.NetworkState
import com.hezb.framework.network.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException

/**
 * Project Name: AndroidFastDev
 * File Name:    NetworkUtil
 *
 * Description: 网络工具类.
 *
 * @author  hezhubo
 * @date    2023年03月25日 20:14
 */
object NetworkUtil {

    /**
     * 获取联网状态
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getNetworkState(context: Context?): NetworkState {
        (context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let { connectivityManager ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.activeNetwork?.let { network ->
                    connectivityManager.getNetworkCapabilities(network)
                        ?.let { networkCapabilities ->
                            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                                return if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                    NetworkState.WIFI
                                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                    NetworkState.MOBILE
                                } else {
                                    NetworkState.OTHER
                                }
                            }
                        }
                }
            } else {
                connectivityManager.activeNetworkInfo?.let { networkInfo ->
                    if (networkInfo.isConnected) {
                        return when (networkInfo.type) {
                            ConnectivityManager.TYPE_WIFI -> NetworkState.WIFI
                            ConnectivityManager.TYPE_MOBILE -> NetworkState.MOBILE
                            else -> NetworkState.OTHER
                        }
                    }
                }
            }
        }
        return NetworkState.NONE
    }

    /**
     * 是否已连接
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun hasConnect(context: Context?): Boolean {
        return (context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let { connectivityManager ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return@let false
                val networkCapabilities =
                    connectivityManager.getNetworkCapabilities(network) ?: return@let false
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } else {
                connectivityManager.activeNetworkInfo?.isAvailable ?: false
            }
        } ?: false
    }

    /**
     * 获取当前网络类型
     *
     * @param context
     * @return NONE, MOBILE(移动网络类型), WIFI, UNKNOWN
     */
    @JvmStatic
    fun getNetworkType(context: Context?): String {
        val networkState = getNetworkState(context)
        if (networkState == NetworkState.MOBILE) {
            return mobileNetworkType(context!!)
        }
        return networkState.text
    }

    /**
     * 获取运营商信息
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getMobileOperator(context: Context?): String {
        context?.let {
            return when (getMobileOperatorCode(it)) {
                "46000", "46002", "46007" -> it.getString(R.string.framework_network_china_mobile)
                "46001" -> it.getString(R.string.framework_network_china_unicom)
                "46003" -> it.getString(R.string.framework_network_china_telecom)
                else -> it.getString(R.string.framework_network_unknown_operator)
            }
        }
        return AppManager.INSTANCE.getApplication()
            .getString(R.string.framework_network_unknown_operator)
    }

    /**
     * 获取运营商代码
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getMobileOperatorCode(context: Context?): String? {
        context?.applicationContext?.let {
            try {
                return (it.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager)?.simOperator
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 获取国家代码
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getNetworkCountryIso(context: Context?): String? {
        context?.applicationContext?.let {
            try {
                return (it.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager)?.networkCountryIso
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 获取本机IP(wifi)
     */
    @JvmStatic
    fun getLocalIpByWifi(context: Context?): String? {
        context?.applicationContext?.let {
            try {
                (it.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.connectionInfo?.ipAddress?.let { ipAddress ->
                    return String.format(
                        "%d.%d.%d.%d",
                        ipAddress and 0xff,
                        ipAddress shr 8 and 0xff,
                        ipAddress shr 16 and 0xff,
                        ipAddress shr 24 and 0xff
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 获取本机IP(2G/3G/4G)
     */
    @JvmStatic
    fun getLocalIpByMobile(): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            en?.let {
                while (en.hasMoreElements()) {
                    val intf = en.nextElement()
                    val enumIpAddr = intf.inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                            return inetAddress.getHostAddress()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * wifi状态下获取网关
     */
    @JvmStatic
    fun pingGatewayInWifi(context: Context?): String? {
        context?.applicationContext?.let {
            try {
                (it.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.dhcpInfo?.gateway?.let { gateway ->
                    return String.format(
                        "%d.%d.%d.%d",
                        gateway and 0xff,
                        gateway shr 8 and 0xff,
                        gateway shr 16 and 0xff,
                        gateway shr 24 and 0xff
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 获取本地DNS
     */
    @JvmStatic
    fun getLocalDns(dns: String): String {
        var process: Process? = null
        var str = ""
        try {
            process = Runtime.getRuntime().exec("getprop net.$dns")
            process?.let {
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                reader.use { br ->
                    str = br.readText()
                }
            }
            process.waitFor()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            try {
                process?.destroy()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return str.trim()
    }

    /**
     * 域名解析
     */
    @JvmStatic
    fun getDomainIp(domain: String): Map<String, Any> {
        val map = HashMap<String, Any>()
        var start: Long = 0
        var end: Long = 0
        var time: Long = 0
        var remoteInet: Array<InetAddress>? = null
        try {
            start = System.currentTimeMillis()
            remoteInet = InetAddress.getAllByName(domain)
            if (remoteInet != null) {
                end = System.currentTimeMillis()
                time = end - start
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            end = System.currentTimeMillis()
            time = end - start
            remoteInet = null
        } finally {
            remoteInet?.let {
                map["remoteInet"] = remoteInet
            }
            map["useTime"] = time
        }
        return map
    }

    /**
     * 获取移动网络类型
     * 需要声明并申请权限android.Manifest.permission.READ_PHONE_STATE
     *
     * @param context
     * @return
     */
    @JvmStatic
    @SuppressLint("MissingPermission")
    private fun mobileNetworkType(context: Context): String {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return NetworkState.MOBILE.text
        }
        try {
            return (context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager)?.networkType?.let { type ->
                when (type) {
                    TelephonyManager.NETWORK_TYPE_1xRTT // ~ 50-100 kbps
                    -> "2G"
                    TelephonyManager.NETWORK_TYPE_CDMA // ~ 14-64 kbps
                    -> "2G"
                    TelephonyManager.NETWORK_TYPE_EDGE // ~ 50-100 kbps
                    -> "2G"
                    TelephonyManager.NETWORK_TYPE_EVDO_0 // ~ 400-1000 kbps
                    -> "3G"
                    TelephonyManager.NETWORK_TYPE_EVDO_A // ~ 600-1400 kbps
                    -> "3G"
                    TelephonyManager.NETWORK_TYPE_GPRS // ~ 100 kbps
                    -> "2G"
                    TelephonyManager.NETWORK_TYPE_HSDPA // ~ 2-14 Mbps
                    -> "3G"
                    TelephonyManager.NETWORK_TYPE_HSPA // ~ 700-1700 kbps
                    -> "3G"
                    TelephonyManager.NETWORK_TYPE_HSUPA // ~ 1-23 Mbps
                    -> "3G"
                    TelephonyManager.NETWORK_TYPE_UMTS // ~ 400-7000 kbps
                    -> "3G"
                    TelephonyManager.NETWORK_TYPE_EHRPD // ~ 1-2 Mbps
                    -> "3G"
                    TelephonyManager.NETWORK_TYPE_EVDO_B // ~ 5 Mbps
                    -> "3G"
                    TelephonyManager.NETWORK_TYPE_HSPAP // ~ 10-20 Mbps
                    -> "3G"
                    TelephonyManager.NETWORK_TYPE_IDEN // ~25 kbps
                    -> "2G"
                    TelephonyManager.NETWORK_TYPE_LTE // ~ 10+ Mbps
                    -> "4G"
                    TelephonyManager.NETWORK_TYPE_NR
                    -> "5G"
                    else -> NetworkState.MOBILE.text
                }
            } ?: NetworkState.MOBILE.text
        } catch (e: Exception) {
            e.printStackTrace()
            return NetworkState.MOBILE.text
        }
    }

}