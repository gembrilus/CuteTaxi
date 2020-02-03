package ua.com.cuteteam.cutetaxiproject.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import ua.com.cuteteam.cutetaxiproject.ui.ModelType
import ua.com.cuteteam.cutetaxiproject.ui.ViewModelFactory
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.DriverSettingsViewModel
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.PassengerSettingsViewModel

abstract class SettingsViewModel : ViewModel() {

    abstract fun setup(callback: CuteTaxiSettingsCallback)

    companion object {

        fun getViewModel(owner: ViewModelStoreOwner, role: Int): SettingsViewModel = when (role) {
            0 -> ViewModelProvider(owner,
                ViewModelFactory(
                    ModelType.SETTINGS_PASS
                )
            ).get(PassengerSettingsViewModel::class.java)
            else -> ViewModelProvider(owner,
                ViewModelFactory(
                    ModelType.SETTINGS_DRIVER
                )
            ).get(DriverSettingsViewModel::class.java)
        }

    }

    interface CuteTaxiSettingsCallback{
        fun <T> observe(vararg value: LiveData<T>)
    }


}