package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.preference.*
import ua.com.cuteteam.cutetaxiproject.common.settings.AppSettingsToFirebaseStore
import ua.com.cuteteam.cutetaxiproject.common.settings.FbDbMock
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.SettingsViewModel
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.ViewModelFactory

private const val TAG = "CuteTaxi.BaseFragment"

abstract class BaseSettingsFragment :
    PreferenceFragmentCompat() {

    abstract val resourceId: Int

    abstract fun setNewDataStore()

    protected val model by lazy {
        ViewModelProvider(requireActivity(), ViewModelFactory(shPrefs))
            .get(SettingsViewModel::class.java)
    }

    private val shPrefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireActivity())
    }

    protected val appSettingsToFirebaseStore by lazy {
        AppSettingsToFirebaseStore(shPrefs, FbDbMock())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setNewDataStore()
        setPreferencesFromResource(resourceId, rootKey)
    }

    private fun changeVisibility(vararg keys: String, visibility: Boolean) {
        keys.forEach {
            findPreference<Preference>(it)?.isVisible = visibility
        }
    }

    protected fun swapGroupsVisibility(
        predicate: Boolean,
        groupVisible: Array<String>,
        groupInvisible: Array<String>
    ) = when (predicate) {

        false -> {
            changeVisibility(*groupVisible, visibility = true)
            changeVisibility(*groupInvisible, visibility = false)
        }
        true -> {
            changeVisibility(*groupInvisible, visibility = true)
            changeVisibility(*groupVisible, visibility = false)
        }
    }

    protected fun setDataStore(keys: List<String>, dataStore: PreferenceDataStore) {
        keys.forEach {
            val pref = findPreference<Preference>(it)
            Log.d(TAG, "Set DataStore for $it")
            pref?.preferenceDataStore = dataStore
        }
    }
}