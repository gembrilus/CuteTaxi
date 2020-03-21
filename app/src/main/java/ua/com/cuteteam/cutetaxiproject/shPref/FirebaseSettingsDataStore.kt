package ua.com.cuteteam.cutetaxiproject.shPref

import android.content.SharedPreferences
import androidx.preference.PreferenceDataStore
import ua.com.cuteteam.cutetaxiproject.data.database.BaseDao
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries

class FirebaseSettingsDataStore(
    private val shPref: SharedPreferences,
    private val fbDao: BaseDao
) : PreferenceDataStore() {

    val paths = mapOf(
        DbEntries.Passengers.Fields.NAME to DbEntries.Passengers.Fields.NAME,
        DbEntries.Passengers.Fields.PHONE to DbEntries.Passengers.Fields.PHONE,
        DbEntries.Passengers.Fields.COMFORT_LEVEL to DbEntries.Passengers.Fields.COMFORT_LEVEL,
        DbEntries.Car.BRAND to "${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.BRAND}",
        DbEntries.Car.MODEL to "${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.MODEL}",
        DbEntries.Car.NUMBER to "${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.NUMBER}",
        DbEntries.Car.COLOR to "${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.COLOR}",
        DbEntries.Car.COMFORT_LEVEL to "${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.COMFORT_LEVEL}"
    )

    override fun getString(key: String?, defValue: String?): String? =
        shPref.getString(key, defValue) ?: defValue

    override fun putString(key: String?, value: String?) {
        shPref.edit().putString(key, value).apply()
        paths[key]?.let { fbDao.writeField(it, value) }
    }
}