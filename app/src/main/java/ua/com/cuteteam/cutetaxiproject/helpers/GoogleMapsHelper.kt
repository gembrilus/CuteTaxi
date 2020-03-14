package ua.com.cuteteam.cutetaxiproject.helpers

import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsHelper(val googleMap: GoogleMap) {

    init {
        googleMap.isMyLocationEnabled = true
        googleMap.isBuildingsEnabled = false
    }

    fun addMarkers(markers: MutableMap<Int, Marker?>) {
        googleMap.clear()
        googleMap
        for (marker in markers) {
            val addedMarker =
                googleMap.addMarker(MarkerOptions().position(marker.value?.position!!))
            addedMarker.tag = marker.value?.tag
            addedMarker.setIcon(BitmapDescriptorFactory.fromResource(marker.key))
        }
    }

    fun addMarker(markerOptions: MarkerOptions) {
        googleMap.addMarker(markerOptions)
    }

    fun addMarker(latLng: LatLng, title: String? = null) {
        googleMap.addMarker(MarkerOptions().position(latLng).title(title))
    }

    fun moveCameraToLocation(latLng: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f), 1000, null)
    }

    fun createMarker(latLng: LatLng, tag: Any?, icon: Int): Marker? {
        val marker = googleMap.addMarker(MarkerOptions().position(latLng))
        marker.setIcon(BitmapDescriptorFactory.fromResource(icon))
        marker.tag = tag
        return marker
    }

    fun createOrUpdateMarkerByClick(markers: MutableMap<Int, Marker?>, tag: Any?, icon: Int, callback: ((Marker?) -> Unit)? = null) {
        val marker = hasMarker(markers, tag)
        if (marker != null) updateMarkerByClick(marker.value!!, icon, callback)
        else createMarkerByClick(tag,icon, callback)
    }

    fun updateMarkerByClick(
        marker: Marker,
        icon: Int,
        callback: ((Marker?) -> Unit)? = null
    ): Marker? {
        marker.remove()
        return createMarkerByClick(marker.tag, icon)
    }

    fun hasMarker(
        markers: MutableMap<Int, Marker?>,
        tag: Any?
    ): MutableMap.MutableEntry<Int, Marker?>? {
        for (marker in markers) {
            if (marker.value?.tag == tag) return marker
        }
        return null
    }

    fun createMarkerByClick(tag: Any?, icon: Int, callback: ((Marker?) -> Unit)? = null): Marker? {
        var marker: Marker? = null
        googleMap.setOnMapClickListener { latLng ->
            marker?.remove()
            marker = createMarker(latLng, tag, icon)
            callback?.invoke(marker)
        }
        return marker
    }

    fun removeOnMapClickListener() {
        googleMap.setOnMapClickListener(null)
    }
}