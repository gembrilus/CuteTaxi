package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import ua.com.cuteteam.cutetaxiproject.R

private const val TAG = "CuteTaxi.PassInfoFrag"

class PassengerInfoFragment : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.passenger_info_preferences

    val changedStore by lazy {
        listOf(
            spKeys.FAVORITE_ADDRESSES_KEY,
            spKeys.BLACK_LIST_DRIVERS_KEY
        )
    }

    override fun setNewDataStore() {
        setDataStore(changedStore, appSettingsToFirebaseStore)
    }
}