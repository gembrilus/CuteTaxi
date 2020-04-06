package ua.com.cuteteam.cutetaxiproject.fragments.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

private const val TAG = "CuteTaxi.BaseFragment"

abstract class BaseSettingsFragment :
    PreferenceFragmentCompat() {

    abstract val resourceId: Int

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(resourceId, rootKey)
    }

}