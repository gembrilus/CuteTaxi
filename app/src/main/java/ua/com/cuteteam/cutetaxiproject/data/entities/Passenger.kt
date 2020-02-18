package ua.com.cuteteam.cutetaxiproject.data.entities

import ua.com.cuteteam.cutetaxiproject.data.User

data class Passenger(
    override var name: String? = "",
    override var phoneNumber: String? = "",
    override var rate: Double? = 0.0,
    val addresses: List<String> = emptyList(),
    var orderId: String? = null
) : User