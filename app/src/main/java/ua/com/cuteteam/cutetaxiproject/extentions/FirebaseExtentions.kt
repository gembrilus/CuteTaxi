package ua.com.cuteteam.cutetaxiproject.extentions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun DatabaseReference.getValue(): DataSnapshot {

    return suspendCoroutine { continuation ->
        addListenerForSingleValueEvent(FValueEventListener(
            onDataChange = { continuation.resume(it)},
            onError = { continuation.resumeWithException(it.toException()) }
        ))
    }
}

suspend fun DatabaseReference.exists() : Boolean {
    return this.getValue().exists()
}

class FValueEventListener(
    val onDataChange: (DataSnapshot) -> Unit,
    val onError: (DatabaseError) -> Unit
) :
    ValueEventListener {
    override fun onDataChange(data: DataSnapshot) = onDataChange.invoke(data)
    override fun onCancelled(error: DatabaseError) = onError.invoke(error)
}