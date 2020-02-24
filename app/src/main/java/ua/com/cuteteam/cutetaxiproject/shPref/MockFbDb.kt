package ua.com.cuteteam.cutetaxiproject.shPref

import com.google.firebase.database.DatabaseReference
import ua.com.cuteteam.cutetaxiproject.data.User
import ua.com.cuteteam.cutetaxiproject.data.database.BaseDao
import ua.com.cuteteam.cutetaxiproject.data.entities.Passenger

class MockFbDb : BaseDao(){
    override val usersRef: DatabaseReference
        get() = rootRef.child("passengers")

    override suspend fun getUser(uid: String): User? {
        return Passenger()
    }

    fun <T> getField(key: String?, path: String): T? {
        return null
    }

    fun <T> putField(key: String?, path: String) {

    }

}