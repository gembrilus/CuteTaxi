package ua.com.cuteteam.cutetaxiproject.settings

import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val ERROR_MESSAGE_PUT = "You need to set a 'put' function first. " +
        "Use a public method 'setPutFunction()'"

private const val ERROR_MESSAGE_GET = "You need to set a 'get' function first. " +
        "Use a public method 'setGetFunction()'"

private const val TAG = "CuteTaxi.AppFBStore"

/**
 * Class AppSettingsToFirebaseStore stores and/or reads values of shared header_preferences in a file on the device and
 * send changes into the Firebase database
 */
class AppSettingsToFirebaseStore(
    private val sharedPreferences: SharedPreferences,
    private val callback: FirebaseStore) : PreferenceDataStore() {

    interface FirebaseStore {

        fun <T> putToFirebase(key: String, value: T)
        fun <T> getFromFirebase(key: String): T

    }

    override fun getString(key: String?, defValue: String?): String? =
        sharedPreferences.getString(key, defValue)
            ?: key?.let {
                callback.getFromFirebase<String>(it)
            }
            ?: defValue


    override fun putString(key: String?, value: String?) {
        Log.d(TAG, "Put a value with AppSettingsToFirebaseStore!")
        sharedPreferences.edit().apply {
            putString(key, value)
            apply()
        }
        key?.let { callback.putToFirebase(it, arrayOf(value)) }
    }


    override fun putStringSet(key: String?, values: MutableSet<String>?) {
        Log.d(TAG, "Put a value with AppSettingsToFirebaseStore!")
        sharedPreferences.edit().apply {
            putStringSet(key, values)
            apply()
        }
        key?.let { callback.putToFirebase(it, values?.toTypedArray()) }
    }


    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? =
        sharedPreferences.getStringSet(key, defValues)
            ?: key?.let {
                callback.getFromFirebase<MutableSet<String>>(it)
            }
            ?: defValues
}