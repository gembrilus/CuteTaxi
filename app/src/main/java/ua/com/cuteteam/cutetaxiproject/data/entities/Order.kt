package ua.com.cuteteam.cutetaxiproject.data.entities

data class Order (
    var orderId: String? = null,
    var driverLocation: Coordinates? = null,
    var comfortLevel: ComfortLevel = ComfortLevel.STANDARD,
    var addressStart: Address? = null,
    var addressDestination: Address? = null,
    var distance: Double? = null,
    var price: Double? = null,
    var arrivingTime: Double,
    var orderStatus: OrderStatus
)