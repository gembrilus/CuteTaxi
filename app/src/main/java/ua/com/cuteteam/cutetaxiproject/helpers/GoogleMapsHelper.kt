package ua.com.cuteteam.cutetaxiproject.helpers

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsHelper(val googleMap: GoogleMap) {

    fun addMarker(latLng: LatLng, title: String? = null) {
        googleMap.addMarker(MarkerOptions().position(latLng).title(title))
    }

    fun moveCameraToMyLocation(latLng: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f), 1000, null)
    }

}