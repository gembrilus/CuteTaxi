package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.common.settings.DELAY_KEY

class SystemFragment : BaseSettingsFragment() {
    override val resourceId: Int
        get() = R.xml.system_preferences

    override fun setNewDataStore() {}

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        findPreference<EditTextPreference>(DELAY_KEY)?.setOnBindEditTextListener {
            it.setSingleLine()
            it.inputType = InputType.TYPE_CLASS_NUMBER
            it.selectAll()
        }
    }

}