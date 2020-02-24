package ua.com.cuteteam.cutetaxiproject.data.entities

data class Trip(
    val driverUid: String? = null,
    val passengerUid: String? = null,
    val timeStart: Long? = null,
    val timeFinish: Long? = null,
    val addressStart: Address? = null,
    val addressDestination: Address? = null,
    val distance: Double? = null,
    val price: Double? = null
)