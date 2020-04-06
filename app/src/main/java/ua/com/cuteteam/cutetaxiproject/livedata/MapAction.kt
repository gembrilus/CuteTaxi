package ua.com.cuteteam.cutetaxiproject.livedata

import com.google.android.gms.maps.model.LatLng
import ua.com.cuteteam.cutetaxiproject.data.MarkerData

sealed class MapAction {
    class UpdateMapObjects : MapAction()
    class CreateMarkerByCoordinates(val tag: String, val markerData: MarkerData) : MapAction()
    class CreateMarkerByClick(val tag: String,
                              val icon: Int,
                              val callback: ((Pair<String, MarkerData>) -> Unit)? = null) : MapAction()
    class StopMarkerUpdate : MapAction()
    class MoveCamera(val latLng: LatLng) : MapAction()
    class BuildRoute(val from: LatLng, val to: LatLng): MapAction()
    class UpdateCameraForRoute : MapAction()
}
