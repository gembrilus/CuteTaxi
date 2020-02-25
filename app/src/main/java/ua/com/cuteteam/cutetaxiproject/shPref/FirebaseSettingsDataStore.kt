package ua.com.cuteteam.cutetaxiproject.shPref

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceDataStore
import androidx.preference.PreferenceManager
import ua.com.cuteteam.cutetaxiproject.data.database.BaseDao

class FirebaseSettingsDataStore(
    context: Context,
    private val fbDao: BaseDao
) : PreferenceDataStore(){

    private val spKeys = SPKeys(context)
    private val shPref = PreferenceManager.getDefaultSharedPreferences(context)

    override fun getString(key: String?, defValue: String?): String? {
        return shPref.getString(key, defValue)
            ?: defValue
    }

    override fun putString(key: String?, value: String?) {
        shPref.edit().putString(key, value).apply()

        val path = spKeys.paths[key] ?: return
        fbDao.writeField(path, value)
    }
}