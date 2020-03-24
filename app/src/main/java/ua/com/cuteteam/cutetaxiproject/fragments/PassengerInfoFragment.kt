package ua.com.cuteteam.cutetaxiproject.fragments

import androidx.preference.Preference
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.firebase_database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.firebase_database.PassengerDao
import ua.com.cuteteam.cutetaxiproject.preferences.ListBoxPreference
import ua.com.cuteteam.cutetaxiproject.preferences.ListBoxPreferenceDialogFragmentCompat
import ua.com.cuteteam.cutetaxiproject.shPref.FirebaseSettingsDataStore
import ua.com.cuteteam.cutetaxiproject.fragments.sum_providers.ComfortLevelSumProvider

private const val TAG = "CuteTaxi.PassInfoFrag"

class PassengerInfoFragment : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.passenger_info_preferences

    private val appSettingsToFirebaseStore by lazy {
        FirebaseSettingsDataStore(
            shPref,
            PassengerDao()
        )
    }

    override fun setNewDataStore() {
        findPreference<Preference>(DbEntries.Passengers.Fields.COMFORT_LEVEL)?.apply {
            preferenceDataStore = appSettingsToFirebaseStore
            summaryProvider = ComfortLevelSumProvider()
        }
        findPreference<Preference>(getString(R.string.key_black_list_preference))
            ?.preferenceDataStore = appSettingsToFirebaseStore
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        when(preference){
            is ListBoxPreference -> {
                ListBoxPreferenceDialogFragmentCompat.newInstance(preference.key).apply {
                    setTargetFragment(this@PassengerInfoFragment, 0)
                    targetFragment?.parentFragmentManager?.let {
                        show(it, "ua.com.cuteteam.cutetaxiproject.ListBoxPreference")
                    }
                }
            }
            else -> super.onDisplayPreferenceDialog(preference)
        }
    }
}