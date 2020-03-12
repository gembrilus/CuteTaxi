package ua.com.cuteteam.cutetaxiproject.shPref

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.User
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel
import ua.com.cuteteam.cutetaxiproject.data.entities.Driver
import ua.com.cuteteam.cutetaxiproject.data.entities.Passenger

/**
 * Class helps to write/read header_preferences to/from a file
 *
 * @param shPref is a shared header_preferences file where they are stored
 *
 */
class AppSettingsHelper (
    private val context: Context,
    private val shPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
) {

    /**
     *  First initialisation of shared preferences.
     *  Unpack user info and write it to shared preferences.
     */
    fun initUser(user: User) {

        name = user.name
        phone = user.phoneNumber

        when (user) {
            is Passenger -> {
                comfortClass = user.comfortLevel
            }
            is Driver -> {
                carBrand = user.car?.brand
                carModel = user.car?.model
                carClass = user.car?.comfortLevel
                carColor = user.car?.color
                carNumber = user.car?.regNumber
            }
        }
    }


    /**
     * Property that stores an active order ID
     */
    var activeOrderId: String?
        get() = shPref.getString(context.getString(R.string.key_active_order_id), null)
        set(value) = put(context.getString(R.string.key_active_order_id), value)
    

    /**
     * Property that shows is a first start of the app
     */
    val isFirstStart: Boolean
        get() = with(shPref.getBoolean(context.getString(R.string.key_is_first_start_app), true)) {
            if (this) put(context.getString(R.string.key_is_first_start_app), false)
            this
        }


    /**
     * Property for the user's role
     */
    var role: Boolean
        get() = shPref.getBoolean(context.getString(R.string.key_role_preference), false)
        set(value) = put(context.getString(R.string.key_role_preference), value)


    /**
     * Property for the user's name
     */
    var name: String?
        get() = shPref.getString(DbEntries.Passengers.Fields.NAME, null)
        set(value) = put(DbEntries.Passengers.Fields.NAME, value)


    /**
     * Property for the user's phone
     */
    var phone: String?
        get() = shPref.getString(DbEntries.Passengers.Fields.PHONE, null)
        set(value) = put(DbEntries.Passengers.Fields.PHONE, value)


    /**
     * Property for the car comfort class what user selects
     */
    var comfortClass: ComfortLevel?
        get() {
            val ordinal =
                shPref.getInt(DbEntries.Passengers.Fields.COMFORT_LEVEL, ComfortLevel.STANDARD.ordinal)
            return ComfortLevel.values()[ordinal]
        }
        set(value) = put(
            DbEntries.Passengers.Fields.COMFORT_LEVEL,
            value?.ordinal ?: ComfortLevel.STANDARD.ordinal
        )


/*    *//**
     * Property of the black list drivers
     *//*
    var blackListOfDrivers: Set<String>?
        get() = shPref.getStringSet(spKeys.BLACK_LIST_DRIVERS_KEY, null)
        set(value) = with(shPref.edit()) {
            putStringSet(spKeys.BLACK_LIST_DRIVERS_KEY, value)
            apply()
        }*/


    /**
     * Property of the user's favorite addresses
     */
    var favoriteAddresses: Set<String>?
        get() = shPref.getStringSet(context.getString(R.string.key_black_list_preference), null)
        set(value) = with(shPref.edit()) {
            putStringSet(context.getString(R.string.key_black_list_preference), value)
            apply()
        }


    /**
     * Property for the car brand
     */
    var carBrand: String?
        get() = shPref.getString(DbEntries.Car.BRAND, null)
        set(value) = put(DbEntries.Car.BRAND, value)


    /**
     * Property for the car model
     */
    var carModel: String?
        get() = shPref.getString(DbEntries.Car.MODEL, null)
        set(value) = put(DbEntries.Car.MODEL, value)


    /**
     * Property for the car registration number
     */
    var carNumber: String?
        get() = shPref.getString(DbEntries.Car.NUMBER, null)
        set(value) = put(DbEntries.Car.NUMBER, value)


    /**
     * Property for the car comfort class
     */
    var carClass: ComfortLevel?
        get() {
            val ordinal = shPref.getInt(DbEntries.Car.COMFORT_LEVEL, ComfortLevel.STANDARD.ordinal)
            return ComfortLevel.values()[ordinal]
        }
        set(value) = put(DbEntries.Car.COMFORT_LEVEL, value?.ordinal ?: ComfortLevel.STANDARD.ordinal)


    /**
     * Property for the car color
     */
    var carColor: String?
        get() = shPref.getString(DbEntries.Car.COLOR, null)
        set(value) = put(DbEntries.Car.COLOR, value)


    /**
     * Property for enable/disable a background service
     */
    var isServiceEnabled: Boolean
        get() = shPref.getBoolean(context.getString(R.string.key_send_notifications_preference), true)
        set(value) = put(context.getString(R.string.key_send_notifications_preference), value)


    /**
     * Property for set an app theme
     */
    var appTheme: String?
        get() = shPref.getString(
            context.getString(R.string.key_app_theme_preference),
            context.getString(R.string.value_item_light_theme)
        )
        set(value) = put(context.getString(R.string.key_app_theme_preference), value)


    private fun <T> put(key: String, value: T) =
        with(shPref.edit()) {
            when (value) {
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is String? -> putString(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
            }
            apply()
        }

}