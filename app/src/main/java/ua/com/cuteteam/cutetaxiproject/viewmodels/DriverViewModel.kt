package ua.com.cuteteam.cutetaxiproject.viewmodels

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
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.common.arrivalTime
import ua.com.cuteteam.cutetaxiproject.data.database.DbEntries
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates
import ua.com.cuteteam.cutetaxiproject.data.entities.Order
import ua.com.cuteteam.cutetaxiproject.data.entities.OrderStatus
import ua.com.cuteteam.cutetaxiproject.extentions.distanceTo
import ua.com.cuteteam.cutetaxiproject.extentions.toLatLng
import ua.com.cuteteam.cutetaxiproject.livedata.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.livedata.MapAction
import ua.com.cuteteam.cutetaxiproject.repositories.DriverRepository

class DriverViewModel(
    private val repo: DriverRepository
) : BaseViewModel(repo) {
    private var mOrder: Order? = null

    init {
        getOrders()
    }

    var mapOfVisibility: Map<View, Int>? = null

    fun buildRoute(from: LatLng, to: LatLng, wayPoints: List<LatLng>) {
        mapAction.value = MapAction.BuildRoute(from, to, wayPoints)
    }

    private val orderListener by lazy {
        object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                p0.toException().printStackTrace()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                mOrder = snapshot.getValue(Order::class.java)?.also {
                    _activeOrder.value = it
                }
            }
        }
    }

    private val locationObserver by lazy {
        Observer<LatLng> {
            updateOrder(
                DbEntries.Orders.Fields.DRIVER_LOCATION,
                Coordinates(it.latitude, it.longitude)
            )
        }
    }

    private val countObserver by lazy {
        object : Observer<Int?> {
            override fun onChanged(count: Int?) {
                count ?: return
                _openHomeOrOrders.value = (count == 0)
                countOfOrders.removeObserver(this)
            }
        }
    }
    private val _openHomeOrOrders = MutableLiveData<Boolean>()
    val openHomeOrOrders: LiveData<Boolean> get() = _openHomeOrOrders
    fun openHomeOrOrders() = countOfOrders.observeForever(countObserver)

    private val _activeOrder = MutableLiveData<Order>()
    val activeOrder: LiveData<Order> get() = _activeOrder

    private val _orders = MutableLiveData<List<Order>>()
    val orders = MediatorLiveData<List<Order>>().apply {
        addSource(_orders) { list ->
            value = list?.filter { it.comfortLevel == repo.spHelper.carClass }
        }
        addSource(LocationLiveData()) { loc ->
            value = value?.map {
                it.apply {
                    driverLocation = Coordinates(loc.latitude, loc.longitude)
                }
            }
        }
    }

    private val _countOfOrders = MediatorLiveData<Int?>().apply {
        addSource(orders) { list ->
            value = list?.size
        }
    }
    val countOfOrders: LiveData<Int?> get() = _countOfOrders

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
        }
    }

    fun obtainOrder(orderId: String) = viewModelScope.launch {
        if (repo.netHelper.hasInternet) {
            mOrder = repo.dao.getOrder(orderId)?.apply {
                if (orderStatus == OrderStatus.NEW) {
                    orderStatus = OrderStatus.ACCEPTED
                    driverId = FirebaseAuth.getInstance().currentUser?.uid
                    arrivingTime = arrivalTime(this)
                    carInfo = with(repo.spHelper) {
                        repo.appContext.getString(
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

    fun rate(rating: Float) {
        updateOrder(DbEntries.Orders.Fields.PASSENGER_RATE, rating.toDouble())
        calculateTripRating()
    }

    fun closeOrder() {
        _activeOrder.value = null
        mOrder?.orderId?.let {                  //TODO: Replace with Dao-method
            FirebaseDatabase.getInstance()
                .reference
                .child(DbEntries.Orders.TABLE)
                .child(it).removeEventListener(orderListener)
        }
        currentLocation.removeObserver(locationObserver)
        repo.spHelper.activeOrderId = null
        mOrder = null
    }

    fun updateOrder(field: String, value: Any) {
        mOrder?.orderId?.let {
            repo.dao.updateOrder(it, field, value)
        }
    }

    private fun calculateTripRating() = viewModelScope.launch {

        val driver = FirebaseAuth.getInstance().currentUser?.uid?.let { repo.dao.getUser(it) }
        val rating = mOrder?.driverRate
        val currentRating = driver?.rate ?: 0.0
        val currentCountOfTrips = driver?.tripsCount ?: 0

        val newCountOfTrips = currentCountOfTrips + 1
        val newRating = rating?.let { (currentCountOfTrips * currentRating + it) / newCountOfTrips }

        driver?.let { user ->
            user.tripsCount = newCountOfTrips
            newRating?.let { user.rate = it }
            repo.dao.writeUser(user)
        }

    }

    override fun onCleared() {
        super.onCleared()
        currentLocation.removeObserver(locationObserver)
        repo.dao.removeAllListeners()
    }
}
