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

    val shouldStartService: Boolean get() = repository.spHelper.isServiceEnabled

    val netStatus: LiveData<NetStatus> = repository.netHelper.netStatus

    val activeOrderId: LiveData<String?> = SingleLiveEvent<String?>().apply {
        value = repository.spHelper.activeOrderId
    }

    private val _currentLocation = LocationLiveData()
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