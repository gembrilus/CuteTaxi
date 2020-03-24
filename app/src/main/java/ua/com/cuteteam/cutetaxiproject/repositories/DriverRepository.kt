package ua.com.cuteteam.cutetaxiproject.repositories

import ua.com.cuteteam.cutetaxiproject.data.firebase_database.DriverDao

class DriverRepository: Repository() {
    val dao = DriverDao()
}
