package ua.com.cuteteam.cutetaxiproject.settings

import android.util.Log


@Deprecated(
    message = "It's a MOCK only",
    replaceWith = ReplaceWith("real instance of database")
)
class FbDbMock: AppSettingsToFirebaseStore.FirebaseStore {

    private val db = mutableMapOf<String, MutableList<String>>()

    private val TAG = "CuteTaxi.FbDbMock"
    private val MESSAGE = "It is only a MOCK!"

    override fun <T> putToFirebase(key: String, value: T) {
        Log.d(TAG, "${MESSAGE} Put a value to database!")
        db[key]?.let { list ->
            list.clear()
            list.add(value as String)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getFromFirebase(key: String): T {
        return if (db.containsKey(key)){
            Log.d(TAG, "${MESSAGE} Get a value from database!")
            db.getValue(key) as T
        } else "" as T
    }

}