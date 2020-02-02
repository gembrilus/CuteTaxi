package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ua.com.cuteteam.cutetaxiproject.settings.AppSettingsStore
import ua.com.cuteteam.cutetaxiproject.settings.AppSettingsToFirebaseStore
import ua.com.cuteteam.cutetaxiproject.settings.FbDbMock

const val SP_FILE = "CuteTaxi_Settings"

abstract class BaseSettingsFragment : PreferenceFragmentCompat() {

    abstract val resourceId: Int
    abstract fun setup()

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
    }

    private val appSettingsStore by lazy {
        AppSettingsStore().apply {
            setSharedPreferences(sharedPreferences)
        }
    }

    protected val appSettingsToFirebaseStore by lazy {
        AppSettingsToFirebaseStore().apply {
            val fbDbMock = FbDbMock()
            setPutFunction(fbDbMock::putValueToDb)
            setGetFunction(fbDbMock::getValueFromDb)
        }
    }

    final override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(resourceId, rootKey)
        preferenceManager.preferenceDataStore = appSettingsStore
        setup()
    }
}