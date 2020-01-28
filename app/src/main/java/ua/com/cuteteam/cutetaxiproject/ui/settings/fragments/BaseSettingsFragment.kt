package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ua.com.cuteteam.cutetaxiproject.settings.AppSettingsStore
import ua.com.cuteteam.cutetaxiproject.settings.AppSettingsToFirebaseStore

const val SP_FILE = "CuteTaxi_Settings"

abstract class BaseSettingsFragment : PreferenceFragmentCompat() {

    abstract val resourceId: Int
    abstract fun setup()

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
    }

    protected val appSettingsStore by lazy {
        AppSettingsStore(sharedPreferences)
    }

    protected val appSettingsToFirebaseStore by lazy {
        AppSettingsToFirebaseStore(sharedPreferences)
    }

    final override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(resourceId, rootKey)
        preferenceManager.preferenceDataStore = appSettingsStore
        setup()
    }
}