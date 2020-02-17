package ua.com.cuteteam.cutetaxiproject.data.database

import com.google.firebase.database.DatabaseReference
import ua.com.cuteteam.cutetaxiproject.data.entities.Trip

class DriverDbHelper : DbHelper() {

    override val usersReference: DatabaseReference
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getTrips(): List<Trip> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}