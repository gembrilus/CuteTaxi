package ua.com.cuteteam.cutetaxiproject.livedata

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import javax.crypto.AEADBadTagException

sealed class MapAction {

    class AddMarkers : MapAction()
    class CreateMarkerByCoordinates(val latLng: LatLng, val tag: Any?, val icon: Int) : MapAction()
    class StartMarkerUpdate(val tag: Any?,
                            val icon: Int,
                            val callback: ((Marker?) -> Unit)? = null) : MapAction()
    class StopMarkerUpdate : MapAction()
    class MoveCamera(val latLng: LatLng) : MapAction()
    class BuildRoute(val from: LatLng, val to: LatLng): MapAction()
    class UpdateCameraForRoute() : MapAction()
}
