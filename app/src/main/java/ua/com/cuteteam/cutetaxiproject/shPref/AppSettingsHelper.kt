package ua.com.cuteteam.cutetaxiproject.shPref

import android.content.Context
import androidx.preference.PreferenceManager
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.User
import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel
import ua.com.cuteteam.cutetaxiproject.data.entities.Driver
import ua.com.cuteteam.cutetaxiproject.data.entities.Passenger
import javax.inject.Inject

/**
 * Class helps to write/read header_preferences to/from a file
 *
 * @param shPref is a shared header_preferences file where they are stored
 *
 */
class AppSettingsHelper (
    private val context: Context
) {

    private val spKeys = SPKeys(context)
    private val shPref = PreferenceManager.getDefaultSharedPreferences(context)


    /**
     *  First initialisation of shared preferences.
     *  Unpack user info and write it to shared preferences.
     */
    fun initUser(user: User) {

        name = user.name
        phone = user.phoneNumber

        when (user) {
            is Passenger -> {
                comfortClass = ComfortLevel.STANDARD
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
     * Property that shows is there an active order or not
     */
    var hasActiveOrder: Boolean
        get() = shPref.getBoolean(spKeys.HAS_ACTIVE_ORDER, false)
        set(value) = put(spKeys.HAS_ACTIVE_ORDER, value)


    /**
     * Property that shows is a first start of the app
     */
    val isFirstStart: Boolean
        get() = with(shPref.getBoolean(spKeys.IS_FIRST_START_KEY, true)) {
            if (this) put(spKeys.IS_FIRST_START_KEY, false)
            this
        }


    /**
     * Property for the user's role
     */
    var role: Boolean
        get() = shPref.getBoolean(spKeys.ROLE_KEY, false)
        set(value) = put(spKeys.ROLE_KEY, value)


    /**
     * Property for the user's name
     */
    var name: String?
        get() = shPref.getString(spKeys.NAME_KEY, null)
        set(value) = put(spKeys.NAME_KEY, value)


    /**
     * Property for the user's phone
     */
    var phone: String?
        get() = shPref.getString(spKeys.PHONE_KEY, null)
        set(value) = put(spKeys.PHONE_KEY, value)


    /**
     * Property for the car comfort class what user selects
     */
    var comfortClass: ComfortLevel?
        get() {
            val ordinal =
                shPref.getInt(spKeys.PASSENGER_CAR_CLASS_KEY, ComfortLevel.STANDARD.ordinal)
            return ComfortLevel.values()[ordinal]
        }
        set(value) = put(
            spKeys.PASSENGER_CAR_CLASS_KEY,
            value?.ordinal ?: ComfortLevel.STANDARD.ordinal
        )


    /**
     * Property of the black list drivers
     */
    var blackListOfDrivers: Set<String>?
        get() = shPref.getStringSet(spKeys.BLACK_LIST_DRIVERS_KEY, null)
        set(value) = with(shPref.edit()) {
            putStringSet(spKeys.BLACK_LIST_DRIVERS_KEY, value)
            apply()
        }


    /**
     * Property of the user's favorite addresses
     */
    var favoriteAddresses: Set<String>?
        get() = shPref.getStringSet(spKeys.FAVORITE_ADDRESSES_KEY, null)
        set(value) = with(shPref.edit()) {
            putStringSet(spKeys.FAVORITE_ADDRESSES_KEY, value)
            apply()
        }


    /**
     * Property for the car brand
     */
    var carBrand: String?
        get() = shPref.getString(spKeys.CAR_BRAND_KEY, null)
        set(value) = put(spKeys.CAR_BRAND_KEY, value)


    /**
     * Property for the car model
     */
    var carModel: String?
        get() = shPref.getString(spKeys.CAR_MODEL_KEY, null)
        set(value) = put(spKeys.CAR_MODEL_KEY, value)


    /**
     * Property for the car registration number
     */
    var carNumber: String?
        get() = shPref.getString(spKeys.CAR_NUMBER_KEY, null)
        set(value) = put(spKeys.CAR_NUMBER_KEY, value)


    /**
     * Property for the car comfort class
     */
    var carClass: ComfortLevel?
        get() {
            val ordinal = shPref.getInt(spKeys.CAR_CLASS_KEY, ComfortLevel.STANDARD.ordinal)
            return ComfortLevel.values()[ordinal]
        }
        set(value) = put(spKeys.CAR_CLASS_KEY, value?.ordinal ?: ComfortLevel.STANDARD.ordinal)


    /**
     * Property for the car color
     */
    var carColor: String?
        get() = shPref.getString(spKeys.CAR_COLOR_KEY, null)
        set(value) = put(spKeys.CAR_COLOR_KEY, value)


    /**
     * Property for enable/disable a background service
     */
    var isServiceEnabled: Boolean
        get() = shPref.getBoolean(spKeys.SEND_NOTIFICATION_KEY, true)
        set(value) = put(spKeys.SEND_NOTIFICATION_KEY, value)


    /**
     * Property for set an app theme
     */
    var appTheme: String?
        get() = shPref.getString(
            spKeys.APP_THEME_KEY,
            context.getString(R.string.value_item_light_theme)
        )
        set(value) = put(spKeys.APP_THEME_KEY, value)


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