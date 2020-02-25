package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import ua.com.cuteteam.cutetaxiproject.R

private const val TAG = "CuteTaxi.AddInfoFrag"

class AdditionalInfoFragment : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.additional_info_preferences

    private val pass_groups by lazy {
        arrayOf(
            spKeys.IMPROVEMENTS_CATEGORY_KEY,
            spKeys.ADDITIONAL_FACILITIES_CATEGORY_KEY
        )
    }

    private val driver_groups by lazy {
        arrayOf(
            spKeys.CAR_CATEGORY_KEY
        )
    }

    val changedStore by lazy {
        listOf(
            spKeys.PASSENGER_CAR_CLASS_KEY,
            spKeys.FAVORITE_ADDRESSES_KEY,
            spKeys.BLACK_LIST_DRIVERS_KEY,
            spKeys.CAR_CLASS_KEY,
            spKeys.CAR_COLOR_KEY,
            spKeys.CAR_BRAND_KEY,
            spKeys.CAR_MODEL_KEY,
            spKeys.CAR_NUMBER_KEY
        )
    }

    override fun setNewDataStore() {
        setDataStore(changedStore, appSettingsToFirebaseStore)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        model.role.observe(this, Observer {
            val currentRole = resources.getString(R.string.role_driver).toInt()
            swapGroupsVisibility(it == currentRole, pass_groups, driver_groups)
        })
    }
}