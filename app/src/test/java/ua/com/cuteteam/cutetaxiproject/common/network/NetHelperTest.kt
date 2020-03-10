package ua.com.cuteteam.cutetaxiproject.common.network

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ua.com.cuteteam.cutetaxiproject.getOrAwaitValue
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK])
@Deprecated("Rewrite NetHelper test when app will ready!!")
class NetHelperTest {

    private var context: Context? =  null
    private var netHelper: NetHelper? = null

    @Before
    fun setUp() {

        context = InstrumentationRegistry.getInstrumentation().targetContext
        netHelper = context?.let { NetHelper(it) }
        netHelper?.registerNetworkListener()
    }

    @After
    fun tearDown() {

        netHelper?.unregisterNetworkListener()
        context = null
        netHelper = null

    }

    @Test(expected = TimeoutException::class)
    fun getNetStatus() {
        val value = netHelper?.netStatus?.getOrAwaitValue()
        assertThat(value, CoreMatchers.nullValue())

    }

    @Test
    fun getHasMobileNetwork() {

        assertThat(netHelper?.hasMobileNetwork, CoreMatchers.`is`(true))

    }

    @Test
    fun getHasWiFi() {

        assertThat(netHelper?.hasWiFi, CoreMatchers.`is`(true))

    }

    @Test
    fun getHasInternet() {

        assertThat(netHelper?.hasInternet, CoreMatchers.`is`(true))

    }

}