package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

private const val TAG = "CuteTaxi.BaseFragment"

abstract class BaseSettingsFragment :
    PreferenceFragmentCompat() {

    abstract val resourceId: Int
    abstract fun setNewDataStore()

    protected val shPref by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setNewDataStore()
        setPreferencesFromResource(resourceId, rootKey)
    }

}