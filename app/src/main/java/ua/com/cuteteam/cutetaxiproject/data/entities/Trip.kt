package ua.com.cuteteam.cutetaxiproject.data.entities

data class Trip(
    val driverUid: String? = null,
    val passengerUid: String? = null,
    val timeStart: Long? = null,
    val timeFinish: Long? = null,
    val coordinatesStart: Coordinates? = null,
    val coordinatesFinish: Coordinates? = null,
    val addressStart: String? = null,
    val addressDestination: String? = null,
    val distance: Double? = null,
    val price: Double? = null
)