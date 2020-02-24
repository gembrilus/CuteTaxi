package ua.com.cuteteam.cutetaxiproject.ui.settings.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper

class SettingsViewModel(
    appSettingsHelper: AppSettingsHelper
) : ViewModel() {

    private val _role = MutableLiveData<Boolean>().apply {
        value = appSettingsHelper.role
    }
    val role: LiveData<Boolean> get() = _role

    fun setRole(role: Boolean) {
        _role.value = role
    }

}