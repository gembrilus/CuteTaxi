package ua.com.cuteteam.cutetaxiproject.ui.settings.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceFragmentCompat
import ua.com.cuteteam.cutetaxiproject.settings.AppSettingsHelper
import ua.com.cuteteam.cutetaxiproject.settings.AppSettingsStore
import ua.com.cuteteam.cutetaxiproject.settings.AppSettingsToFirebaseStore
import ua.com.cuteteam.cutetaxiproject.settings.FbDbMock
import ua.com.cuteteam.cutetaxiproject.ui.settings.SettingsViewModel

const val SP_FILE = "CuteTaxi_Settings"

abstract class BaseSettingsFragment :
    PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    abstract val resourceId: Int
    abstract fun setup()

    private lateinit var model: SettingsViewModel

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
    }

    private val appSettingsHelper by lazy { AppSettingsHelper(sharedPreferences) }

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


    private val callback = object : SettingsViewModel.CuteTaxiSettingsCallback {
        override fun <T> observe(vararg value: LiveData<T>) {

            TODO("Observe any value with this method")



        }

    }

    private fun setModel(role: Int) {
        model = SettingsViewModel.getViewModel(this, role)
    }

    private fun subscribe(){
        model.setup(callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = SettingsViewModel.getViewModel(this, appSettingsHelper.role)
    }

    final override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(resourceId, rootKey)
        preferenceManager.preferenceDataStore = appSettingsStore
        setup()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "role"){
            setModel(sharedPreferences?.getInt("role", 0)!!)
        }
    }
}