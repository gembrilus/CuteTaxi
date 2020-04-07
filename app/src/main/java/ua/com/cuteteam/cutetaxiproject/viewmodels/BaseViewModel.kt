
package ua.com.cuteteam.cutetaxiproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import ua.com.cuteteam.cutetaxiproject.api.RouteProvider
import ua.com.cuteteam.cutetaxiproject.api.geocoding.GeocodeRequest
import ua.com.cuteteam.cutetaxiproject.data.MarkerData
import ua.com.cuteteam.cutetaxiproject.extentions.findBy
import ua.com.cuteteam.cutetaxiproject.helpers.PhoneNumberHelper
import ua.com.cuteteam.cutetaxiproject.helpers.network.NetStatus
import ua.com.cuteteam.cutetaxiproject.livedata.MapAction
import ua.com.cuteteam.cutetaxiproject.livedata.SingleLiveEvent
import ua.com.cuteteam.cutetaxiproject.livedata.ViewAction
import ua.com.cuteteam.cutetaxiproject.providers.LocationProvider
import ua.com.cuteteam.cutetaxiproject.repositories.Repository
import java.util.*

abstract class BaseViewModel(
    private val repository: Repository
) : ViewModel() {

    var currentRoute: RouteProvider.RouteSummary? = null

    var cameraPosition: CameraPosition? = null

    var polylineOptions: PolylineOptions? = null

    val locationProvider: LocationProvider
        get() = repository.locationProvider

    val markers = MutableLiveData(mutableMapOf<String, MarkerData>())

    suspend fun currentCameraPosition(): CameraPosition {
        return cameraPosition ?: countryCameraPosition()
    }

    fun setMarkers(pair: Pair<String, MarkerData>) {
        markers.value = markers.value?.plus(pair)?.toMutableMap()
    }

    fun findMarkerByTag(tag: String): MarkerData? {
        return markers.value?.findBy { it.key == tag }?.value
    }

    private suspend fun countryCameraPosition(): CameraPosition {
        val phone = repository.spHelper.phone
        val country = countryNameByRegionCode(
            PhoneNumberHelper().regionCode(phone!!)
        )
        val countryCoordinates = coordinatesByCountryName(country)
        return CameraPosition.builder().target(countryCoordinates).zoom(6f).build()
    }

    private suspend fun coordinatesByCountryName(countryName: String): LatLng {
        return GeocodeRequest.Builder().build()
            .requestCoordinatesByName(countryName).toLatLng()
    }

    private fun countryNameByRegionCode(regionCode: String): String {
        val local = Locale("", regionCode)
        return local.displayCountry
    }

    private var dialogShowed = false

    fun shouldShowGPSRationale(): Boolean {
        if (dialogShowed || isGPSEnabled) return false

        dialogShowed = true
        return true
    }

    fun updateMapObjects() {
        mapAction.value = MapAction.UpdateMapObjects()
    }

    fun moveCamera(latLng: LatLng) {
        mapAction.value = MapAction.MoveCamera(latLng)
    }

    fun buildRoute() {
        val from = findMarkerByTag("A")?.position
        val to = findMarkerByTag("B")?.position
        if (from == null || to == null) return
        mapAction.value = MapAction.BuildRoute(from, to)
    }

    fun updateCameraForRoute() {
        mapAction.value = MapAction.UpdateCameraForRoute()
    }

    var shouldShowPermissionPermanentlyDeniedDialog = true

    val mapAction = SingleLiveEvent<MapAction>()

    init {
        repository.netHelper.registerNetworkListener()
    }

    val viewAction = SingleLiveEvent<ViewAction>()

    val isGPSEnabled get() = repository.locationProvider.isGPSEnabled()

    val shouldStartService: Boolean get() = repository.spHelper.isServiceEnabled

    val netStatus: LiveData<NetStatus> = repository.netHelper.netStatus

    val activeOrderId: LiveData<String?> = SingleLiveEvent<String?>().apply {
        value = repository.spHelper.activeOrderId
    }

    val currentLocation
        get() = Transformations.map(repository.observableLocation) {
            LatLng(it.latitude, it.longitude)
        }

    fun signOut() = repository.signOut()
    fun getSignInUser() = repository.getUser()

    fun changeRole(role: Boolean) {
        this.role.value = role
    }

    private var _isChecked = false
    val isChecked get() = _isChecked

    private val roleObserver = Observer<Boolean> {
        _isChecked = it
        repository.spHelper.role = it
    }

    private val role = MutableLiveData(repository.spHelper.role).apply {
        observeForever(roleObserver)
    }

    override fun onCleared() {
        super.onCleared()
        repository.netHelper.unregisterNetworkListener()
        role.removeObserver(roleObserver)
    }
}