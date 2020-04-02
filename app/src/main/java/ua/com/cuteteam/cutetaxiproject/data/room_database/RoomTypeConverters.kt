package ua.com.cuteteam.cutetaxiproject.data.room_database

import androidx.room.TypeConverter
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.entities.Coordinates


class RoomTypeConverters {

    @TypeConverter
    fun fromCoordinates(coord: Coordinates?): String? {
        return if (coord != null) {
            "${coord.latitude}|${coord.longitude}"
        } else null
    }

    @TypeConverter
    fun toCoordinates(value: String): Coordinates? {
        val valuesArray = value.split("|")
        return if (valuesArray.isNotEmpty() &&
            valuesArray.size == 2
        ) {
            Coordinates(valuesArray[0].toDouble(), valuesArray[1].toDouble())
        } else null

    }

    @TypeConverter
    fun fromAddress(address: Address): String? {
        return if (address.location?.latitude != null &&
            address.location?.longitude != null &&
            address.address != null
        ) {
            val lat = address.location!!.latitude
            val lon = address.location!!.longitude
            val addr = address.address
            "$lat|$lon|$addr"
        } else if (address.location?.latitude != null &&
            address.location?.longitude != null
        ) {
            val lat = address.location!!.latitude
            val lon = address.location!!.longitude
            "$lat|$lon"
        } else null
    }

    @TypeConverter
    fun toAddress(value: String): Address? {
        val valuesArray = value.split("|")

        return if (valuesArray.isNotEmpty() &&
            valuesArray.size == 3
        ) {
            Address(
                Coordinates(valuesArray[0].toDoubleOrNull(), valuesArray[1].toDoubleOrNull()),
                valuesArray[2]
            )
        } else if (valuesArray.isNotEmpty() && valuesArray.size == 2) {
            Address(
                Coordinates(valuesArray[0].toDoubleOrNull(), valuesArray[1].toDoubleOrNull()),
                null
            )
        } else null
    }
}