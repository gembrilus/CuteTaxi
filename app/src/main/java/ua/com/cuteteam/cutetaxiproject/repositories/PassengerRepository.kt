package ua.com.cuteteam.cutetaxiproject.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.firebase_database.PassengerDao
import ua.com.cuteteam.cutetaxiproject.data.room_database.OrdersDatabase
import ua.com.cuteteam.cutetaxiproject.data.room_database.entities.FavoriteOrder
import ua.com.cuteteam.cutetaxiproject.extentions.mutation

class PassengerRepository : Repository() {
    val dao = PassengerDao()

    private val database = OrdersDatabase.invoke(appContext)
    private val favoriteOrdersDao = database.favoriteOrdersDao()

    val activeOrder = MutableLiveData<Order?>()

    fun makeOrder(order: Order) {
        val newOrderId = dao.writeOrder(order).key

        if (newOrderId != null) {
            dao.subscribeForOrder(
                newOrderId,
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("Subscribe for changes", error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        activeOrder.mutation { it.value = snapshot.getValue(Order::class.java) }
                    }
                })
        }
    }

    fun deleteListener() {
        val activeOrderId = activeOrder.value?.orderId

        if (activeOrderId != null) {
            dao.removeOrdersListeners(activeOrderId)
        }
    }

    fun addToFavorites(order: FavoriteOrder) = ioScope.launch {
        favoriteOrdersDao.insertOrder(order)
    }

    fun getFavoriteAddresses(): LiveData<List<String>> {
        return favoriteOrdersDao.getAddresses()
    }

    fun getFavoriteOrders(): LiveData<List<FavoriteOrder>> {
        return favoriteOrdersDao.getFavoritesList()
    }
}
