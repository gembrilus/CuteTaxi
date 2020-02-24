package ua.com.cuteteam.cutetaxiproject.shPref

import ua.com.cuteteam.cutetaxiproject.data.database.BaseDao

class FirebasePreferenceCallback(
    private val fbDao: BaseDao
) : FirebaseSettingsDataStore.FirebaseStoreCallback {

    private val paths = mapOf(
        NAME_KEY to NAME_KEY,
        PHONE_KEY to PHONE_KEY,
        CAR_CLASS_FOR_PASSENGER_KEY to CAR_CLASS_FOR_PASSENGER_KEY,
        CAR_BRAND_KEY to "$CAR_CATEGORY_KEY/$CAR_BRAND_KEY",
        CAR_MODEL_KEY to "$CAR_CATEGORY_KEY/$CAR_MODEL_KEY",
        CAR_CLASS_KEY to "$CAR_CATEGORY_KEY/$CAR_CLASS_KEY",
        CAR_COLOR_KEY to "$CAR_CATEGORY_KEY/$CAR_COLOR_KEY",
        CAR_NUMBER_KEY to "$CAR_CATEGORY_KEY/$CAR_NUMBER_KEY"
    )

    override fun getString(key: String?, defValue: String?): String? {
        val path = paths[key] ?: return null
        return (fbDao as MockFbDb).getField<String>(key, path)
    }

    override fun putString(key: String?, value: String?) {
        val path = paths[key] ?: return
        (fbDao as MockFbDb).putField<String>(key, path)
    }

}