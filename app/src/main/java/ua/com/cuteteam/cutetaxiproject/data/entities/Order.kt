package ua.com.cuteteam.cutetaxiproject.data.entities

data class Order (
    var orderId: String? = null,
    var driverLocation: Coordinates? = null,
    var comfortLevel: ComfortLevel = ComfortLevel.STANDARD,
    var coordinatesStart: Coordinates? = null,
    var coordinatesFinish: Coordinates? = null,
    var addressStart: String? = null,
    var addressDestination: String? = null,
    var distance: Double? = null,
    var price: Double? = null
)