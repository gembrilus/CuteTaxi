package ua.com.cuteteam.cutetaxiproject.fragments

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

abstract class MapFragment : SupportMapFragment(),
    OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map
    }

}