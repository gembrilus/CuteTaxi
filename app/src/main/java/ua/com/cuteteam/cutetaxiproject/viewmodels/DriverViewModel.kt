package ua.com.cuteteam.cutetaxiproject.viewmodels

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.extentions.distanceTo
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository
import ua.com.cuteteam.cutetaxiproject.repositories.Repository

class DriverViewModel(repository: Repository) : BaseViewModel(repository) {

    private val repo = repository as DriverRepository
    private var mOrder: Order? = null

    init {
        getOrders()
    }

    private val orderListener by lazy {
        object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                p0.toException().printStackTrace()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(Order::class.java)?.let {
                        _activeOrder.value = it
                }
            }
        }
    }

    private val locationObserver by lazy {
        Observer<LatLng>{latLng ->
            mOrder?.driverLocation = Coordinates(latLng.latitude, latLng.longitude)
                mOrder?.let { repo.dao.writeOrder(it) }
        }
    }

    private val _activeOrder = MutableLiveData<Order>()
    val activeOrder: LiveData<Order> get() = _activeOrder

    fun getOrders() = viewModelScope.launch(Dispatchers.Unconfined) {
        if (repo.netHelper.hasInternet) {
            val currentLocation = repo.locationProvider.getLocation()?.toLatLng
            repo.dao.observeOrders { orders ->
                _orders.value = orders
                    .filter { it.comfortLevel == repo.spHelper.carClass }
                    .filter {
                        val lat = it.addressStart?.location?.latitude
                        val lon = it.addressStart?.location?.longitude
                        if (lat != null && lon != null && currentLocation != null) {
                            val latLng = LatLng(lat, lon)
                            val distance = currentLocation distanceTo latLng
                            distance <= 5000
                        } else false
                    }
            }
        }
    }

    private val _orders= MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders
    val isNewOrdersExist = Transformations.map(orders){
        it.size
    }

    fun obtainOrder(order: Order){
        mOrder = order
        mOrder?.driverId = FirebaseAuth.getInstance().currentUser?.uid
        val orderId = mOrder?.orderId
        if (repo.netHelper.hasInternet) {
            repo.dao.writeOrder(order)
            currentLocation.observeForever(locationObserver)
            orderId?.let {
                repo.dao.subscribeForChanges(DbEntries.Orders.TABLE, it, orderListener)
            }
        }
//        repo.spHelper.activeOrderId = orderId
    }

    override fun onCleared() {
        super.onCleared()
        currentLocation.removeObserver(locationObserver)
    }

}