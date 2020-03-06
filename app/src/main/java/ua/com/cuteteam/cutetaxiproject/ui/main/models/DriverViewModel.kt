package ua.com.cuteteam.cutetaxiproject.ui.main.models

import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository

class DriverViewModel(
    private val repository: PassengerRepository
) : BaseViewModel(repository) {


}