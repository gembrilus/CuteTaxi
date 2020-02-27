package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import ua.com.cuteteam.cutetaxiproject.R

private const val TAG = "CuteTaxi.AddInfoFrag"

class AdditionalInfoFragment : BaseSettingsFragment() {

    override val resourceId: Int
        get() {
            val passRoleId = resources.getString(R.string.role_passenger).toInt()
            return if (role == passRoleId)
                R.xml.passenger_info_preferences
            else R.xml.driver_info_preferences
        }

    val changedStore by lazy {
        listOf(
            spKeys.PASSENGER_CAR_CLASS_KEY,
            spKeys.FAVORITE_ADDRESSES_KEY,
            spKeys.BLACK_LIST_DRIVERS_KEY
        )
    }

    override fun setNewDataStore() {
        setDataStore(changedStore, appSettingsToFirebaseStore)
    }
}