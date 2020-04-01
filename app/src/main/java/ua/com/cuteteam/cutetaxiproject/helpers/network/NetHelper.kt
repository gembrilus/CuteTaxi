package ua.com.cuteteam.cutetaxiproject.helpers.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.MutableLiveData

private const val TAG = "CuteTaxi.NetHelper"

/**
 * Class for network checking and a network status observing
 *
 * @param context It needs a context of app-level. In other case could be weak leaks
 *
 */
class NetHelper(context: Context) {

    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkRequest by lazy {
        NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
    }

    private val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onLost(network: Network) {
            if (hasWiFi || hasMobileNetwork) return
            netStatus.postValue(NetStatus.LOST)
        }

        override fun onUnavailable() {
            netStatus.postValue(NetStatus.UNAVAILABLE)
        }

        override fun onAvailable(network: Network) {
            netStatus.postValue(NetStatus.AVAILABLE)
        }
    }


    /**
     * Property for observing network status. It is LiveData!
     * You can to subscribe and receive statuses where is your connection lost
     * or available.
     * You can use [netStatus] in your ViewModel
     *
     */
    val netStatus = MutableLiveData<NetStatus>()


    /**
     * Boolen property for checking a mobile network.
     * <b>true</b> - if a network available, else <b>false</b>
     */
    val hasMobileNetwork
        get() = hasNetwork {
            hasTransport(TRANSPORT_CELLULAR)
        }


    /**
     * Boolen property for checking a wifi network.
     * <b>true</b> - if a network available, else <b>false</b>
     */
    val hasWiFi
        get() = hasNetwork {
            hasTransport(TRANSPORT_WIFI)
        }


    /**
     * Boolen property for checking both wifi and mobile network.
     * <b>true</b> - if a network available, else <b>false</b>
     */
    val hasInternet get() = hasMobileNetwork || hasWiFi


    /**
     * You can to register a listener whenever (in ViewModel, Activity or Fragment)
     * for listening a status of the network connection.
     * Don't forget to invoke a [unregisterNetworkListener]
     *
     */
    fun registerNetworkListener() {
        connectivityManager.registerNetworkCallback(
            networkRequest,
            callback
        )
    }


    /**
     * You must to remove all registered listeners when a lifecycle of class is destroyed
     *
     */
    fun unregisterNetworkListener() {
        connectivityManager.unregisterNetworkCallback(callback)
    }


    @Suppress("DEPRECATION")
    private fun hasNetwork(predicate: NetworkCapabilities.() -> Boolean): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectivityManager
                .getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.let { predicate.invoke(it) }
                ?: false
        } else
            connectivityManager
                .activeNetworkInfo
                .run { this != null && isConnected }
}
