package ua.com.cuteteam.cutetaxiproject.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.data.User
import ua.com.cuteteam.cutetaxiproject.data.database.DriverDao
import ua.com.cuteteam.cutetaxiproject.data.database.PassengerDao
import ua.com.cuteteam.cutetaxiproject.data.entities.Passenger
import ua.com.cuteteam.cutetaxiproject.repositories.StartUpRepository
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper

class StartUpViewModel(private val repository: StartUpRepository) : ViewModel() {

    fun checkRole() = repository.checkRole()

    suspend fun updateOrCreateUser(firebaseUser: FirebaseUser) =
        repository.updateOrCreateUser(firebaseUser)
}
