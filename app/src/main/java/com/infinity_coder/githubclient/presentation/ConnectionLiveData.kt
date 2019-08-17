package com.infinity_coder.githubclient.presentation

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkInfo
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData

class ConnectionLiveData(context: Context) :
    MutableLiveData<ConnectionLiveData.ServerConnectionType>() {

    enum class ServerConnectionType { ONLINE, OFFLINE }

    private var connectivityManager: ConnectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private val connectivityManagerCallback: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                postValue(ServerConnectionType.ONLINE)
            }

            override fun onLost(network: Network?) {
                postValue(ServerConnectionType.OFFLINE)
            }
        }

    override fun onActive() {
        super.onActive()

        updateConnection()
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(TRANSPORT_CELLULAR)
            .addTransportType(TRANSPORT_WIFI)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    private fun updateConnection() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork?.isConnected == true) {
            postValue(ServerConnectionType.ONLINE)
        }
    }
}