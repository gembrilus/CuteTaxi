package ua.com.cuteteam.cutetaxiproject.fragments.settings

import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.fragments.settings.BaseSettingsFragment

private const val TAG = "CuteTaxi.AddInfoFrag"

class DriverInfoFragment : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.driver_info_preferences
}