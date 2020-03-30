package ua.com.cuteteam.cutetaxiproject.repositories

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.data.User
import ua.com.cuteteam.cutetaxiproject.data.database.DriverDao
import ua.com.cuteteam.cutetaxiproject.data.database.PassengerDao
import ua.com.cuteteam.cutetaxiproject.data.entities.Driver
import ua.com.cuteteam.cutetaxiproject.data.entities.Passenger
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper

class StartUpRepository(context: Context = AppClass.appContext()) {
    private val passengerDAO = PassengerDao()
    private val driverDAO = DriverDao()

    private val appSettingsHelper = AppSettingsHelper(context)

    enum class UserRole {
        DRIVER,
        PASSENGER
    }

    fun checkRole(): UserRole {
        return if (appSettingsHelper.role) UserRole.DRIVER
        else UserRole.PASSENGER
    }

    suspend fun updateOrCreateUser(firebaseUser: FirebaseUser) {
        if (appSettingsHelper.role) updateOrCreateDriver(firebaseUser)
        else updateOrCreatePassenger(firebaseUser)
    }



    private suspend fun updateOrCreateDriver(firebaseUser: FirebaseUser) {
        checkDriverExistence(firebaseUser.uid)?.also {
            appSettingsHelper.initUser(it)
            return@updateOrCreateDriver
        }
        val driver = Driver(
            firebaseUser.displayName,
            firebaseUser.phoneNumber
        )
        createDriver(firebaseUser.uid, driver)
        appSettingsHelper.initUser(driver)
    }

    private suspend fun updateOrCreatePassenger(firebaseUser: FirebaseUser) {
        checkPassengerExistence(firebaseUser.uid)?.also {
            appSettingsHelper.initUser(it)
            return@updateOrCreatePassenger
        }
        val passenger = Passenger(
            firebaseUser.displayName,
            firebaseUser.phoneNumber
        )
        createPassenger(firebaseUser.uid, passenger)
        appSettingsHelper.initUser(passenger)
    }

    private fun createDriver(id: String, driver: Driver) = driverDAO.writeUser(driver)

    private fun createPassenger(id: String, passenger: Passenger) = passengerDAO.writeUser(passenger)

    private suspend fun getUser(firebaseUser: FirebaseUser): User? {
        val uid = firebaseUser.uid
        return checkDriverExistence(uid) ?: checkPassengerExistence(uid)
    }

    private suspend fun checkDriverExistence(uid: String): User? = driverDAO.getUser(uid)

    private suspend fun checkPassengerExistence(uid: String): User? = passengerDAO.getUser(uid)
}
