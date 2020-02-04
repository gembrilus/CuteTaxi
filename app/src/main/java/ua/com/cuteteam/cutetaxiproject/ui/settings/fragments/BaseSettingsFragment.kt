package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.*
import ua.com.cuteteam.cutetaxiproject.settings.*
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.SettingsViewModel
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.ViewModelFactory

abstract class BaseSettingsFragment :
    PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    abstract val resourceId: Int
    abstract fun setup()

    private val pass_groups
        get() = arrayOf(
            IMPROVEMENTS_CATEGORY_KEY,
            ADDITIONAL_FACILITIES_CATEGORY_KEY
        )

    private val driver_groups
        get() = arrayOf(
            CAR_CATEGORY_KEY
        )

    private lateinit var model: SettingsViewModel

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
    }

    private val appSettingsStore by lazy {
        AppSettingsStore().apply {
            setSharedPreferences(sharedPreferences)
        }
    }

    protected val appSettingsToFirebaseStore by lazy {
        AppSettingsToFirebaseStore().apply {
            val fbDbMock = FbDbMock()
            setPutFunction(fbDbMock::putValueToDb)
            setGetFunction(fbDbMock::getValueFromDb)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(
            this,
            ViewModelFactory(
                sharedPreferences
            )
        ).get(SettingsViewModel::class.java)

        model.role.observe(this, Observer {
            setRoleSettings(it)
        })
    }

    final override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(resourceId, rootKey)
        preferenceManager.preferenceDataStore = appSettingsStore
        setup()
    }

    private fun changeVisibility(vararg keys: String, visibility: Boolean) {
        keys.forEach {
            findPreference<Preference>(it)?.isVisible = visibility
        }
    }

    private fun setRoleSettings(role: Int) = when (role) {
        0 -> {
            changeVisibility(*driver_groups, visibility = false)
            changeVisibility(*pass_groups, visibility = true)
        }
        else -> {
            changeVisibility(*driver_groups, visibility = true)
            changeVisibility(*pass_groups, visibility = false)
        }
    }

    protected fun setDataStore(key: String, dataStore: PreferenceDataStore) {
        when (val pref = findPreference<Preference>(key)) {
            is PreferenceCategory -> {
                val count = pref.preferenceCount
                for (i in 0 until count) {
                    pref.getPreference(i).preferenceDataStore = dataStore
                }
            }
            is PreferenceScreen -> {
                val count = pref.preferenceCount
                for (i in 0 until count) {
                    pref.getPreference(i).preferenceDataStore = dataStore
                }
            }
            else -> {
                pref?.let {
                    it.preferenceDataStore = dataStore
                }
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == ROLE_KEY) {
            model.setRole(sharedPreferences?.getInt(ROLE_KEY, 0)!!)
        }
    }
}