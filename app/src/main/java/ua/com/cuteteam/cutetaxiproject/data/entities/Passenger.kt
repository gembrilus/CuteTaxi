package ua.com.cuteteam.cutetaxiproject.data.entities

import ua.com.cuteteam.cutetaxiproject.data.User

data class Passenger(
    override var name: String? = "",
    override var phoneNumber: String? = "",
    override var tripsCount: Int? = null,
    var comfortLevel: ComfortLevel = ComfortLevel.STANDARD,
    override var rate: Double? = 0.0,
    var addresses: List<Address> = emptyList(),
    var orderId: String? = null,
    var message: String? = null
) : User

