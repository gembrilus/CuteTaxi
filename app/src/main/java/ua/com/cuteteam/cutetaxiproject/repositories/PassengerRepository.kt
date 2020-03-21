package ua.com.cuteteam.cutetaxiproject.repositories

import ua.com.cuteteam.cutetaxiproject.data.database.PassengerDao

class PassengerRepository : Repository() {
    val dao = PassengerDao()
}
