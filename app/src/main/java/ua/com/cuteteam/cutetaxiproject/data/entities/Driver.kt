package ua.com.cuteteam.cutetaxiproject.data.entities

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import ua.com.cuteteam.cutetaxiproject.data.User

@IgnoreExtraProperties
data class Driver(
    override var name: String? = "",
    override var phoneNumber: String? = "",
    var car: Car? = null,
    override var rate: Double? = 0.0,
    var status: DriverStatus? = DriverStatus.OFFLINE,
    var location: Coordinates? = null,
    var orderId: String? = null,
    var message: String? = null
) : User