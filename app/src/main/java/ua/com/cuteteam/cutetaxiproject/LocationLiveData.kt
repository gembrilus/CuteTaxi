package ua.com.cuteteam.cutetaxiproject

import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class LocationLiveData : LiveData<Location>() {

    private val locationProvider = LocationProvider()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.locations ?: return
            value = locationResult.lastLocation
        }
    }

    private fun startLocationUpdates() {
        locationProvider.requestLocationUpdates(locationCallback)
    }

    override fun onActive() {
        super.onActive()
        locationProvider.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    value = it
                }
            }
        startLocationUpdates()
    }

    override fun onInactive() {
        super.onInactive()
        locationProvider.removeLocationUpdates(locationCallback)
    }

}
