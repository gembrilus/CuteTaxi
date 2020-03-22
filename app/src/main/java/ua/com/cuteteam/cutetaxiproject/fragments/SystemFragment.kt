package ua.com.cuteteam.cutetaxiproject.fragments

import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.fragments.BaseSettingsFragment

class SystemFragment : BaseSettingsFragment() {
    override val resourceId: Int
        get() = R.xml.system_preferences

    override fun setNewDataStore() {}

}