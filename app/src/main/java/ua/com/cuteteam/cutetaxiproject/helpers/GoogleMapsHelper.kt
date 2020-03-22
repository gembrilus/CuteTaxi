package ua.com.cuteteam.cutetaxiproject.helpers

import android.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.R
import ua.com.cuteteam.cutetaxiproject.api.RouteProvider

class GoogleMapsHelper(private val googleMap: GoogleMap) {

    init {
        googleMap.isMyLocationEnabled = true
        googleMap.isBuildingsEnabled = false
    }

    fun addMarkers(markers: Map<Int, Marker?>): Map<Int, Marker?> {
        googleMap.clear()
        return markers
            .filterValues { it != null }
            .mapValues {
                googleMap.addMarker(MarkerOptions().position(it.value?.position!!)).apply {
                    tag = it.value?.tag
                    setIcon(BitmapDescriptorFactory.fromResource(it.key))
                }
            }
    }

    fun onCameraMove(callback: ((CameraPosition) -> Unit)) {
        googleMap.setOnCameraMoveListener { callback.invoke(googleMap.cameraPosition) }
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

    fun createMarker(latLng: LatLng, tag: Any?, icon: Int): Marker {
        val marker = googleMap.addMarker(MarkerOptions().position(latLng))
        marker.setIcon(BitmapDescriptorFactory.fromResource(icon))
        marker.tag = tag
        return marker
    }

    fun createOrUpdateMarkerByClick(
        markers: MutableMap<Int, Marker?>,
        tag: Any?,
        icon: Int,
        callback: ((Marker?) -> Unit)? = null
    ) {
        val marker = findBy(markers) { it.value?.tag == tag }

        if (marker != null) {
            updateMarkerByClick(marker.value!!, icon, callback)
        } else {
            createMarkerByClick(tag, icon, callback)
        }
    }

    suspend fun buildRoute(markers: MutableMap<Int, Marker?>) {
        if (markers.count() < 2) return
        val routeProvider = buildRouteProvider(markers) ?: return
        withContext(Dispatchers.Main) {
            val routeSummary = routeProvider.routes()[0]
            googleMap.addPolyline(PolylineOptions()
                .clickable(true)
                .add(*routeSummary.polyline)
                .color(Color.parseColor("#0288d1"))
                .width(15f)
                .startCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.circular_shape_silhouette), 300f))
                .endCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.circular_shape_silhouette), 300f))
            )
            updateCameraForBounds(markers)
        }
    }

    fun removeOnMapClickListener() {
        googleMap.setOnMapClickListener(null)
    }

    private suspend fun updateCameraForBounds(markers: MutableMap<Int, Marker?>) {
        val routeProvider = buildRouteProvider(markers) ?: return
        withContext(Dispatchers.Main) {
            val routeSummary = routeProvider.routes()[0]
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(buildBoundaryForRoute(routeSummary), 100))
        }
    }

    private fun buildBoundaryForRoute(routeSummary: RouteProvider.RouteSummary): LatLngBounds {
        return LatLngBounds.Builder()
            .include(routeSummary.polyline.minBy { it.longitude })
            .include(routeSummary.polyline.minBy { it.latitude })
            .include(routeSummary.polyline.maxBy { it.latitude })
            .include(routeSummary.polyline.maxBy { it.longitude })
            .build()
    }

    private fun stringListOfPoints(markers: MutableMap<Int, Marker?>): List<String> {
        return markers.values
            .filterNotNull()
            .map { "${it.position?.latitude}, ${it.position?.longitude}" }
    }

    private fun buildRouteProvider(markers: MutableMap<Int, Marker?>): RouteProvider? {
        val pointCoordinates = stringListOfPoints(markers)
        if (pointCoordinates.isEmpty()) return null
        return RouteProvider.Builder()
            .addOrigin(pointCoordinates[0])
            .addDestination(pointCoordinates[1])
            .build()
    }

    private fun updateMarkerByClick(
        marker: Marker,
        icon: Int,
        callback: ((Marker?) -> Unit)? = null
    ) {
        marker.remove()
        createMarkerByClick(marker.tag, icon, callback)
    }

    private fun findBy(
        markers: MutableMap<Int, Marker?>,
        predicate: (MutableMap.MutableEntry<Int, Marker?>) -> Boolean
    ): MutableMap.MutableEntry<Int, Marker?>? {
        for (marker in markers) {
            if (predicate(marker)) return marker
        }
        return null
    }

    private fun createMarkerByClick(
        tag: Any?,
        icon: Int,
        callback: ((Marker?) -> Unit)? = null
    ) {
        googleMap.setOnMapClickListener { latLng ->
            val marker = createMarker(latLng, tag, icon)
            callback?.invoke(marker)
        }
    }
}
