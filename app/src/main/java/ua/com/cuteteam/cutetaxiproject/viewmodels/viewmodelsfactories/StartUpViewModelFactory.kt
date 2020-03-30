package ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.com.cuteteam.cutetaxiproject.repositories.StartUpRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.StartUpViewModel

@Suppress("UNCHECKED_CAST")
class StartUpViewModelFactory: ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StartUpViewModel(StartUpRepository()) as T
    }
}