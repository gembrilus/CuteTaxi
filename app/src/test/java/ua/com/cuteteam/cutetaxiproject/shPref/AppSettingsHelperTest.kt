package ua.com.cuteteam.cutetaxiproject.shPref

import android.content.Context
import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.entities.Car
import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel
import ua.com.cuteteam.cutetaxiproject.data.entities.Driver
import ua.com.cuteteam.cutetaxiproject.data.entities.Passenger

@RunWith(MockitoJUnitRunner.Silent::class)
class AppSettingsHelperTest {

    private val editor: SharedPreferences.Editor =
        mock {
            on { putBoolean(any(), any()) } doReturn mock
            on { putInt(any(), any()) } doReturn mock
            on { putLong(any(), any()) } doReturn mock
            on { putFloat(any(), any()) } doReturn mock
            on { putString(any(), any()) } doReturn mock
            on { putStringSet(any(), any()) } doReturn mock
        }

    private val sharedPreferences: SharedPreferences =
        mock {
            on { edit() } doReturn editor
        }

    private val context: Context = mock()

    private val appSettingsHelper by lazy {
        AppSettingsHelper(context, sharedPreferences)
    }

    private val pass = Passenger(
        name = "Max",
        phoneNumber = "+111111",
        comfortLevel = ComfortLevel.COMFORT
    )

    private val driver = Driver(
        name = "Viktor",
        phoneNumber = "+222222",
        car = Car(
            brand = "AUDI",
            model = "Q7",
            carClass = ComfortLevel.COMFORT,
            color = "darkgrey",
            regNumber = "II1111II"
        )
    )

    @Test
    fun check_that_function_initUser_where_user_is_Passenger_writes_all_its_properties(){

        //Setup

        var name = ""
        var phone = ""
        var comfortLevel: ComfortLevel? = null
        val inOrder = inOrder(sharedPreferences, editor)
        whenever(editor.putString(DbEntries.Passengers.Fields.NAME, pass.name)).doAnswer {
            name = it.getArgument(1) as String
            editor
        }
        whenever(editor.putString(DbEntries.Passengers.Fields.PHONE, pass.phoneNumber)).doAnswer {
            phone = it.getArgument(1) as String
            editor
        }
        whenever(editor.putInt(DbEntries.Passengers.Fields.COMFORT_LEVEL, pass.comfortLevel.ordinal)).doAnswer {
            comfortLevel = ComfortLevel.values()[it.getArgument(1) as Int]
            editor
        }
        doNothing().whenever(editor).apply()

        //Run

        appSettingsHelper.initUser(pass)

        //Setup2

        doReturn(name).whenever(sharedPreferences).getString(DbEntries.Passengers.Fields.NAME, null)
        doReturn(phone).whenever(sharedPreferences).getString(DbEntries.Passengers.Fields.PHONE, null)
        doReturn(comfortLevel?.ordinal.toString()).whenever(sharedPreferences).getString(DbEntries.Passengers.Fields.COMFORT_LEVEL, null)


        //Assertion
        assertEquals(name, appSettingsHelper.name)
        assertEquals(phone, appSettingsHelper.phone)
        assertEquals(comfortLevel, appSettingsHelper.comfortClass)

        //Verify
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(DbEntries.Passengers.Fields.NAME, pass.name)
        inOrder.verify(editor).apply()
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(DbEntries.Passengers.Fields.PHONE, pass.phoneNumber)
        inOrder.verify(editor).apply()
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putInt(DbEntries.Passengers.Fields.COMFORT_LEVEL, pass.comfortLevel.ordinal)
        inOrder.verify(editor).apply()
        inOrder.verify(sharedPreferences).getString(DbEntries.Passengers.Fields.NAME, null)
        inOrder.verify(sharedPreferences).getString(DbEntries.Passengers.Fields.PHONE, null)
        inOrder.verify(sharedPreferences).getString(DbEntries.Passengers.Fields.COMFORT_LEVEL, null)
        verifyNoMoreInteractions(sharedPreferences, editor)

    }

