package ua.com.cuteteam.cutetaxiproject.data.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ua.com.cuteteam.cutetaxiproject.data.User
import ua.com.cuteteam.cutetaxiproject.data.entities.Trip

abstract class DbHelper {

    //Will be moved to dagger2 module
    private val database = Firebase.database


    protected val rootRef = database.reference.root
    protected val tripsRef = database.reference.root.child("trips")

    protected abstract val usersReference: DatabaseReference

    fun writeUser(user: User) {

        usersReference.child(user.uid).setValue(user)
    }

    fun getUser(uid: String): User {
        TODO()
    }


    // May be move to another class, if trips will be created by firebase functions
    fun writeTrip(trip: Trip) {
        TODO()
    }

    abstract fun getTrips(): List<Trip>

}