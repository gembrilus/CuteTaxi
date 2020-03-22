package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import ua.com.cuteteam.cutetaxiproject.R

private const val TAG = "CuteTaxi.HeaderFragment"

class HeaderFragment : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.header_preferences

    override fun setNewDataStore() {}
}