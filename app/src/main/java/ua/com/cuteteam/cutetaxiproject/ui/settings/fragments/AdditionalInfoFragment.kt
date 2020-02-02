package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.preferences.ListBoxPreference

class AdditionalInfoFragment : BaseSettingsFragment() {
    override val resourceId: Int
        get() = R.xml.additional_info_preferences

    override fun setup() {

        findPreference<ListBoxPreference>(
            getString(R.string.key_favorite_addresses_preference)
        )?.apply {
            preferenceDataStore = appSettingsToFirebaseStore
        }
    }
}