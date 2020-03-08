package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import androidx.preference.Preference
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.database.PassengerDao
import ua.com.cuteteam.cutetaxiproject.shPref.FirebaseSettingsDataStore

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
        findPreference<Preference>(DbEntries.Passengers.Fields.COMFORT_LEVEL)
            ?.preferenceDataStore = appSettingsToFirebaseStore
        findPreference<Preference>(DbEntries.Passengers.Fields.FAVORITE_ADDRESSES)
            ?.preferenceDataStore = appSettingsToFirebaseStore
    }
}