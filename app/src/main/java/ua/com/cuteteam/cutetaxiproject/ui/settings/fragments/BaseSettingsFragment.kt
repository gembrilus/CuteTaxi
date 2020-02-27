package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceDataStore
import androidx.preference.PreferenceFragmentCompat
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.data.database.PassengerDao
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper
import ua.com.cuteteam.cutetaxiproject.shPref.FirebaseSettingsDataStore
import ua.com.cuteteam.cutetaxiproject.shPref.SPKeys

private const val TAG = "CuteTaxi.BaseFragment"

abstract class BaseSettingsFragment :
    PreferenceFragmentCompat() {

    abstract val resourceId: Int

    abstract fun setNewDataStore()

    protected val spKeys get() = SPKeys(requireContext())

    protected val role get() = AppSettingsHelper(AppClass.appContext()).role

    protected val appSettingsToFirebaseStore by lazy {
        FirebaseSettingsDataStore(requireContext(), PassengerDao())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setNewDataStore()
        setPreferencesFromResource(resourceId, rootKey)
    }

    protected fun setDataStore(keys: List<String>, dataStore: PreferenceDataStore) {
        keys.forEach {
            val pref = findPreference<Preference>(it)
            Log.d(TAG, "Set DataStore for $it")
            pref?.preferenceDataStore = dataStore
        }
    }
}