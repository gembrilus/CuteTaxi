package ua.com.cuteteam.cutetaxiproject.viewmodels

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.common.network.NetStatus
import ua.com.cuteteam.cutetaxiproject.livedata.SingleLiveEvent
import ua.com.cuteteam.cutetaxiproject.repositories.Repository

open class BaseViewModel(private val repository: Repository) : ViewModel() {

    var shouldShowPermissionPermanentlyDeniedDialog = true

    init {
        repository.netHelper.registerNetworkListener()
    }

    val isGPSEnabled get() = repository.locationProvider.isGPSEnabled()

    val netStatus: LiveData<NetStatus> = repository.netHelper.netStatus

    val activeOrderId: LiveData<String?> = SingleLiveEvent<String?>().apply {
        value = repository.spHelper.activeOrderId
    }

    /**
     * Return location as Address class with coordinates and an address name
     */
    fun getSingleLocation() = liveData {
        val loc = repository.locationProvider.getLocation()
        if (loc != null) {
            val latLng = LatLng(loc.latitude, loc.longitude)
            val address = repository.geocoder.build()
                .requestNameByCoordinates(latLng)
                .toAddress()

            emit(address)
        }
    }

    private val _currentLocation = LocationLiveData()

    /**
     * Periodical observable location in [LatLng]
     */
    val currentLocation
        get() = Transformations.map(_currentLocation) {
            LatLng(it.latitude, it.longitude)
        }

    /**
     * Return address name by LatLng argument or string of "latitude,longitude"
     */
    fun geocodeLocation(param: Any) = liveData {
        val address = when (param) {
            is String -> {
                repository.geocoder.build()
                    .requestNameByCoordinates(param)
                    .toName()
            }
            is LatLng -> {
                repository.geocoder.build()
                    .requestNameByCoordinates(param)
                    .toName()
            }
            else -> ""
        }
        emit(address)
    }

    /**
     * Build route by origin and destination addresses. It can be a name or string of "latitude,longitude"
     * Return only the one smallest route
     */
    fun buildRoute(orig: String, dest: String, wayPoints: List<LatLng>? = null) = liveData {
        val points = repository.routeBuilder.apply {
                addDestination(orig)
                addOrigin(dest)
                wayPoints?.let {
                    it.forEach {
                        addWayPoint("${it.latitude},${it.longitude}")
                    }
                }
            }
            .build()
            .routes()[0]
        emit(points)
    }

    private val _role = MutableLiveData(repository.spHelper.role)

    fun changeRole(role: Boolean) {
        _role.value = role
        repository.spHelper.role = role
    }

    val isChecked = _role.value ?: false

    override fun onCleared() {
        super.onCleared()
        repository.netHelper.unregisterNetworkListener()
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun getViewModelFactory(repository: Repository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                when {
                    modelClass.isAssignableFrom(BaseViewModel::class.java) -> {
                        BaseViewModel(
                            repository
                        ) as T
                    }
                    modelClass.isAssignableFrom(PassengerViewModel::class.java) -> {
                        PassengerViewModel(
                            repository
                        ) as T
                    }
                    modelClass.isAssignableFrom(DriverViewModel::class.java) -> {
                        DriverViewModel(
                            repository
                        ) as T
                    }
                    else -> throw IllegalArgumentException("Wrong class name")
                }
        }
    }
}