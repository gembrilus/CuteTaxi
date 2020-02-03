package ua.com.cuteteam.cutetaxiproject.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.DriverSettingsViewModel
import ua.com.cuteteam.cutetaxiproject.ui.settings.models.PassengerSettingsViewModel


/**
 * Factory for creating ViewModels by type
 *
 * @param type is a type of ViewModel that is created:
 *  - SETTINGS_PASS => PassengerSettingsViewModel
 *  - SETTINGS_DRIVER => DriverSettingsViewModel
 *  - PASSENGER => PassengerViewModel
 *  - DRIVER => DriverViewModel
 */
class ViewModelFactory(private val type: ModelType) : ViewModelProvider.Factory{

    private val map get() = mapOf(
        ModelType.SETTINGS_PASS to { PassengerSettingsViewModel() },
        ModelType.SETTINGS_DRIVER to { DriverSettingsViewModel() }

    //  Add here pairs for enums PASSENGER and DRIVER

    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return (map[type] ?: error("With ${type} is there no an associated class of ViewModel")).invoke() as T
    }

}


/**
 * Enum class with types of ViewModel for an app CuteTaxi
 */
enum class ModelType {
    SETTINGS_PASS,
    SETTINGS_DRIVER,
    PASSENGER,
    DRIVER
}