    @Test
    fun check_that_function_initUser_where_user_is_Driver_writes_all_its_properties(){

        //Setup

        var name = ""
        var phone = ""
        var brand =""
        var model =""
        var number =""
        var color =""
        var comfortLevel: ComfortLevel? = null
        val inOrder = inOrder(sharedPreferences, editor)

        whenever(editor.putString(DbEntries.Drivers.Fields.NAME, driver.name)).doAnswer {
            name = it.getArgument(1) as String
            editor
        }
        whenever(editor.putString(DbEntries.Drivers.Fields.PHONE, driver.phoneNumber)).doAnswer {
            phone = it.getArgument(1) as String
            editor
        }
        whenever(editor.putString(DbEntries.Car.BRAND, driver.car?.brand)).doAnswer {
            brand = it.getArgument(1) as String
            editor
        }
        whenever(editor.putString(DbEntries.Car.MODEL, driver.car?.model)).doAnswer {
            model = it.getArgument(1) as String
            editor
        }
        whenever(editor.putString(DbEntries.Car.NUMBER, driver.car?.regNumber)).doAnswer {
            number = it.getArgument(1) as String
            editor
        }
        whenever(editor.putString(DbEntries.Car.COLOR, driver.car?.color)).doAnswer {
            color = it.getArgument(1) as String
            editor
        }
        whenever(editor.putInt(DbEntries.Car.CAR_CLASS, driver.car?.carClass?.ordinal!!)).doAnswer {
            comfortLevel = ComfortLevel.values()[it.getArgument(1) as Int]
            editor
        }
        doNothing().whenever(editor).apply()

        //Run

        appSettingsHelper.initUser(driver)

        //Setup2

        doReturn(name).whenever(sharedPreferences).getString(DbEntries.Drivers.Fields.NAME, null)
        doReturn(phone).whenever(sharedPreferences).getString(DbEntries.Drivers.Fields.PHONE, null)
        doReturn(brand).whenever(sharedPreferences).getString(DbEntries.Car.BRAND, null)
        doReturn(model).whenever(sharedPreferences).getString(DbEntries.Car.MODEL, null)
        doReturn(number).whenever(sharedPreferences).getString(DbEntries.Car.NUMBER, null)
        doReturn(color).whenever(sharedPreferences).getString(DbEntries.Car.COLOR, null)
        doReturn(comfortLevel?.ordinal.toString()).whenever(sharedPreferences).getString(DbEntries.Car.CAR_CLASS, null)


        //Assertion
        assertEquals(name, appSettingsHelper.name)
        assertEquals(phone, appSettingsHelper.phone)
        assertEquals(brand, appSettingsHelper.carBrand)
        assertEquals(model, appSettingsHelper.carModel)
        assertEquals(number, appSettingsHelper.carNumber)
        assertEquals(color, appSettingsHelper.carColor)
        assertEquals(comfortLevel, appSettingsHelper.carClass)

        //Verify
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(DbEntries.Drivers.Fields.NAME, driver.name)
        inOrder.verify(editor).apply()
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(DbEntries.Drivers.Fields.PHONE, driver.phoneNumber)
        inOrder.verify(editor).apply()
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(DbEntries.Car.BRAND, driver.car?.brand)
        inOrder.verify(editor).apply()
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(DbEntries.Car.MODEL, driver.car?.model)
        inOrder.verify(editor).apply()
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putInt(DbEntries.Car.CAR_CLASS, driver.car?.carClass?.ordinal!!)
        inOrder.verify(editor).apply()
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(DbEntries.Car.COLOR, driver.car?.color)
        inOrder.verify(editor).apply()
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(DbEntries.Car.NUMBER, driver.car?.regNumber)
        inOrder.verify(editor).apply()
        inOrder.verify(sharedPreferences).getString(DbEntries.Drivers.Fields.NAME, null)
        inOrder.verify(sharedPreferences).getString(DbEntries.Drivers.Fields.PHONE, null)
        inOrder.verify(sharedPreferences).getString(DbEntries.Car.BRAND, null)
        inOrder.verify(sharedPreferences).getString(DbEntries.Car.MODEL, null)
        inOrder.verify(sharedPreferences).getString(DbEntries.Car.NUMBER, null)
        inOrder.verify(sharedPreferences).getString(DbEntries.Car.COLOR, null)
        inOrder.verify(sharedPreferences).getString(DbEntries.Car.CAR_CLASS, null)
        verifyNoMoreInteractions(sharedPreferences, editor)

    }

