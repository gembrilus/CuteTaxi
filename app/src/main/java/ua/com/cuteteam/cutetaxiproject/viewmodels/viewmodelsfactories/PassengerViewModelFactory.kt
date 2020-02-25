package ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel

@Suppress("UNCHECKED_CAST")
class PassengerViewModelFactory(private val passengerRepository: PassengerRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PassengerViewModel(passengerRepository) as T
    }
}
