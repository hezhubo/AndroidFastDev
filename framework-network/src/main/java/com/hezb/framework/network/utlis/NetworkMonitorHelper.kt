package com.hezb.framework.network.utlis

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import com.hezb.framework.network.NetworkState
import org.greenrobot.eventbus.EventBus

/**
 * Project Name: AndroidFastDev
 * File Name:    NetworkMonitorHelper
 *
 * Description: 网络变化监听工具.
 *
 * @author  hezhubo
 * @date    2023年04月05日 21:40
 */
object NetworkMonitorHelper {

    private const val ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"

    /** 当前网络是否已连接 */
    private var isConnected: Boolean = false
    /** 当前网络状态 */
    private var currentNetworkState: NetworkState = NetworkState.NONE

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun registerNetworkCallback(context: Context): ConnectivityManager.NetworkCallback? {
        val connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                val networkCallback = getNetworkCallback()
                connectivityManager.registerDefaultNetworkCallback(networkCallback)
                return networkCallback
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val networkRequest = NetworkRequest.Builder().build()
                val networkCallback = getNetworkCallback()
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
                return networkCallback
            }
        }
        return null
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun unregisterMonitor(context: Context, networkCallback: ConnectivityManager.NetworkCallback?) {
        networkCallback?.let {
            val connectivityManager =
                context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    @JvmStatic
    fun registerBroadcastReceiver(context: Context): BroadcastReceiver {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ANDROID_NET_CHANGE_ACTION)
        val networkReceiver = getNetworkReceiver()
        context.applicationContext.registerReceiver(networkReceiver, intentFilter)
        return networkReceiver
    }

    @JvmStatic
    fun unregisterMonitor(context: Context, networkReceiver: BroadcastReceiver?) {
        networkReceiver?.let {
            context.applicationContext.unregisterReceiver(networkReceiver)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getNetworkCallback(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // 网络连接成功
                isConnected = true
                EventBus.getDefault().post(NetworkStateChangeEvent(isConnected))
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                // 网络已断开连接
                isConnected = false
                EventBus.getDefault().post(NetworkStateChangeEvent(isConnected))
            }

            override fun onUnavailable() {
                super.onUnavailable()
                // 网络连接超时或网络连接不可达
                isConnected = false
                EventBus.getDefault().post(NetworkStateChangeEvent(isConnected))
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                // 网络状态修改（有网）
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                    val networkState = if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        NetworkState.WIFI
                    } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        NetworkState.MOBILE
                    } else {
                        NetworkState.OTHER
                    }
                    if (networkState != currentNetworkState) {
                        currentNetworkState = networkState
                        EventBus.getDefault().post(NetworkStateChangeEvent(isConnected, currentNetworkState))
                    }
                }
            }
        }
    }

    private fun getNetworkReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (ANDROID_NET_CHANGE_ACTION.equals(intent.action, true)) {
                    val networkState = NetworkUtil.getNetworkState(context)
                    isConnected = networkState != NetworkState.NONE
                    currentNetworkState = networkState
                    EventBus.getDefault().post(NetworkStateChangeEvent(isConnected, currentNetworkState))
                }
            }
        }
    }

}

/**
 * 网络状态变化事件
 *
 * @param isConnected 当前网络是否已连接
 * @param isConnected 当前网络状态，可能为null
 */
class NetworkStateChangeEvent(val isConnected: Boolean, val networkState: NetworkState? = null)