package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.preference.EditTextPreference
import ua.com.cuteteam.cutetaxiproject.R

class MainInfoFragment : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.main_info_preferences

    private val changedStore by lazy {
        listOf(
            spKeys.NAME_KEY,
            spKeys.PHONE_KEY
        )
    }

    override fun setNewDataStore() {
        setDataStore(changedStore, appSettingsToFirebaseStore)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        findPreference<EditTextPreference>(spKeys.PHONE_KEY)?.setOnBindEditTextListener {
            it.setSingleLine()
            it.inputType = EditorInfo.TYPE_CLASS_PHONE
        }

    }

}