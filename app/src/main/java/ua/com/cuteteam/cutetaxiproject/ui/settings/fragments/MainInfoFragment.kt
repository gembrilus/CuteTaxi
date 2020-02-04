package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.view.inputmethod.EditorInfo
import androidx.preference.EditTextPreference
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.settings.NAME_KEY
import ua.com.cuteteam.cutetaxiproject.settings.PHONE_KEY

class MainInfoFragment : BaseSettingsFragment() {
    override val resourceId: Int
        get() = R.xml.main_info_preferences

    private val prefsForDatabase = listOf(
        NAME_KEY,
        PHONE_KEY
    )

    override fun onSetDataStore() {

        prefsForDatabase.forEach { setDataStore(it, appSettingsToFirebaseStore) }

        findPreference<EditTextPreference>(
            PHONE_KEY
        )?.setOnBindEditTextListener {
            it.setSingleLine()
            it.inputType = EditorInfo.TYPE_CLASS_PHONE
        }
    }
}