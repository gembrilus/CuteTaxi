package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.text.InputType
import androidx.preference.EditTextPreference
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.settings.DELAY_KEY

class SystemFragment : BaseSettingsFragment() {
    override val resourceId: Int
        get() = R.xml.system_preferences

    override fun setup() {

        findPreference<EditTextPreference>(DELAY_KEY)?.setOnBindEditTextListener {
            it.setSingleLine()
            it.inputType = InputType.TYPE_CLASS_NUMBER
            it.selectAll()
        }

    }

}