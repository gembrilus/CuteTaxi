package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.preference.EditTextPreference
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.settings.*

private const val TAG = "CuteTaxi.AddInfoFrag"

class AdditionalInfoFragment() : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.additional_info_preferences

    private val pass_groups = arrayOf(
        IMPROVEMENTS_CATEGORY_KEY,
        ADDITIONAL_FACILITIES_CATEGORY_KEY
    )

    private val driver_groups = arrayOf(
        CAR_CATEGORY_KEY
    )

    val changedStore = listOf(
        CAR_CLASS_FOR_PASSENGER_KEY,
        FAVORITE_ADDRESSES_KEY,
        BLACK_LIST_OF_DRIVERS_KEY,
        CAR_CLASS_KEY,
        CAR_COLOR_KEY,
        CAR_MODEL_KEY,
        CAR_NUMBER_KEY
    )

    override fun setNewDataStore() {
        setDataStore(changedStore, appSettingsToFirebaseStore)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        model.role.observe(this, Observer {
            swapGroupsVisibility(it, pass_groups, driver_groups)
        })
    }
}