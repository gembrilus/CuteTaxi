package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import ua.com.cuteteam.cutetaxiproject.R

private const val TAG = "CuteTaxi.AddInfoFrag"

class DriverInfoFragment : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.driver_info_preferences

    val changedStore by lazy {
        listOf(
            spKeys.PASSENGER_CAR_CLASS_KEY
        )
    }

    override fun setNewDataStore() {
        setDataStore(changedStore, appSettingsToFirebaseStore)
    }
}