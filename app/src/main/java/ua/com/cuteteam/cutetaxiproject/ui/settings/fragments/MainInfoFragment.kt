package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.preference.EditTextPreference
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.shPref.NAME_KEY
import ua.com.cuteteam.cutetaxiproject.shPref.PHONE_KEY

class MainInfoFragment : BaseSettingsFragment() {
    override val resourceId: Int
        get() = R.xml.main_info_preferences

    private val changedStore = listOf(
        NAME_KEY,
        PHONE_KEY
    )

    override fun setNewDataStore() {
        setDataStore(changedStore, appSettingsToFirebaseStore)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        findPreference<EditTextPreference>(PHONE_KEY)?.setOnBindEditTextListener {
            it.setSingleLine()
            it.inputType = EditorInfo.TYPE_CLASS_PHONE
        }

    }

}