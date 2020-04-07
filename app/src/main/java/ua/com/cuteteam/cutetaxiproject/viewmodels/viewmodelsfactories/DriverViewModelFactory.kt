package ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.DriverViewModel

@Suppress("UNCHECKED_CAST")
class DriverViewModelFactory(private val repo: DriverRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = DriverViewModel(repo) as T
}