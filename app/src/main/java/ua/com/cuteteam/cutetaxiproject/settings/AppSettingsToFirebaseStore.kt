package ua.com.cuteteam.cutetaxiproject.settings

private const val ERROR_MESSAGE_PUT = "You need to set a 'put' function first. " +
        "Use a public method 'setPutFunction()'"

private const val ERROR_MESSAGE_GET = "You need to set a 'get' function first. " +
        "Use a public method 'setGetFunction()'"


/**
 * Class AppSettingsToFirebaseStore stores and/or reads values of shared header_preferences in a file on the device and
 * send changes into the Firebase database
 */
class AppSettingsToFirebaseStore : AppSettingsStore() {

    private var _put: ((String, Array<String>) -> Unit)? = null
    private val put
        get() = _put ?: throw IllegalArgumentException(ERROR_MESSAGE_PUT)

    /**
     * Sets a function that puts values into the database.
     *
     * @param put is a lambda or function reference that take a string key (path into a database)
     * and various parameters (or one) and put them to database
     *
     */
    fun setPutFunction(put: (String, Array<String>) -> Unit) {
        _put = put
    }

    private var _get: ((String) -> Array<String>)? = null
    private val get
        get() = _get ?: throw IllegalArgumentException(ERROR_MESSAGE_GET)


    /**
     * Sets a function that receives values from the database.
     *
     * @param get is a lambda or function reference that take a string key (path into a database)
     * and return array of values (or one value only) from the database
     *
     */
    fun setGetFunction(get: (String) -> Array<String>) {
        _get = get
    }


    override fun getString(key: String?, defValue: String?): String =
        super.getString(key, defValue)
            ?: key?.let { get(it)[0] }
            ?: defValue
            ?: ""


    override fun putString(key: String?, value: String?) {
        super.putString(key, value)
        key?.let { k ->
            value?.let { v ->
                put(k, arrayOf(v))
            }
        } ?: Unit
    }


    override fun putStringSet(key: String?, values: MutableSet<String>?) {
        super.putStringSet(key, values)
        key?.let { k ->
            values?.let { v ->
                put(k, v.toTypedArray())
            }
        } ?: Unit
    }


    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> =
        super.getStringSet(key, defValues)
            ?: key?.let { get(it).toMutableSet() }
            ?: defValues
            ?: mutableSetOf()
}