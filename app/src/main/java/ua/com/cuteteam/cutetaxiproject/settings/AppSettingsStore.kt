package ua.com.cuteteam.cutetaxiproject.settings

import android.content.SharedPreferences
import androidx.preference.PreferenceDataStore

/**
 * Class AppSettingsStore stores and/or reads values of shared header_preferences in a custom file on the device
 * @param sharedPreferences is a replacement of default store place
 */

open class AppSettingsStore(
    private val sharedPreferences: SharedPreferences)
    : PreferenceDataStore(){

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    override fun putLong(key: String?, value: Long) {
        sharedPreferences.edit().apply {
            putLong(key, value)
            apply()
        }
    }

    override fun putInt(key: String?, value: Int) {
        sharedPreferences.edit().apply{
            putInt(key, value)
            apply()
        }
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    override fun putBoolean(key: String?, value: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    override fun putStringSet(key: String?, values: MutableSet<String>?) {
        sharedPreferences.edit().apply {
            putStringSet(key, values)
            apply()
        }
    }

    override fun getLong(key: String?, defValue: Long): Long {
        return sharedPreferences.getLong(key, defValue)
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        return sharedPreferences.getFloat(key, defValue)
    }

    override fun putFloat(key: String?, value: Float) {
        sharedPreferences.edit().apply {
            putFloat(key, value)
            apply()
        }
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? {
        return sharedPreferences.getStringSet(key, defValues)
    }

    override fun getString(key: String?, defValue: String?): String? {
        return sharedPreferences.getString(key, defValue)
    }

    override fun putString(key: String?, value: String?) {
        sharedPreferences.edit().apply {
            putString(key, value)
            apply()
        }
    }
}