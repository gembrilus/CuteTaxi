package ua.com.cuteteam.cutetaxiproject.data.database

import com.google.firebase.database.DatabaseReference

class DriverDao : BaseDao() {

    override val usersRef: DatabaseReference
        get() = rootRef.child("drivers")

}