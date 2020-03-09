package ua.com.cuteteam.cutetaxiproject.api.adapters

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import ua.com.cuteteam.cutetaxiproject.api.directions.Location

/**
 * Moshi adapter class for [LatLng]. Use [Location] class
 */
class LatLngAdapter {

    @ToJson
    fun toJson(latLng: LatLng): Location {
        return Location(
            latLng.latitude,
            latLng.longitude
        )
    }

    @FromJson
    fun fromJson(json: Location): LatLng {
        return LatLng(
            json.latitude,
            json.longitude
        )
    }

}