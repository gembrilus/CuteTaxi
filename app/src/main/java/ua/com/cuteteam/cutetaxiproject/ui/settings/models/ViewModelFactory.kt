package ua.com.cuteteam.cutetaxiproject.ui.settings.models

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.common.settings.AppSettingsHelper

/**
 * Factory for creating ViewModels by type
 *
 * @param type is a type of ViewModel that is created:
 *
 */
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory{

    private val shPref = PreferenceManager.getDefaultSharedPreferences(context)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            AppSettingsHelper(context, shPref)
        ) as T
    }

}