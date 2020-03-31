package ua.com.cuteteam.cutetaxiproject.repositories

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.data.User
import ua.com.cuteteam.cutetaxiproject.data.database.BaseDao
import ua.com.cuteteam.cutetaxiproject.data.database.DriverDao
import ua.com.cuteteam.cutetaxiproject.data.database.PassengerDao
import ua.com.cuteteam.cutetaxiproject.data.entities.Driver
import ua.com.cuteteam.cutetaxiproject.data.entities.Passenger
import ua.com.cuteteam.cutetaxiproject.shPref.AppSettingsHelper

class StartUpRepository(context: Context = AppClass.appContext()) {
    private val passengerDAO = PassengerDao()
    private val driverDAO = DriverDao()

    private val appSettingsHelper = AppSettingsHelper(context)

    val isDriver: Boolean
        get() = appSettingsHelper.role

    suspend fun updateOrCreateUser(firebaseUser: FirebaseUser) {
        if (appSettingsHelper.role) {
            updateOrCreateUser(firebaseUser, driverDAO) {
                Driver(firebaseUser.displayName, firebaseUser.phoneNumber)
            }
        } else {
            updateOrCreateUser(firebaseUser, passengerDAO) {
                Passenger(firebaseUser.displayName, firebaseUser.phoneNumber)
            }
        }
    }

    private suspend fun <T : User> updateOrCreateUser(
        firebaseUser: FirebaseUser,
        dao: BaseDao,
        buildUser: () -> T
    ) {
        dao.getUser<T>(firebaseUser.uid)?.also {
            appSettingsHelper.initUser(it)
            return@updateOrCreateUser
        }
        val user = buildUser()
        dao.writeUser(user)
        appSettingsHelper.initUser(user)
    }
}
