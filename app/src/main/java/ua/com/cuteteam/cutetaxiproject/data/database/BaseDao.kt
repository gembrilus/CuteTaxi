package ua.com.cuteteam.cutetaxiproject.data.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ua.com.cuteteam.cutetaxiproject.data.User
import ua.com.cuteteam.cutetaxiproject.data.entities.Trip
import ua.com.cuteteam.cutetaxiproject.extentions.exists
import ua.com.cuteteam.cutetaxiproject.extentions.getValue

abstract class BaseDao(
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
    database: FirebaseDatabase = Firebase.database
) {

    protected val authUser = auth.currentUser!!
    protected val rootRef = database.reference.root
    private val tripsRef = database.reference.root.child("trips")
    protected abstract val usersRef: DatabaseReference

    abstract suspend fun getUser(uid: String = authUser.uid): User?

    private val eventListeners = mutableMapOf<DatabaseReference, ValueEventListener>()

    fun writeUser(user: User) {
        usersRef.child(authUser.uid).setValue(user).addOnFailureListener {
            Log.e("Firebase: writeUser()", it.message.toString())
        }.addOnCompleteListener {
            Log.d("Firebase: writeUser()", "Write is successful")
        }
    }

    fun <T> writeField(entry: Entry, value: T, uid: String = authUser.uid) {
        usersRef.child(uid).child(entry.field).setValue(value).addOnFailureListener {
            Log.d("Firebase: writeField()", it.message.toString())
        }.addOnCompleteListener {
            Log.d("Firebase: writeField()", "Write is successful")
        }
    }


    suspend fun <T> getField(entry: Entry, uid: String = authUser.uid): T? {
        val fieldData = usersRef.child(uid).child(entry.field).getValue()
        @Suppress("UNCHECKED_CAST")
        return fieldData.value as T
    }

    suspend fun isUserExist(uid: String = authUser.uid): Boolean {
        return usersRef.child(uid).exists()
    }

    suspend fun isFieldExist(entry: Entry, uid: String = authUser.uid): Boolean {
        return usersRef.child(uid).child(entry.field).exists()
    }

    fun subscribeForChanges(
        entry: Entry,
        listener: ValueEventListener,
        uid: String = authUser.uid
    ) {
        val childRef = usersRef.child(uid).child(entry.field)
        if (!eventListeners.contains(childRef)) {
            childRef.addValueEventListener(listener)
            eventListeners.put(childRef, listener)
            Log.d("Realtime database", "Set to listen for $childRef")
        } else {
            Log.e("Database Error", "Listener $childRef is already set")
        }
    }

    fun removeAllListeners() {
        for (item in eventListeners) {
            item.key.removeEventListener(item.value)
        }
        eventListeners.clear()
    }

    fun removeListeners(entry: Entry, uid: String = authUser.uid) {
        removeListeners(usersRef.child(uid).child(entry.field))
    }

    private fun removeListeners(reference: DatabaseReference) {
        for (item in eventListeners) {
            if (item.key == reference) {
                Log.d("Realtime Database", "Unsubscribe from ${item.key}")
                item.key.removeEventListener(item.value)
                eventListeners.remove(item.key)
            }
        }
    }

    // Temporary function for testing table of trips, it will be moved to Firebase functions
    fun createTrip(trip: Trip) {
        tripsRef.push().setValue(trip)
    }

/*    fun getTrips(): List<Trip> {
        TODO()
    }*/
}