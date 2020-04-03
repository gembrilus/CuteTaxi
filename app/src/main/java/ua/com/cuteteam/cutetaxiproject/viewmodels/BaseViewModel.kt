package ua.com.cuteteam.cutetaxiproject.viewmodels

import androidx.lifecycle.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import ua.com.cuteteam.cutetaxiproject.api.RouteProvider
import ua.com.cuteteam.cutetaxiproject.extentions.findBy
import ua.com.cuteteam.cutetaxiproject.livedata.LocationLiveData
import ua.com.cuteteam.cutetaxiproject.helpers.network.NetStatus
import ua.com.cuteteam.cutetaxiproject.livedata.MapAction
import ua.com.cuteteam.cutetaxiproject.livedata.SingleLiveEvent
import ua.com.cuteteam.cutetaxiproject.providers.LocationProvider
import ua.com.cuteteam.cutetaxiproject.repositories.Repository

open class BaseViewModel(private val repository: Repository) : ViewModel() {

    var currentRoute: RouteProvider.RouteSummary? = null

    var cameraPosition: CameraPosition? = null

    val locationProvider: LocationProvider
        get() = repository.locationProvider

    var markers = MutableLiveData(mutableMapOf<Int, Marker?>())

    fun replaceMarkers(newMarkers: Map<Int, Marker?>) {
        markers.value?.clear()
        markers.value?.plusAssign(newMarkers)
    }

    fun findMarkerByTag(tag: String): Marker? {
        return markers.value?.findBy { it.value?.tag == tag }?.value
    }

    fun setMarkers(newMarkers: Map<Int, Marker?>) {
        markers.value = newMarkers.toMutableMap()
    }

    fun setMarker(key: Int, value: Marker?) {
        markers.value = markers.value?.plus(key to value)?.toMutableMap()
    }

    fun markerPositions(): List<LatLng> {
        return markers.value?.map {
            it.value?.position
        }?.filterNotNull() ?: emptyList()
    }

    private var dialogShowed = false

    fun shouldShowGPSRationale(): Boolean {
        if (dialogShowed || repository.locationProvider.isGPSEnabled()) return false

        dialogShowed = true
        return true
    }

    fun addMarkers() {
        mapAction.value = MapAction.AddMarkers()
    }

    fun moveCamera(latLng: LatLng) {
        mapAction.value = MapAction.MoveCamera(latLng)
    }

    fun buildRoute(from: Marker?, to: Marker?) {
        if (from == null || to == null) return
        mapAction.value = MapAction.BuildRoute(from.position, to.position)
    }

    fun updateCameraForRoute() {
        mapAction.value = MapAction.UpdateCameraForRoute()
    }

    var shouldShowPermissionPermanentlyDeniedDialog = true

    val mapAction = SingleLiveEvent<MapAction>()

    init {
        repository.netHelper.registerNetworkListener()
    }

    val isGPSEnabled get() = repository.locationProvider.isGPSEnabled()

    val shouldStartService: Boolean get() = repository.spHelper.isServiceEnabled

    val netStatus: LiveData<NetStatus> = repository.netHelper.netStatus

    val activeOrderId: LiveData<String?> = SingleLiveEvent<String?>().apply {
        value = repository.spHelper.activeOrderId
    }

    private val _currentLocation =
        LocationLiveData()
    val currentLocation
        get() = Transformations.map(_currentLocation) {
            LatLng(it.latitude, it.longitude)
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