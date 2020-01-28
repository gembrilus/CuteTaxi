package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import androidx.preference.EditTextPreference
import ua.com.cuteteam.cutetaxiproject.R

class MainInfoFragment : BaseSettingsFragment() {
    override val resourceId: Int
        get() = R.xml.main_info_preferences

    override fun setup() {

        findPreference<EditTextPreference>(
            getString(R.string.key_user_name_preference)
        )?.preferenceDataStore = appSettingsToFirebaseStore

        findPreference<EditTextPreference>(
            getString(R.string.key_user_phone_number_preference)
        )?.preferenceDataStore = appSettingsToFirebaseStore
    }

}