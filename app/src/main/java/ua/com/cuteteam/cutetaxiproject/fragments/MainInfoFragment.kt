package ua.com.cuteteam.cutetaxiproject.fragments

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.firebase_database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.firebase_database.PassengerDao
import ua.com.cuteteam.cutetaxiproject.shPref.FirebaseSettingsDataStore

class MainInfoFragment : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.main_info_preferences

    private val appSettingsToFirebaseStore by lazy {
        FirebaseSettingsDataStore(
            shPref,
            PassengerDao()
        )
    }

    override fun setNewDataStore() {
        findPreference<Preference>(DbEntries.Passengers.Fields.NAME)
            ?.preferenceDataStore = appSettingsToFirebaseStore

        findPreference<Preference>(DbEntries.Passengers.Fields.PHONE)
            ?.preferenceDataStore = appSettingsToFirebaseStore
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        findPreference<EditTextPreference>(DbEntries.Passengers.Fields.PHONE)?.setOnBindEditTextListener {
            it.setSingleLine()
            it.inputType = EditorInfo.TYPE_CLASS_PHONE
        }

    }

}