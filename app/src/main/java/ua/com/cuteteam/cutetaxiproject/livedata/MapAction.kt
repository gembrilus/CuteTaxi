package ua.com.cuteteam.cutetaxiproject.livedata

import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.data.MarkerData

sealed class MapAction {
    object UpdateMapObjects : MapAction()
    class CreateMarker(val pair: Pair<String, MarkerData>) : MapAction()
    class RemoveMarker(val tag: String) : MapAction()
    class CreateMarkerByCoordinates(val tag: String, val markerData: MarkerData) : MapAction()
    class AddOnMapClickListener(val callback: ((LatLng) -> Unit)) : MapAction()
    object RemoveOnMapClickListener : MapAction()
    class MoveCamera(val latLng: LatLng) : MapAction()
    class BuildRoute(val from: LatLng, val to: LatLng, val wayPoints: List<LatLng> = emptyList()) :
        MapAction()
    object UpdateCameraForRoute : MapAction()
}
