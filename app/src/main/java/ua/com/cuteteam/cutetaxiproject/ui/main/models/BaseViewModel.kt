package ua.com.cuteteam.cutetaxiproject.ui.main.models

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.common.network.NetStatus
import ua.com.cuteteam.cutetaxiproject.repositories.PassengerRepository
import ua.com.cuteteam.cutetaxiproject.viewmodels.PassengerViewModel

open class BaseViewModel(private val repository: PassengerRepository) : ViewModel() {

    var shouldShowPermissionPermanentlyDeniedDialog = true

    init {
        repository.netHelper.registerNetworkListener()
    }

    val isGPSEnabled get() = repository.locationProvider.isGPSEnabled()

    val netStatus: LiveData<NetStatus> = repository.netHelper.netStatus

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
        fun getViewModelFactory(repository: PassengerRepository) = object : ViewModelProvider.Factory {
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