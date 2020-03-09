package ua.com.cuteteam.cutetaxiproject.ui.main.fragments

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

abstract class MapFragment : SupportMapFragment(),
    OnMapReadyCallback {

    override fun onMapReady(map: GoogleMap?) {
        getMapAsync(this)
    }



}