package ua.com.cuteteam.cutetaxiproject.data.database

interface Entry {
    val field: String
}

enum class DriverEntry(override val field: String) :
    Entry {
    NAME("name"),
    PHONE_NUMBER("phone_number"),
    RATE("rate"),
    CAR("car"),
    LOCATION("location"),
    STATUS("status")
}

enum class PassengerEntry(override val field: String) : Entry {
    NAME("name"),
    PHONE_NUMBER("phone_number"),
    RATE("rate")
}