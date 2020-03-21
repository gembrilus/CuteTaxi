package ua.com.cuteteam.cutetaxiproject.shPref

import android.content.SharedPreferences
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.com.cuteteam.cutetaxiproject.data.database.BaseDao
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel

@RunWith(MockitoJUnitRunner.Silent::class)
class FirebaseSettingsDataStoreTest {

    private val editor = mock<SharedPreferences.Editor> {
        on { putString(any(), any()) } doReturn mock
        on { putStringSet(any(), any()) } doReturn mock
    }

    private val shPref = mock<SharedPreferences> {
        on { edit() } doReturn editor
    }

    val fbDao = mock<BaseDao>()

    val dataStore = FirebaseSettingsDataStore(shPref, fbDao)

    val name = "MAX"
    val phone = "+111111111"
    val comfortLevel = ComfortLevel.STANDARD
    val brand = "Telega"
    val model = "Drov"
    val color = "yellow"
    val number = "II1111II"

    @Test
    fun check_that_overriden_method_getString_is_read_correct() {

        //Setup
        val inOrder = inOrder(shPref)

        doReturn(name).whenever(shPref).getString(DbEntries.Passengers.Fields.NAME, null)
        doReturn(phone).whenever(shPref).getString(DbEntries.Passengers.Fields.PHONE, null)
        doReturn(comfortLevel.ordinal.toString()).whenever(shPref)
            .getString(DbEntries.Car.COMFORT_LEVEL, null)
        doReturn(brand).whenever(shPref).getString(DbEntries.Car.BRAND, null)
        doReturn(model).whenever(shPref).getString(DbEntries.Car.MODEL, null)
        doReturn(color).whenever(shPref).getString(DbEntries.Car.COLOR, null)
        doReturn(number).whenever(shPref).getString(DbEntries.Car.NUMBER, null)

        //Assertion
        assertEquals(name, dataStore.getString(DbEntries.Passengers.Fields.NAME, null))
        assertEquals(phone, dataStore.getString(DbEntries.Passengers.Fields.PHONE, null))
        assertEquals(
            comfortLevel.ordinal.toString(),
            dataStore.getString(DbEntries.Car.COMFORT_LEVEL, null)
        )
        assertEquals(brand, dataStore.getString(DbEntries.Car.BRAND, null))
        assertEquals(model, dataStore.getString(DbEntries.Car.MODEL, null))
        assertEquals(color, dataStore.getString(DbEntries.Car.COLOR, null))
        assertEquals(number, dataStore.getString(DbEntries.Car.NUMBER, null))

        //Verify
        inOrder.verify(shPref).getString(DbEntries.Passengers.Fields.NAME, null)
        inOrder.verify(shPref).getString(DbEntries.Passengers.Fields.PHONE, null)
        inOrder.verify(shPref).getString(DbEntries.Car.COMFORT_LEVEL, null)
        inOrder.verify(shPref).getString(DbEntries.Car.BRAND, null)
        inOrder.verify(shPref).getString(DbEntries.Car.MODEL, null)
        inOrder.verify(shPref).getString(DbEntries.Car.COLOR, null)
        inOrder.verify(shPref).getString(DbEntries.Car.NUMBER, null)

        verifyNoMoreInteractions(shPref)
    }

    @Test
    fun check_that_override_method_puString_and_writeField_is_invoked_and_read_paths_map() {

        //Setup
        val inOrder = inOrder(shPref, editor, fbDao)
        doReturn(editor).whenever(editor).putString(DbEntries.Passengers.Fields.NAME, null)
        doReturn(editor).whenever(editor).putString(DbEntries.Passengers.Fields.PHONE, null)
        doReturn(editor).whenever(editor).putString(DbEntries.Car.COMFORT_LEVEL, null)
        doReturn(editor).whenever(editor).putString(DbEntries.Car.BRAND, null)
        doReturn(editor).whenever(editor).putString(DbEntries.Car.MODEL, null)
        doReturn(editor).whenever(editor).putString(DbEntries.Car.COLOR, null)
        doReturn(editor).whenever(editor).putString(DbEntries.Car.NUMBER, null)
        doNothing().whenever(editor).apply()
        doNothing().whenever(fbDao).writeField(DbEntries.Passengers.Fields.NAME, null)
        doNothing().whenever(fbDao).writeField(DbEntries.Passengers.Fields.PHONE, null)
        doNothing().whenever(fbDao).writeField("${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.COMFORT_LEVEL}", null)
        doNothing().whenever(fbDao).writeField("${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.BRAND}", null)
        doNothing().whenever(fbDao).writeField("${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.MODEL}", null)
        doNothing().whenever(fbDao).writeField("${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.COLOR}", null)
        doNothing().whenever(fbDao).writeField("${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.NUMBER}", null)

        //Run
        dataStore.putString(DbEntries.Passengers.Fields.NAME, null)
        dataStore.putString(DbEntries.Passengers.Fields.PHONE, null)
        dataStore.putString(DbEntries.Car.COMFORT_LEVEL, null)
        dataStore.putString(DbEntries.Car.BRAND, null)
        dataStore.putString(DbEntries.Car.MODEL, null)
        dataStore.putString(DbEntries.Car.COLOR, null)
        dataStore.putString(DbEntries.Car.NUMBER, null)

        //Verify
        inOrder.verify(shPref).edit()
        inOrder.verify(editor).putString(DbEntries.Passengers.Fields.NAME, null)
        inOrder.verify(editor).apply()
        inOrder.verify(fbDao).writeField(DbEntries.Passengers.Fields.NAME, null)

        inOrder.verify(shPref).edit()
        inOrder.verify(editor).putString(DbEntries.Passengers.Fields.PHONE, null)
        inOrder.verify(editor).apply()
        inOrder.verify(fbDao).writeField(DbEntries.Passengers.Fields.PHONE, null)

        inOrder.verify(shPref).edit()
        inOrder.verify(editor).putString(DbEntries.Car.COMFORT_LEVEL, null)
        inOrder.verify(editor).apply()
        inOrder.verify(fbDao).writeField("${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.COMFORT_LEVEL}", null)

        inOrder.verify(shPref).edit()
        inOrder.verify(editor).putString(DbEntries.Car.BRAND, null)
        inOrder.verify(editor).apply()
        inOrder.verify(fbDao).writeField("${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.BRAND}", null)

        inOrder.verify(shPref).edit()
        inOrder.verify(editor).putString(DbEntries.Car.MODEL, null)
        inOrder.verify(editor).apply()
        inOrder.verify(fbDao).writeField("${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.MODEL}", null)

        inOrder.verify(shPref).edit()
        inOrder.verify(editor).putString(DbEntries.Car.COLOR, null)
        inOrder.verify(editor).apply()
        inOrder.verify(fbDao).writeField("${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.COLOR}", null)

        inOrder.verify(shPref).edit()
        inOrder.verify(editor).putString(DbEntries.Car.NUMBER, null)
        inOrder.verify(editor).apply()
        inOrder.verify(fbDao).writeField("${DbEntries.Drivers.Fields.CAR}/${DbEntries.Car.NUMBER}", null)

        verifyNoMoreInteractions(shPref, editor, fbDao)
    }
}