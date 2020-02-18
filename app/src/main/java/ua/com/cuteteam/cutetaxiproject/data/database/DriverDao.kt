package ua.com.cuteteam.cutetaxiproject.data.database

import com.google.firebase.database.DatabaseReference
import ua.com.cuteteam.cutetaxiproject.data.User
import ua.com.cuteteam.cutetaxiproject.data.entities.Driver
import ua.com.cuteteam.cutetaxiproject.extentions.getValue

class DriverDao : BaseDao() {

    override val usersRef: DatabaseReference
        get() = rootRef.child("drivers")

    override suspend fun getUser(uid: String): User? {
        val userData = usersRef.child(uid).getValue()
        return userData.getValue(Driver::class.java)
    }

}