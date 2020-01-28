package ua.com.cuteteam.cutetaxiproject.settings

import android.content.SharedPreferences

@Deprecated(
    message = "It's a mock only",
    replaceWith = ReplaceWith("really instance of Firebase Dao-class with read/write methods")
)
class FbDaoMock {
    fun putValue() {}
    fun getValue(): String = ""
    fun putValueSet() {}
    fun getValueSet(): MutableSet<String> = mutableSetOf()
}

/**
 * Class AppSettingsToFirebaseStore stores and/or reads values of shared header_preferences in a file on the device and
 * send changes into the Firebase database
 * @param sharedPreferences is a replacement of default store place
 *
 * @param fbDaoMock is an instance of the class FbDaoMock (here should be a really instance of the Firebase dao-class), that
 * have to read and write settings to the Firebase database
 */
class AppSettingsToFirebaseStore(
    sharedPreferences: SharedPreferences,
    private val fbDaoMock: FbDaoMock = FbDaoMock()
) : AppSettingsStore(sharedPreferences) {

    override fun putStringSet(key: String?, values: MutableSet<String>?) {
        super.putStringSet(key, values)
        fbDaoMock.putValueSet()
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? =
        super.getStringSet(key, defValues)
            .run {
                if (this == defValues) {
                    fbDaoMock.getValueSet()
                } else this
            }

    override fun getString(key: String?, defValue: String?): String? =
        super.getString(key, defValue)
            .run {
                if (this == defValue) {
                    fbDaoMock.getValue()
                } else this
            }

    override fun putString(key: String?, value: String?) {
        super.putString(key, value)
        fbDaoMock.putValue()
    }
}