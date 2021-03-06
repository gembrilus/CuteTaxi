package ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.com.cuteteam.cutetaxiproject.viewmodels.AuthViewModel

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel() as T
    }
}