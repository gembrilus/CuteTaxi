package ua.com.cuteteam.cutetaxiproject.data.entities

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import ua.com.cuteteam.cutetaxiproject.data.User

@IgnoreExtraProperties
data class Driver(
    override var name: String? = "",
    override var phoneNumber: String? = "",
    var car: Car? = null,
    override var rate: Double? = 0.0,
    var status: Status? = Status.OFFLINE,
    var location: LatLng? = null,
    var orderId: String? = null
) : User {

    @Exclude
    fun toMap(): Map<String, Any?> = mapOf(
        "name" to name,
        "phoneNumber" to phoneNumber,
        "rate" to rate,
        "status" to status,
        "location" to location
    )
}