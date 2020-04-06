package ua.com.cuteteam.cutetaxiproject.fragments.settings

import androidx.preference.Preference
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.preferences.ListBoxPreference
import ua.com.cuteteam.cutetaxiproject.preferences.ListBoxPreferenceDialogFragmentCompat

private const val TAG = "CuteTaxi.PassInfoFrag"
private const val LIST_BOX_TAG = "ua.com.cuteteam.cutetaxiproject.ListBoxPreference"

class PassengerInfoFragment : BaseSettingsFragment() {

    override val resourceId: Int
        get() = R.xml.passenger_info_preferences

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        when (preference) {
            is ListBoxPreference -> {
                ListBoxPreferenceDialogFragmentCompat.newInstance(preference.key).apply {
                    setTargetFragment(this@PassengerInfoFragment, 0)
                    targetFragment?.parentFragmentManager?.let {
                        show(it, LIST_BOX_TAG)
                    }
                }
            }
            else -> super.onDisplayPreferenceDialog(preference)
        }
    }
}