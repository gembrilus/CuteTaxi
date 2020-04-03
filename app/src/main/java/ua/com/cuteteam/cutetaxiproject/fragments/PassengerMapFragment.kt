package ua.com.cuteteam.cutetaxiproject.fragments

import androidx.lifecycle.ViewModelProvider
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel
import ua.com.cuteteam.cutetaxiproject.viewmodels.viewmodelsfactories.PassengerViewModelFactory

class PassengerMapFragment : MapsFragment() {
    override val viewModel: PassengerViewModel
        get() = ViewModelProvider(
            activity!!, PassengerViewModelFactory(PassengerRepository())
        ).get(PassengerViewModel::class.java)
}