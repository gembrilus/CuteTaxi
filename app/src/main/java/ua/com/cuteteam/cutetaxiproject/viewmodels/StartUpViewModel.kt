package ua.com.cuteteam.cutetaxiproject.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.data.User
import ua.com.cuteteam.cutetaxiproject.data.database.DriverDao
import ua.com.cuteteam.cutetaxiproject.data.database.PassengerDao
import ua.com.cuteteam.cutetaxiproject.data.entities.Passenger
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper

class StartUpViewModel(context: Context = AppClass.appContext()) : ViewModel() {

    private val passengerDAO = PassengerDao()
    private val driverDAO = DriverDao()

    val appSettingsHelper = AppSettingsHelper(context)

    suspend fun initSettings(firebaseUser: FirebaseUser) {
        when (val user = checkUserExistence(firebaseUser)) {
            is User -> appSettingsHelper.initUser(user)
            else -> {
                val passenger = Passenger(
                    firebaseUser.displayName,
                    firebaseUser.phoneNumber
                )
                appSettingsHelper.initUser(passenger)
                passengerDAO.writeUser(passenger)
            }
        }
    }

    private suspend fun checkUserExistence(firebaseUser: FirebaseUser): User? {
        val uid = firebaseUser.uid
        return when {
            driverDAO.isUserExist(uid) -> driverDAO.getUser(uid)
            passengerDAO.isUserExist(uid) -> passengerDAO.getUser(uid)
            else -> null
        }
    }
}