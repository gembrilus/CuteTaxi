package ua.com.cuteteam.cutetaxiproject.settings

import android.content.SharedPreferences

/**
 * Class helps to write/read header_preferences to/from a file
 *
 * @param shPref is a shared header_preferences file where they are stored
 *
 */

private const val IS_FIRST_START_KEY = "isFirstStart"
private const val ROLE_KEY = "role"

class AppSettingsHelper(private val shPref: SharedPreferences) {


    /**
     * Property that shows is a first start of the app
     */
    val isFirstStart: Boolean
        get() = with(shPref.getBoolean(IS_FIRST_START_KEY, true)){
            if (this) put(IS_FIRST_START_KEY, false)
            this
        }


    /**
     * Property of the user's role
     */
    var role: Int
        get() = shPref.getInt(ROLE_KEY, -1)
        set(value) = put(ROLE_KEY, value)



    private fun <T> put(key: String, value: T) =
            with(shPref.edit()) {
                when (value) {
                    is Int -> putInt(key, value)
                    is Boolean -> putBoolean(key, value)
                    is String -> putString(key, value)
                    is Float -> putFloat(key, value)
                    is Long -> putLong(key, value)
                }
                apply()
            }
}