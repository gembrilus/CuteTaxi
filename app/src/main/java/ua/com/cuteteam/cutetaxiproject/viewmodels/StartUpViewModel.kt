package ua.com.cuteteam.cutetaxiproject.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ua.com.cuteteam.cutetaxiproject.repositories.StartUpRepository

class StartUpViewModel(private val repository: StartUpRepository) : ViewModel() {

    fun isDriver() = repository.isDriver

    suspend fun updateOrCreateUser(firebaseUser: FirebaseUser) =
        repository.updateOrCreateUser(firebaseUser)
}
