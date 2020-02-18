package ua.com.cuteteam.cutetaxiproject.data.database

interface Entry {
    val field: String
}

enum class DriverEntry(override val field: String) : Entry {
    NAME("name"),
    PHONE_NUMBER("phoneNumber"),
    RATE("rate"),
    CAR("car"),
    LOCATION("location"),
    STATUS("status"),
    ORDER_ID("orderId")
}

enum class PassengerEntry(override val field: String) : Entry {
    NAME("name"),
    PHONE_NUMBER("phoneNumber"),
    RATE("rate"),
    ADDRESSES("addresses"),
    ORDER_ID("orderId")
}