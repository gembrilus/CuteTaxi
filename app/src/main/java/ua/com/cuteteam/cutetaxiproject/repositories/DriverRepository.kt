package ua.com.cuteteam.cutetaxiproject.repositories

import ua.com.cuteteam.cutetaxiproject.data.database.DriverDao

class DriverRepository: Repository() {
    val dao = DriverDao()
}
