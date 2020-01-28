package ua.com.cuteteam.cutetaxiproject.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.ui.settings.fragments.SettingsFragment

private const val TAG = "CuteTaxi.SettingsActivity"
private const val TITLE_TAG = "CuteTaxi.SettingsActivityTitle"

class SettingsActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        savedInstanceState
            ?.let {
                title = savedInstanceState.getCharSequence(TITLE_TAG)
            }
            ?: replaceFragment(SettingsFragment())

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                setTitle(R.string.title_activity_settings)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(TITLE_TAG, title)
    }

    override fun onSupportNavigateUp(): Boolean =
        supportFragmentManager.popBackStackImmediate() || super.onSupportNavigateUp()

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean = supportFragmentManager.fragmentFactory
        .instantiate(classLoader, pref.fragment)
        .apply {
            arguments = pref.extras
            setTargetFragment(caller, 0)
        }
        .run {
            replaceFragment(this, true)
            title = pref.title
            true
        }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings, fragment)
            .apply {
                if (addToBackStack) addToBackStack(null)
            }
            .commit()
    }

}
