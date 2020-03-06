package ua.com.cuteteam.cutetaxiproject.data.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ua.com.cuteteam.cutetaxiproject.data.User
import ua.com.cuteteam.cutetaxiproject.data.entities.Driver
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.Trip
import ua.com.cuteteam.cutetaxiproject.extentions.exists
import ua.com.cuteteam.cutetaxiproject.extentions.getValue

abstract class BaseDao(
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
    database: FirebaseDatabase = Firebase.database
) {

    protected val authUser = auth.currentUser!!
    protected val rootRef = database.reference.root
    protected abstract val usersRef: DatabaseReference

    private val eventListeners = mutableMapOf<DatabaseReference, ValueEventListener>()

    @Suppress("UNCHECKED_CAST")
    suspend fun <T: User> getUser(uid: String): T? {
        val userData = usersRef.child(uid).getValue()
        return userData.getValue(Driver::class.java) as T?
    }

    /**Writes user to realtime database
     * @see User
     */
    fun writeUser(user: User) {
        usersRef.child(authUser.uid).setValue(user).addOnFailureListener {
            Log.e("Firebase: writeUser()", it.message.toString())
        }.addOnCompleteListener {
            Log.d("Firebase: writeUser()", "Write is successful")
        }
    }

    /**Writes value into user field, specified by entry
     * @param entry field entry in enum class
     * @see entry
     */
    fun <T> writeField(entry: Entry, value: T, uid: String = authUser.uid) {
        usersRef.child(uid).child(entry.field).setValue(value).addOnFailureListener {
            Log.d("Firebase: writeField()", it.message.toString())
        }.addOnCompleteListener {
            Log.d("Firebase: writeField()", "Write is successful")
        }
    }


    /** Updates user fields. Function doesn't check database structure, use carefully.
     *  @param map map of path-value pairs
     */
    fun writeField(map: HashMap<String, Any>) {
        usersRef.child(authUser.uid).updateChildren(map)
    }


    /** Updates user field.
     * Function doesn't check database structure, use carefully.
     * @param path  path to field in user entry
     * @param value value for writing into specified path
     */
    fun <T> writeField(path: String, value: T) {
        val childUpdate = HashMap<String, Any>()
        childUpdate[path] = value as Any
        usersRef.child(authUser.uid).updateChildren(childUpdate)
    }


    /** Returns value from specified field
     * @param entry field entry in enum class
     * @see Entry
     * @return value or null if field doesn't exist
     */
    suspend fun <T> getField(entry: Entry, uid: String = authUser.uid): T? {
        val fieldData = usersRef.child(uid).child(entry.field).getValue()
        @Suppress("UNCHECKED_CAST")
        return fieldData.value as T
    }


    /** Returns value from specified field
     * @param path  path to field in user entry
     * @return value or null if field doesn't exist
     */
    suspend fun <T> getField(path: String): T {
        var reference = usersRef.child(authUser.uid)
        val children = path.split("/")
        for (child in children) {
            reference = reference.child(child)
            Log.d("GetField", "new reference is $reference")
        }

        @Suppress("UNCHECKED_CAST")
        return reference.getValue().value as T
    }


    /** Checks if user exist in database table
     * @param uid user uid, by default uid from firebase authentication
     * @return true if user exist, else false
     */
    suspend fun isUserExist(uid: String = authUser.uid): Boolean {
        return usersRef.child(uid).exists()
    }


    /** Checks if field exist in user entry
     * @param entry field entry in enum class
     * @see Entry
     * @return true if field exist in database, else false
     */
    suspend fun isFieldExist(entry: Entry, uid: String = authUser.uid): Boolean {
        return usersRef.child(uid).child(entry.field).exists()
    }


    /** Subscribes for value changes. Updates receives using callbacks in ValueEvenListener.
     * If updates aren't necessary anymore, don't forget to remove callbacks using
     * removeListeners() or removeAllListeners()
     * @param entry field entry in enum class
     * @see Entry
     * @see ValueEventListener
     * @see removeAllListeners
     * @see removeListeners
     */
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

    fun writeOrder(order: Order?): DatabaseReference {
        val ref = rootRef.child(DbEntries.Orders.TABLE).push()
        order?.let {
            ref.setValue(it)
        }
        return ref
    }

    @Suppress("UNCHECKED_CAST")
    fun observeOrders(onSuccess: (List<Order>) -> Unit){
        rootRef.child(DbEntries.Orders.TABLE)
            .orderByChild(DbEntries.Orders.Fields.ORDER_STATUS)
            .equalTo(OrderStatus.NEW.name, DbEntries.Orders.Fields.ORDER_STATUS)
            .orderByChild(DbEntries.Orders.Fields.START_ADDRESS)
            .ref
            .child(DbEntries.Orders.Fields.START_ADDRESS)
            .child(DbEntries.Address.LOCATION)
            .startAt(DbEntries.Address.LOCATION)
            .endAt(DbEntries.Address.LOCATION)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("CuteDAO", p0.message)
                }
                override fun onDataChange(p0: DataSnapshot) {
                    val result = p0.getValue(List::class.java) as List<Order>
                    onSuccess.invoke(result)
                }
            })
    }

    /** Removes all active listeners
     * @see ValueEventListener
     * @see subscribeForChanges
     */
    fun removeAllListeners() {
        for (item in eventListeners) {
            item.key.removeEventListener(item.value)
        }
        eventListeners.clear()
    }

    /** Removes listener, specified by entry
     * @param entry
     * @see ValueEventListener
     * @see subscribeForChanges
     * @see Entry
     */
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

}