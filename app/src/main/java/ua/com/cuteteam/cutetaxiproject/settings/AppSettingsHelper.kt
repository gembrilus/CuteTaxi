package ua.com.cuteteam.cutetaxiproject.settings

import android.content.SharedPreferences

/**
 * Class helps to write/read header_preferences to/from a file
 *
 * @param shPref is a shared header_preferences file where they are stored
 *
 */
class AppSettingsHelper(private val shPref: SharedPreferences) {


    /**
     * Property that shows is a first start of the app
     */
    val isFirstStart: Boolean
        get() = with(shPref.getBoolean(IS_FIRST_START_KEY, true)) {
            if (this) put(IS_FIRST_START_KEY, false)
            this
        }


    /**
     * Property for the user's role
     */
    var role: Int
        get() = shPref.getInt(ROLE_KEY, 0)
        set(value) = put(ROLE_KEY, value)


    /**
     * Property for the user's name
     */
    var name: String
        get() = shPref.getString(NAME_KEY, "")!!
        set(value) = put(NAME_KEY, value)


    /**
     * Property for the user's phone
     */
    var phone: String
        get() = shPref.getString(PHONE_KEY, "")!!
        set(value) = put(PHONE_KEY, value)


    /**
     * Property for car  comfort class
     */
    var carClass: Int
        get() = shPref.getInt(CAR_CLASS_KEY, 0)
        set(value) = put(CAR_CLASS_KEY, value)


    /**
     * Property of the black list drivers
     */
    var blackListOfDrivers: Set<String>
        get() = shPref.getStringSet(BLACK_LIST_OF_DRIVERS_KEY, emptySet())!!
        set(value) = put(BLACK_LIST_OF_DRIVERS_KEY, value)


    /**
     * Property of the user's favorite addresses
     */
    var favoriteAddresses: Set<String>
        get() = shPref.getStringSet(FAVORITE_ADDRESSES_KEY, emptySet())!!
        set(value) = put(FAVORITE_ADDRESSES_KEY, value)


    /**
     * Property for the user's phone
     */
    var carModel: String
        get() = shPref.getString(CAR_MODEL_KEY, "")!!
        set(value) = put(CAR_MODEL_KEY, value)


    /**
     * Property for the user's phone
     */
    var carNumber: String
        get() = shPref.getString(CAR_NUMBER_KEY, "")!!
        set(value) = put(CAR_NUMBER_KEY, value)


    /**
     * Property for the user's phone
     */
    var carColor: String
        get() = shPref.getString(CAR_COLOR_KEY, "")!!
        set(value) = put(CAR_COLOR_KEY, value)


    /**
     * Property for delay of coordinates updating
     */
    var delayForCoords: Int
        get() = shPref.getInt(DELAY_KEY, 60)
        set(value) = put(DELAY_KEY, value)


    /**
     * Property for enable/disable a background service
     */
    var isServiceEnabled: Boolean
        get() = shPref.getBoolean(SEND_NOTIFICATION_KEY, true)
        set(value) = put(SEND_NOTIFICATION_KEY, value)


    /**
     * Property for set an app theme
     */
    var appTheme: String
        get() = shPref.getString(APP_THEME_KEY, "Light")!!
        set(value) = put(APP_THEME_KEY, value)



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