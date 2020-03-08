package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import androidx.preference.Preference
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.database.DriverDao
import ua.com.cuteteam.cutetaxiproject.shPref.FirebaseSettingsDataStore

private const val TAG = "CuteTaxi.AddInfoFrag"

class DriverInfoFragment : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.driver_info_preferences

    private val appSettingsToFirebaseStore by lazy {
        FirebaseSettingsDataStore(
            shPref,
            DriverDao()
        )
    }

    override fun setNewDataStore() {
        findPreference<Preference>(DbEntries.Car.BRAND)
            ?.preferenceDataStore = appSettingsToFirebaseStore
        findPreference<Preference>(DbEntries.Car.MODEL)
            ?.preferenceDataStore = appSettingsToFirebaseStore
        findPreference<Preference>(DbEntries.Car.NUMBER)
            ?.preferenceDataStore = appSettingsToFirebaseStore
        findPreference<Preference>(DbEntries.Car.COLOR)
            ?.preferenceDataStore = appSettingsToFirebaseStore
        findPreference<Preference>(DbEntries.Car.COMFORT_LEVEL)
            ?.preferenceDataStore = appSettingsToFirebaseStore
    }
}