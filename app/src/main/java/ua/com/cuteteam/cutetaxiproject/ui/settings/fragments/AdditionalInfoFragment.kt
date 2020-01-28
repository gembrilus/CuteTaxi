package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import androidx.preference.EditTextPreference
import ua.com.cuteteam.cutetaxiproject.R

class AdditionalInfoFragment : BaseSettingsFragment(){
    override val resourceId: Int
        get() = R.xml.additional_info_preferences

    override fun setup() {

        findPreference<EditTextPreference>(
            getString(R.string.key_favorite_addresses_preference)
        )?.preferenceDataStore = appSettingsToFirebaseStore

    }

}