package ua.com.cuteteam.cutetaxiproject.viewmodels

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.helpers.network.NetStatus
import ua.com.cuteteam.cutetaxiproject.livedata.SingleLiveEvent
import ua.com.cuteteam.cutetaxiproject.livedata.ViewAction
import ua.com.cuteteam.cutetaxiproject.repositories.Repository

open class BaseViewModel(private val repository: Repository) : ViewModel() {

    var shouldShowPermissionPermanentlyDeniedDialog = true

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
