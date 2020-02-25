package ua.com.cuteteam.cutetaxiproject.ui

import android.location.LocationProvider
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.api.geocoding.GeocodeRequest


class MapFragment : SupportMapFragment(),
    OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener {

    private var mMap: GoogleMap? = null

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            isMyLocationEnabled = true
            setOnMyLocationButtonClickListener(this@MapFragment)

            GlobalScope.launch {

                val place = "Украина, Черкассы"

                val latLng = GeocodeRequest.Builder()
                    .setLanguageResponse("ru")
                    .build()
                    .requestCoordinatesByName(place)
                    .toLatLng()

                withContext(Dispatchers.Main) {
                    val marker = addMarker(
                        MarkerOptions().position(latLng)
                    )

                    moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                    marker.title = place
                }

            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapAsync(this)
    }

    override fun onMyLocationButtonClick(): Boolean {
        return true
    }

    companion object {

        private const val TAG = "CuteTaxi.MapFragment"

    }
}