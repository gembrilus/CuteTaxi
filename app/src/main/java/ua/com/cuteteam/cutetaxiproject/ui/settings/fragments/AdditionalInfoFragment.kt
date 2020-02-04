package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.settings.ADDITIONAL_FACILITIES_CATEGORY_KEY
import ua.com.cuteteam.cutetaxiproject.settings.CAR_CATEGORY_KEY
import ua.com.cuteteam.cutetaxiproject.settings.IMPROVEMENTS_CATEGORY_KEY

class AdditionalInfoFragment() : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.additional_info_preferences

    val prefsForDatabase = listOf(
        IMPROVEMENTS_CATEGORY_KEY,
        ADDITIONAL_FACILITIES_CATEGORY_KEY,
        CAR_CATEGORY_KEY
    )

    override fun onSetDataStore() {

        prefsForDatabase.forEach { setDataStore(it, appSettingsToFirebaseStore) }

    }

}