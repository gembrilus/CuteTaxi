package ua.com.cuteteam.cutetaxiproject.shPref

import android.content.Context
import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AppSettingsHelperTest {


    private val editor: SharedPreferences.Editor =
        mock {
            on { putBoolean(any(), any()) } doReturn mock
            on { putInt(any(), any()) } doReturn mock
            on { putLong(any(), any()) } doReturn mock
            on { putFloat(any(), any()) } doReturn mock
            on { putString(any(), any()) } doReturn mock
            on { putStringSet(any(), any()) } doReturn mock
            on { remove(any()) } doReturn mock
            on { commit() } doReturn true
        }
    private val sharedPreferences: SharedPreferences =
        mock {
            on { edit() } doReturn editor
        }

    private val context: Context = mock()

    private val appSettingsHelper by lazy {
        AppSettingsHelper(context, sharedPreferences)
    }

    @Test
    fun isFirstStart() {

    }

    @Test
    fun getRole() {
    }

    @Test
    fun setRole() {
    }

    @Test
    fun getName() {
    }

    @Test
    fun setName() {
    }

    @Test
    fun getPhone() {
    }

    @Test
    fun setPhone() {
    }

    @Test
    fun getComfortClass() {
    }

    @Test
    fun setComfortClass() {
    }

    @Test
    fun getBlackListOfDrivers() {
    }

    @Test
    fun setBlackListOfDrivers() {
    }

    @Test
    fun getFavoriteAddresses() {
    }

    @Test
    fun setFavoriteAddresses() {
    }

    @Test
    fun getCarModel() {
    }

    @Test
    fun setCarModel() {
    }

    @Test
    fun getCarNumber() {
    }

    @Test
    fun setCarNumber() {
    }

    @Test
    fun getCarClass() {
    }

    @Test
    fun setCarClass() {
    }

    @Test
    fun getCarColor() {
    }

    @Test
    fun setCarColor() {
    }

    @Test
    fun isServiceEnabled() {
    }

    @Test
    fun setServiceEnabled() {
    }

    @Test
    fun getAppTheme() {
    }

    @Test
    fun setAppTheme() {
    }
}