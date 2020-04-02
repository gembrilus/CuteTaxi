package ua.com.cuteteam.cutetaxiproject.data.entities

data class Order(
    var orderId: String? = null,
    var driverId: String? = null,
    var passengerId: String? = null,
    var orderStatus: OrderStatus = OrderStatus.NEW,
    var driverLocation: Coordinates? = null,
    var comfortLevel: ComfortLevel = ComfortLevel.STANDARD,
    var addressStart: Address? = null,
    var addressDestination: Address? = null,
    var distance: Double? = null,
    var arrivingTime: Long? = null,
    var price: Double? = null,
    var carInfo: String? = null,
    var driverRate: Double? = null,
    var passengerRate : Double? = null,
    var tripRate: Double? = null
) {
    fun isReady(): Boolean {
        return (this.passengerId != null &&
                this.addressStart?.location != null &&
                this.addressDestination?.location != null)
    }
}