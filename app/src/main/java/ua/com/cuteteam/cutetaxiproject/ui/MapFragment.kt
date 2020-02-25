package ua.com.cuteteam.cutetaxiproject.ui

import android.location.LocationProvider
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.com.cuteteam.cutetaxiproject.api.RouteProvider
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

        var isFirstClick = true
        var orig = ""
        var dest = ""

        mMap?.setOnMapLongClickListener { latLng ->

            mMap?.addMarker(MarkerOptions().position(latLng))

            val lat = latLng.latitude
            val lng = latLng.longitude

            GlobalScope.launch {

                withContext(Dispatchers.Main) {
                    if (isFirstClick) {
                        orig = "$lat,$lng"
                        isFirstClick = false
                    } else {
                        dest = "$lat,$lng"
                        isFirstClick = true
                    }
                }

                if (isFirstClick) {
                    val routeProvider = RouteProvider.Builder()   // Пример построения маршрута
                        .addOrigin(orig)
                        .addDestination(dest)
                        .build()

                    GlobalScope.launch(Dispatchers.Main) {
                        val value =
                            routeProvider.routes()
                        mMap?.addPolyline(
                            PolylineOptions()
                                .clickable(true)
                                .add(*value[0].polyline)
                        )
                    }
                }

            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapAsync(this)
    }

    /*    override fun onResume() {
        super.onResume()

        GlobalScope.launch {

            val geocode = GeocodeRequest.Builder()
                .build()
                .requestCoordinatesByName("Черкассы, ул. Шевченкоб 237")  // Пример Геокодинга

            val geocode2 = GeocodeRequest.Builder()
                .setLanguageResponse("us")
                .setRegion("us")
                .build()
                .requestNameByCoordinates("32.1231231,51.3333323")

            withContext(Dispatchers.Main) {
                InfoDialog.show(
                    supportFragmentManager, "Координаты", geocode.toLatLng().toString() + "\n" +
                            geocode2.toName()
                )
            }

        }

    }*/

    override fun onMyLocationButtonClick(): Boolean {
        return true
    }

    companion object {

        private const val TAG = "CuteTaxi.MapFragment"

    }
}