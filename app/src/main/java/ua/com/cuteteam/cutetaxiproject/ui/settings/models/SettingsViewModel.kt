package ua.com.cuteteam.cutetaxiproject.ui.settings.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper

class SettingsViewModel(
    private val appSettingsHelper: AppSettingsHelper
) : ViewModel() {

    private val _role = MutableLiveData<Int>(appSettingsHelper.role)

    val role: LiveData<Int> get() = _role

    fun setRole(role: Int) {
        _role.value = role
        appSettingsHelper.role = role
    }

}