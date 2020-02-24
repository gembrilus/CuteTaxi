package ua.com.cuteteam.cutetaxiproject.shPref

import androidx.preference.PreferenceDataStore

class FirebaseSettingsDataStore(
    private val callback: FirebaseStoreCallback
) : PreferenceDataStore(){

    interface FirebaseStoreCallback{

        fun getString(key: String?, defValue: String?): String?
        fun putString(key: String?, value: String?)

    }

    override fun getString(key: String?, defValue: String?): String? {
        return super.getString(key, defValue)
            ?: callback.getString(key, defValue)
            ?: defValue
    }

    override fun putString(key: String?, value: String?) {
        super.putString(key, value)
        callback.putString(key, value)
    }
}