package ua.com.cuteteam.cutetaxiproject.shPref

import android.content.Context
import ua.com.cuteteam.cutetaxiproject.R

class SPKeys(context: Context) {

    val HAS_ACTIVE_ORDER = context.getString(R.string.key_has_active_order)
    val IS_FIRST_START_KEY = context.resources.getString(R.string.key_is_first_start_app)
    val ROLE_KEY = context.resources.getString(R.string.key_role_preference)

    val NAME_KEY = context.resources.getString(R.string.key_user_name_preference)
    val PHONE_KEY = context.resources.getString(R.string.key_user_phone_number_preference)
    val PASSENGER_CAR_CLASS_KEY = context.resources.getString(R.string.key_passenger_car_class_preference)
    val CAR_BRAND_KEY = context.resources.getString(R.string.key_car_brand_preference)
    val CAR_MODEL_KEY = context.resources.getString(R.string.key_car_model_preference)
    val CAR_CLASS_KEY = context.resources.getString(R.string.key_car_class_preference)
    val CAR_NUMBER_KEY = context.resources.getString(R.string.key_car_number_preference)
    val CAR_COLOR_KEY = context.resources.getString(R.string.key_car_color_preference)

    val BLACK_LIST_DRIVERS_KEY = context.resources.getString(R.string.key_black_list_preference)
    val FAVORITE_ADDRESSES_KEY = context.resources.getString(R.string.key_favorite_addresses_preference)

    val CAR_CATEGORY_KEY = context.resources.getString(R.string.key_car_category)
    val IMPROVEMENTS_CATEGORY_KEY = context.resources.getString(R.string.key_improvements_category)
    val ADDITIONAL_FACILITIES_CATEGORY_KEY = context.resources.getString(R.string.key_additional_facilities_category)

    val SEND_NOTIFICATION_KEY = context.resources.getString(R.string.key_send_notifications_preference)
    val APP_THEME_KEY = context.resources.getString(R.string.key_app_theme_preference)

    val paths = mapOf(
        NAME_KEY to NAME_KEY,
        PHONE_KEY to PHONE_KEY,
        PASSENGER_CAR_CLASS_KEY to PASSENGER_CAR_CLASS_KEY,
        CAR_BRAND_KEY to "${CAR_CATEGORY_KEY}/${CAR_BRAND_KEY}",
        CAR_MODEL_KEY to "${CAR_CATEGORY_KEY}/${CAR_MODEL_KEY}",
        CAR_CLASS_KEY to "${CAR_CATEGORY_KEY}/${CAR_CLASS_KEY}",
        CAR_COLOR_KEY to "${CAR_CATEGORY_KEY}/${CAR_COLOR_KEY}",
        CAR_NUMBER_KEY to "${CAR_CATEGORY_KEY}/${CAR_NUMBER_KEY}"
    )

}