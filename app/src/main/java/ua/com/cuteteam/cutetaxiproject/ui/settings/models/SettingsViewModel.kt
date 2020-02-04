package ua.com.cuteteam.cutetaxiproject.ui.settings.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.com.cuteteam.cutetaxiproject.settings.AppSettingsHelper

class SettingsViewModel(
    appSettingsHelper: AppSettingsHelper
) : ViewModel() {

    private val _role = MutableLiveData(0)
    val role: LiveData<Int> get() = _role

    init {
        setRole(appSettingsHelper.role)
    }

    fun setRole(role: Int) {
        _role.value = role
    }

}