package ua.com.cuteteam.cutetaxiproject.settings

import android.content.SharedPreferences

/**
 * Class helps to write/read header_preferences to/from a file
 *
 * @param shPref is a shared header_preferences file where they are stored
 *
 */


class AppSettingsHelper private constructor() {

    private var _shPref: SharedPreferences? = null
    private val shPref get() = _shPref
        ?: throw IllegalArgumentException("An instance of SharedPreference is NULL." +
                "You must to use a static method getInstance()" +
                "to create an instance of SharedPreference")

    /**
     * Property of the user's role
     */
    var role: Int
    get() = shPref.getInt("role", -1)
    set(value) {
        shPref.edit().apply {
            putInt("role", value)
            apply()
        }
    }

    /**
     * Read a user name from shared header_preferences
     *
     * @param key is a key for a storing the preference
     */
    fun getUserName(key: String) = shPref.getString(key, "")


    /**
     * Write a user name to the file of shared header_preferences
     *
     * @param key is a key for a storing the preference
     * @param value is a value of the preference
     */
    fun setUserName(key: String, value: String) {
        shPref.edit().apply {
            putString(key, value)
            apply()
        }
    }

    /**
     * Read a phone number from shared header_preferences
     *
     * @param is a key for a storing the preference
     */
    fun getPhoneNumber(key: String): String = shPref.getString(key, null)
        ?: throw IllegalArgumentException("No phone number. Please contact support")


    /**
     * Write a user name to the file of shared header_preferences
     *
     * @param key is a key for a storing the preference
     * @param value is a value of the preference
     */
    fun setPhoneNumber(key: String, value: String) {
        shPref.edit().apply {
            putString(key, value)
            apply()
        }
    }

    companion object {

        fun getInstance(sharedPreferences: SharedPreferences) = AppSettingsHelper().apply {
            _shPref = sharedPreferences
        }

    }

}