package ua.com.cuteteam.cutetaxiproject

import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.Task
import ua.com.cuteteam.cutetaxiproject.application.AppClass
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationProvider(private val context: Context = AppClass.appContext()) {

    private val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val fusedLocationProviderClient = FusedLocationProviderClient(context)

    val lastLocation: Task<Location> = fusedLocationProviderClient.lastLocation

    fun requestLocationUpdates(locationCallback: LocationCallback): Task<Void>? {
        return fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun removeLocationUpdates(locationCallback: LocationCallback) {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    suspend fun getLocation() : Location? {
        return suspendCoroutine {
            requestLocationUpdates( object : LocationCallback() {
                override fun onLocationResult(result: LocationResult?) {
                    it.resume(result?.lastLocation)
                    removeLocationUpdates(this)
                }
            })
        }
    }

    fun isGPSEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
