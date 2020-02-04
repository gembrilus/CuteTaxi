package ua.com.cuteteam.cutetaxiproject.settings

import android.util.Log


@Deprecated(
    message = "It's a MOCK only",
    replaceWith = ReplaceWith("real instance of database")
)
class FbDbMock {

    private val db = mutableMapOf<String, MutableList<String>>()

    private val TAG = "CuteTaxi.FbDbMock"
    private val MESSAGE = "It is only a MOCK!"

    fun putValueToDb(parameterName: String, vararg value: String){
        Log.d(TAG, "${MESSAGE} Put a value to database!")
        value.forEach {rec ->
            db[parameterName]?.let {list ->
                list.clear()
                list.add(rec)
            }
        }
    }

    fun getValueFromDb(parameterName: String): Array<String> {
        return if (db.containsKey(parameterName)) {
            Log.d(TAG, "${MESSAGE} Get a value from database!")
            db.getValue(parameterName).toTypedArray()
        } else arrayOf("")
    }

}