    @Test
    fun check_that_is_read_property__active_order_id(){

        //setup
        val key = "active_order_id"
        val expected = "1111"

        doReturn(key).whenever(context).getString(R.string.key_active_order_id)
        doReturn(expected).whenever(sharedPreferences).getString(key, null)

        //Assertion
        assertEquals(expected, appSettingsHelper.activeOrderId)

        //Verify
        verify(context).getString(R.string.key_active_order_id)
        verify(sharedPreferences).getString(key, null)
        verifyNoMoreInteractions(sharedPreferences, context)
    }

    @Test
    fun check_that_is_written_property_active_order_id() {

        //setup
        var newValue = ""
        val oldValue = "1111"
        val expected = "2222"
        val key = "active_order_id"
        val inOrder = inOrder(sharedPreferences, editor)

        doReturn(key).whenever(context).getString(R.string.key_active_order_id)
        whenever(editor.putString(key, expected)).doAnswer {
            newValue = it.getArgument(1) as String
            editor
        }
        doReturn(oldValue).whenever(sharedPreferences).getString(key, null)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(oldValue, appSettingsHelper.activeOrderId)

        //Setup
        appSettingsHelper.activeOrderId = expected
        doReturn(newValue).whenever(sharedPreferences).getString(key, null)

        //Assertion2
        assertEquals(expected, appSettingsHelper.activeOrderId)

        //Verify
        verify(context, atLeastOnce()).getString(R.string.key_active_order_id)
        verify(sharedPreferences, times(2)).getString(key, null)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(key, expected)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences)

    }

    @Test
    fun check_that_is_read_property_isFirstStart() {

        //Setup
        val key = "isFirstStart"
        var temp = true
        val inOrder = inOrder(sharedPreferences, context, editor)

        doReturn(key).whenever(context).getString(R.string.key_is_first_start_app)
        whenever(sharedPreferences.getBoolean(key, true))
            .doAnswer {
                if (temp) {
                    temp = false
                    return@doAnswer it.getArgument(1) as Boolean
                } else return@doAnswer temp
        }

        //Assertion
        assertEquals(true, appSettingsHelper.isFirstStart)
        assertEquals(false, appSettingsHelper.isFirstStart)

        //Verify
        verify(context, atLeastOnce()).getString(R.string.key_is_first_start_app)
        verify(sharedPreferences, times(2)).getBoolean(key, true)

        inOrder.verify(context).getString(R.string.key_is_first_start_app)
        inOrder.verify(sharedPreferences).getBoolean(key, true)
        inOrder.verify(context).getString(R.string.key_is_first_start_app)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putBoolean(key, false)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences, context)
    }

    @Test
    fun check_that_is_read_property_Role() {
        //setup
        val key = "role"
        val expected = true

        doReturn(key).whenever(context).getString(R.string.key_role_preference)
        doReturn(expected).whenever(sharedPreferences).getBoolean(key, false)

        //Assertion
        assertEquals(expected, appSettingsHelper.role)

        //Verify
        verify(context).getString(R.string.key_role_preference)
        verify(sharedPreferences).getBoolean(key, false)
        verifyNoMoreInteractions(sharedPreferences, context)
    }

    @Test
    fun check_that_is_written_property_Role() {

        //setup
        var newValue = false
        val key = "role"
        val expected = true
        val inOrder = inOrder(sharedPreferences, context, editor)

        doReturn(key).whenever(context).getString(R.string.key_role_preference)
        whenever(editor.putBoolean(key, expected)).doAnswer {
            newValue = it.getArgument(1) as Boolean
            editor
        }
        doReturn(false).whenever(sharedPreferences).getBoolean(key, false)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(false, appSettingsHelper.role)

        //Setup
        appSettingsHelper.role = expected

        doReturn(newValue).whenever(sharedPreferences).getBoolean(key, false)

        //Assertion
        assertEquals(expected, appSettingsHelper.role)

        //Verify
        verify(context, atLeastOnce()).getString(R.string.key_role_preference)
        verify(sharedPreferences, times(2)).getBoolean(key, false)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putBoolean(key, expected)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences, context)
    }

    @Test
    fun check_that_is_read_property_Name() {
        //Setup
        doReturn("MAX").whenever(sharedPreferences).getString(DbEntries.Passengers.Fields.NAME, null)

        //Assertion
        assertEquals("MAX", appSettingsHelper.name)

        //Verify
        verify(sharedPreferences).getString(DbEntries.Passengers.Fields.NAME, null)
    }

    @Test
    fun check_that_is_written_property_Name() {

        //setup
        var newValue = ""
        val oldValue = "Max"
        val expected = "Yura"
        val key = DbEntries.Passengers.Fields.NAME
        val inOrder = inOrder(sharedPreferences, editor)

        whenever(editor.putString(key, expected)).doAnswer {
            newValue = it.getArgument(1) as String
            editor
        }
        doReturn(oldValue).whenever(sharedPreferences).getString(key, null)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(oldValue, appSettingsHelper.name)

        //Setup
        appSettingsHelper.name = expected
        doReturn(newValue).whenever(sharedPreferences).getString(key, null)

        //Assertion2
        assertEquals(expected, appSettingsHelper.name)

        //Verify
        verify(sharedPreferences, times(2)).getString(key, null)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(key, expected)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences)
    }

    @Test
    fun check_that_is_read_property_Phone() {
        //Setup
        doReturn("+15556667777").whenever(sharedPreferences).getString(DbEntries.Passengers.Fields.PHONE, null)

        //Assertion
        assertEquals("+15556667777", appSettingsHelper.phone)

        //Verify
        verify(sharedPreferences).getString(DbEntries.Passengers.Fields.PHONE, null)
    }

    @Test
    fun check_that_is_written_property_Phone() {

        //setup
        var newValue = ""
        val oldValue = "+15556667777"
        val expected = "+13333334444"
        val key = DbEntries.Passengers.Fields.PHONE
        val inOrder = inOrder(sharedPreferences, editor)

        whenever(editor.putString(key, expected)).doAnswer {
            newValue = it.getArgument(1) as String
            editor
        }
        doReturn(oldValue).whenever(sharedPreferences).getString(key, null)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(oldValue, appSettingsHelper.phone)

        //Setup
        appSettingsHelper.phone = expected
        doReturn(newValue).whenever(sharedPreferences).getString(key, null)

        //Assertion2
        assertEquals(expected, appSettingsHelper.phone)

        //Verify
        verify(sharedPreferences, times(2)).getString(key, null)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(key, expected)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences)
    }

    @Test
    fun check_that_is_read_property_ComfortClass() {
        //Setup
        doReturn(ComfortLevel.STANDARD.ordinal.toString()).whenever(sharedPreferences).getString(DbEntries.Passengers.Fields.COMFORT_LEVEL, null)

        //Assertion
        assertEquals(ComfortLevel.STANDARD, appSettingsHelper.comfortClass)

        //Verify
        verify(sharedPreferences).getString(DbEntries.Passengers.Fields.COMFORT_LEVEL, null)
    }

    @Test
    fun check_that_is_written_property_ComfortClass() {

        //setup
        var newValue: ComfortLevel? = null
        val oldValue = ComfortLevel.STANDARD
        val expected = ComfortLevel.COMFORT
        val key = DbEntries.Passengers.Fields.COMFORT_LEVEL
        val inOrder = inOrder(sharedPreferences, editor)

        whenever(editor.putInt(key, expected.ordinal)).doAnswer {
            newValue = ComfortLevel.values()[it.getArgument(1) as Int]
            editor
        }
        doReturn(oldValue.ordinal.toString()).whenever(sharedPreferences).getString(key, null)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(oldValue, appSettingsHelper.comfortClass)

        //Setup
        appSettingsHelper.comfortClass = expected
        doReturn(newValue?.ordinal.toString()).whenever(sharedPreferences).getString(key, null)

        //Assertion2
        assertEquals(expected, appSettingsHelper.comfortClass)

        //Verify
        verify(sharedPreferences, times(2)).getString(key, null)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putInt(key, expected.ordinal)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences)
    }

    @Test
    fun check_that_is_read_property_CarBrand() {
        //Setup
        doReturn("BMW").whenever(sharedPreferences).getString(DbEntries.Car.BRAND, null)

        //Assertion
        assertEquals("BMW", appSettingsHelper.carBrand)

        //Verify
        verify(sharedPreferences).getString(DbEntries.Car.BRAND, null)
    }

    @Test
    fun check_that_is_written_property_CarBrand(){

        //setup
        var newValue = ""
        val oldValue = "BMW"
        val expected = "AUDI"
        val key = DbEntries.Car.BRAND
        val inOrder = inOrder(sharedPreferences, editor)

        whenever(editor.putString(key, expected)).doAnswer {
            newValue = it.getArgument(1) as String
            editor
        }
        doReturn(oldValue).whenever(sharedPreferences).getString(key, null)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(oldValue, appSettingsHelper.carBrand)

        //Setup
        appSettingsHelper.carBrand = expected
        doReturn(newValue).whenever(sharedPreferences).getString(key, null)

        //Assertion2
        assertEquals(expected, appSettingsHelper.carBrand)

        //Verify
        verify(sharedPreferences, times(2)).getString(key, null)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(key, expected)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences)

    }

    @Test
    fun check_that_is_read_property_CarModel() {
        //Setup
        doReturn("BMW QQ").whenever(sharedPreferences).getString(DbEntries.Car.MODEL, null)

        //Assertion
        assertEquals("BMW QQ", appSettingsHelper.carModel)

        //Verify
        verify(sharedPreferences).getString(DbEntries.Car.MODEL, null)
    }

    @Test
    fun check_that_is_written_property_CarModel() {

        //setup
        var newValue = ""
        val oldValue = "BMW QQ"
        val expected = "AUDI Q100"
        val key = DbEntries.Car.MODEL
        val inOrder = inOrder(sharedPreferences, editor)

        whenever(editor.putString(key, expected)).doAnswer {
            newValue = it.getArgument(1) as String
            editor
        }
        doReturn(oldValue).whenever(sharedPreferences).getString(key, null)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(oldValue, appSettingsHelper.carModel)

        //Setup
        appSettingsHelper.carModel = expected
        doReturn(newValue).whenever(sharedPreferences).getString(key, null)

        //Assertion2
        assertEquals(expected, appSettingsHelper.carModel)

        //Verify
        verify(sharedPreferences, times(2)).getString(key, null)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(key, expected)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences)
    }

    @Test
    fun check_that_is_read_property_CarNumber() {
        //Setup
        doReturn("II1111II").whenever(sharedPreferences).getString(DbEntries.Car.NUMBER, null)

        //Assertion
        assertEquals("II1111II", appSettingsHelper.carNumber)

        //Verify
        verify(sharedPreferences).getString(DbEntries.Car.NUMBER, null)
    }

    @Test
    fun check_that_is_written_property_CarNumber() {

        //setup
        var newValue = ""
        val oldValue = "II1111II"
        val expected = "CO0000CO"
        val key = DbEntries.Car.NUMBER
        val inOrder = inOrder(sharedPreferences, editor)

        whenever(editor.putString(key, expected)).doAnswer {
            newValue = it.getArgument(1) as String
            editor
        }
        doReturn(oldValue).whenever(sharedPreferences).getString(key, null)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(oldValue, appSettingsHelper.carNumber)

        //Setup
        appSettingsHelper.carNumber = expected
        doReturn(newValue).whenever(sharedPreferences).getString(key, null)

        //Assertion2
        assertEquals(expected, appSettingsHelper.carNumber)

        //Verify
        verify(sharedPreferences, times(2)).getString(key, null)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(key, expected)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences)

    }

    @Test
    fun check_that_is_read_property_CarClass() {
        //Setup
        doReturn(ComfortLevel.STANDARD.ordinal.toString()).whenever(sharedPreferences).getString(DbEntries.Car.CAR_CLASS, null)

        //Assertion
        assertEquals(ComfortLevel.STANDARD, appSettingsHelper.carClass)

        //Verify
        verify(sharedPreferences).getString(DbEntries.Car.CAR_CLASS, null)
    }

    @Test
    fun check_that_is_written_property_CarClass() {

        //setup
        var newValue: ComfortLevel? = null
        val oldValue = ComfortLevel.STANDARD
        val expected = ComfortLevel.COMFORT
        val key = DbEntries.Car.CAR_CLASS
        val inOrder = inOrder(sharedPreferences, editor)

        whenever(editor.putInt(key, expected.ordinal)).doAnswer {
            newValue = ComfortLevel.values()[it.getArgument(1) as Int]
            editor
        }
        doReturn(oldValue.ordinal.toString()).whenever(sharedPreferences).getString(key, null)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(oldValue, appSettingsHelper.carClass)

        //Setup
        appSettingsHelper.carClass = expected
        doReturn(newValue?.ordinal.toString()).whenever(sharedPreferences).getString(key, null)

        //Assertion2
        assertEquals(expected, appSettingsHelper.carClass)

        //Verify
        verify(sharedPreferences, times(2)).getString(key, null)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putInt(key, expected.ordinal)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences)

    }

    @Test
    fun check_that_is_read_property_CarColor() {
        //Setup
        doReturn("Green").whenever(sharedPreferences).getString(DbEntries.Car.COLOR, null)

        //Assertion
        assertEquals("Green", appSettingsHelper.carColor)

        //Verify
        verify(sharedPreferences).getString(DbEntries.Car.COLOR, null)
    }

    @Test
    fun check_that_is_written_property_CarColor() {

        //setup
        var newValue = ""
        val oldValue = "Green"
        val expected = "Yellow"
        val key = DbEntries.Car.COLOR
        val inOrder = inOrder(sharedPreferences, editor)

        whenever(editor.putString(key, expected)).doAnswer {
            newValue = it.getArgument(1) as String
            editor
        }
        doReturn(oldValue).whenever(sharedPreferences).getString(key, null)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(oldValue, appSettingsHelper.carColor)

        //Setup
        appSettingsHelper.carColor = expected
        doReturn(newValue).whenever(sharedPreferences).getString(key, null)

        //Assertion2
        assertEquals(expected, appSettingsHelper.carColor)

        //Verify
        verify(sharedPreferences, times(2)).getString(key, null)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(key, expected)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences)

    }

    @Test
    fun check_that_is_read_property_isServiceEnabled() {
        //setup
        val key = "send_notifications"
        val expected = true

        doReturn(key).whenever(context).getString(R.string.key_send_notifications_preference)
        doReturn(expected).whenever(sharedPreferences).getBoolean(key, true)

        //Assertion
        assertEquals(expected, appSettingsHelper.isServiceEnabled)

        //Verify
        verify(sharedPreferences).getBoolean(key, true)
    }

    @Test
    fun check_that_is_written_property_isServiceEnabled() {

        //setup
        var newValue = false
        val key = "send_notifications"
        val expected = true
        val inOrder = inOrder(sharedPreferences, context, editor)

        doReturn(key).whenever(context).getString(R.string.key_send_notifications_preference)
        whenever(editor.putBoolean(key, expected)).doAnswer {
            newValue = it.getArgument(1) as Boolean
            editor
        }
        doReturn(false).whenever(sharedPreferences).getBoolean(key, true)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(false, appSettingsHelper.isServiceEnabled)

        //Setup
        appSettingsHelper.isServiceEnabled = expected

        doReturn(newValue).whenever(sharedPreferences).getBoolean(key, true)

        //Assertion
        assertEquals(expected, appSettingsHelper.isServiceEnabled)

        //Verify
        verify(context, atLeastOnce()).getString(R.string.key_send_notifications_preference)
        verify(sharedPreferences, times(2)).getBoolean(key, true)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putBoolean(key, expected)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences, context)

    }

    @Test
    fun check_that_is_read_property_AppTheme() {
        //Setup
        val key = "app_theme"
        val value = "Light"
        doReturn(key).whenever(context).getString(R.string.key_app_theme_preference)
        doReturn(value).whenever(sharedPreferences).getString(key, null)

        //Assertion
        assertEquals(value, appSettingsHelper.appTheme)

        //Verify
        verify(sharedPreferences).getString(key, null)
    }

    @Test
    fun check_that_is_written_property_AppTheme() {

        //setup
        var newValue = ""
        val oldValue = "Light"
        val expected = "Dark"
        val key = "app_theme"
        val inOrder = inOrder(sharedPreferences, editor)

        doReturn(key).whenever(context).getString(R.string.key_app_theme_preference)
        whenever(editor.putString(key, expected)).doAnswer {
            newValue = it.getArgument(1) as String
            editor
        }
        doReturn(oldValue).whenever(sharedPreferences).getString(key, null)
        doNothing().whenever(editor).apply()

        //Assertion1
        assertEquals(oldValue, appSettingsHelper.appTheme)

        //Setup
        appSettingsHelper.appTheme = expected
        doReturn(newValue).whenever(sharedPreferences).getString(key, null)

        //Assertion2
        assertEquals(expected, appSettingsHelper.appTheme)

        //Verify
        verify(context, atLeastOnce()).getString(R.string.key_app_theme_preference)
        verify(sharedPreferences, times(2)).getString(key, null)
        inOrder.verify(sharedPreferences).edit()
        inOrder.verify(editor).putString(key, expected)
        inOrder.verify(editor).apply()
        verifyNoMoreInteractions(sharedPreferences)

    }

}