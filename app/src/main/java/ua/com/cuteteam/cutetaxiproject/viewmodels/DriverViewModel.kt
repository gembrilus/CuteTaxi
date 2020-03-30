package ua.com.cuteteam.cutetaxiproject.viewmodels

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.com.cuteteam.cutetaxiproject.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import ua.com.cuteteam.cutetaxiproject.common.arrivalTime
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.extentions.distanceTo
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository
import ua.com.cuteteam.cutetaxiproject.repositories.Repository

class DriverViewModel(
    repository: Repository,
    private val context: Context = AppClass.appContext()
) : BaseViewModel(repository) {
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
        Observer<LatLng> { latLng ->
            val location = Coordinates(latLng.latitude, latLng.longitude)
            mOrder?.orderId?.let {
                repo.dao.updateOrder(it, DbEntries.Orders.Fields.DRIVER_LOCATION, location)
            }
        }
    }

    private val _activeOrder = MutableLiveData<Order>()
    val activeOrder: LiveData<Order> get() = _activeOrder

    private val _orders = MutableLiveData<List<Order>>()
    val orders = MediatorLiveData<List<Order>>()

    val countOfOrders = Transformations.map(orders) {
        it?.size
    }

    fun updateOrders() {
        _orders.value = _orders.value
    }

    private fun getOrders() = viewModelScope.launch(Dispatchers.Unconfined) {
        if (repo.netHelper.hasInternet) {
            val currentLocation = repo.locationProvider.getLocation()?.toLatLng
            repo.dao.observeOrders { list ->
                _orders.value = list
                    .filter {
                        val lat = it.addressStart?.location?.latitude
                        val lon = it.addressStart?.location?.longitude
                        if (lat != null && lon != null && currentLocation != null) {
                            val latLng = LatLng(lat, lon)
                            val distance = currentLocation distanceTo latLng
                            distance <= 5000
                        } else false
                    }
                    .map {
                        it.apply {
                            driverLocation =
                                Coordinates(currentLocation?.latitude, currentLocation?.longitude)
                        }
                    }
            }
            orders.addSource(_orders) { list ->
                orders.value = list?.filter { it.comfortLevel == repo.spHelper.carClass }
            }
            orders.addSource(LocationLiveData()) { loc ->
                orders.value = orders.value?.map {
                    it.apply {
                        driverLocation = Coordinates(loc.latitude, loc.longitude)
                    }
                }
            }
        }
    }

    fun obtainOrder(orderId: String) = viewModelScope.launch {
        if (repo.netHelper.hasInternet) {
            mOrder = repo.dao.getOrder(orderId)?.apply {
                if (orderStatus == OrderStatus.NEW) {
                    orderStatus = OrderStatus.ACTIVE
                    driverId = FirebaseAuth.getInstance().currentUser?.uid
                    arrivingTime = arrivalTime(this)
                    carInfo = with(repo.spHelper) {
                        context.getString(
                            R.string.order_message_from_dirver,
                            carBrand,
                            carModel,
                            carColor,
                            carNumber,
                            this@apply.arrivingTime
                        )
                    }
                }
            }
            repo.dao.writeOrder(mOrder!!)
            subscribeOnOrder(mOrder!!.orderId)
        }
    }

    fun subscribeOnOrder(orderId: String?) {
        repo.spHelper.activeOrderId = orderId
        currentLocation.observeForever(locationObserver)
        orderId?.let {
            repo.dao.subscribeForChanges(DbEntries.Orders.TABLE, it, orderListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentLocation.removeObserver(locationObserver)
        repo.dao.removeAllListeners()
    }

    fun closeOrder() {
        repo.spHelper.activeOrderId = null
        mOrder?.orderId?.let {                  //TODO: Replace with Dao-method
            FirebaseDatabase.getInstance()
                .reference
                .child(DbEntries.Orders.TABLE)
                .child(it).removeEventListener(orderListener)
        }
        currentLocation.removeObserver(locationObserver)
        mOrder = null
    }

    var mapOfVisibility: Map<View, Int>? = null

}