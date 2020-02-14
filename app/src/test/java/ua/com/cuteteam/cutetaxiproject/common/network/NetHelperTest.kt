package ua.com.cuteteam.cutetaxiproject.common.network

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NetHelperTest {

    private var context: Context? =  null
    private var netHelper: NetHelper? = null

    @Before
    fun setUp() {

        context = ApplicationProvider.getApplicationContext()
        netHelper = context?.let { NetHelper(it) }
        netHelper?.registerNetworkListener()
    }

    @After
    fun tearDown() {

        netHelper?.unregisterNetworkListener()
        context = null
        netHelper = null

    }

    @Test
    fun getNetStatus() {



    }

    @Test
    fun getHasMobileNetwork() {
    }

    @Test
    fun getHasWiFi() {
    }

    @Test
    fun getHasInternet() {
    }

    @Test
    fun registerNetworkListener() {
    }

    @Test
    fun unregisterNetworkListener() {
    }
}