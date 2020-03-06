package ua.com.cuteteam.cutetaxiproject.data.database

import com.google.firebase.database.DatabaseReference

class PassengerDao : BaseDao() {

    override val usersRef: DatabaseReference
        get() = rootRef.child("passengers")

}