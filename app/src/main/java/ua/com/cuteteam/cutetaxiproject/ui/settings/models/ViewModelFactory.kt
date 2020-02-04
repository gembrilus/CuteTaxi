package ua.com.cuteteam.cutetaxiproject.ui.settings.models

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.com.cuteteam.cutetaxiproject.settings.AppSettingsHelper

/**
 * Factory for creating ViewModels by type
 *
 * @param type is a type of ViewModel that is created:
 *
 */
class ViewModelFactory(private val shPref: SharedPreferences) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            AppSettingsHelper(shPref)
        ) as T
    }

}