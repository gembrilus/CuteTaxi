package ua.com.cuteteam.cutetaxiproject.helpers.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkInfo
import android.os.Build
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@Suppress("DEPRECATION")
@RunWith(MockitoJUnitRunner.Silent::class)
class NetHelperTest {

    private lateinit var context: Context
    private lateinit var netHelper: NetHelper
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private lateinit var network: Network

    @Before
    fun setUp() {

        network = mock()

        val netInfo: NetworkInfo = mock{
            on { isConnected } doReturn true
        }

        val networkCapabilities: NetworkCapabilities = mock {
            on { hasTransport(TRANSPORT_CELLULAR) } doReturn false
            on { hasTransport(TRANSPORT_WIFI) } doReturn true
        }

        networkCallback = mock()

        connectivityManager = mock{
            on { activeNetworkInfo } doReturn netInfo
            on { getNetworkCapabilities(activeNetwork) } doReturn networkCapabilities
        }

        context = mock{
            on { getSystemService(Context.CONNECTIVITY_SERVICE) } doReturn connectivityManager
        }

        netHelper = NetHelper(context)
    }


    @Test
    fun getHasMobileNetwork() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            assertEquals(netHelper.hasMobileNetwork, false)
        } else {
            assertEquals(netHelper.hasMobileNetwork, true)
        }

    }

    @Test
    fun getHasWiFi() {

        assertEquals(netHelper.hasWiFi, true)

    }

    @Test
    fun getHasInternet() {

        assertEquals(netHelper.hasInternet, true)

    }

}