package ua.com.cuteteam.cutetaxiproject.data

import androidx.lifecycle.MutableLiveData
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.database.PassengerDao
import ua.com.cuteteam.cutetaxiproject.data.entities.Order

class PassengerRepository(private val dao: PassengerDao = PassengerDao()) {

    val order = MutableLiveData<Order>()

    fun subscribeForOrder() {

    }

    fun makeOrder(order: Order) {
        dao.writeOrder(order)
    }

}