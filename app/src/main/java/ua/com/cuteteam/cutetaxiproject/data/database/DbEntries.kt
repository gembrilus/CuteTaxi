package ua.com.cuteteam.cutetaxiproject.data.database

object DbEntries {

    object Passengers {
        const val TABLE = "passengers"

        object Fields {
            const val NAME = "name"
            const val PHONE = "phoneNumber"
            const val COMFORT_LEVEL = "comfortLevel"
            const val RATE = "rate"
            const val FAVORITE_ADDRESSES = "addresses"
            const val ORDER_ID = "orderId"
            const val MESSAGE = "message"
            const val TRIPS_COUNT = "tripsCount"
        }
    }

    object Drivers {
        const val TABLE = "drivers"

        object Fields {
            const val NAME = "name"
            const val PHONE = "phoneNumber"
            const val CAR = "car"
            const val RATE = "rate"
            const val DRIVER_STATUS = "status"
            const val LOCATION = "location"
            const val ORDER_ID = "orderId"
            const val MESSAGE = "message"
            const val TRIPS_COUNT = "tripsCount"
        }
    }

    object Orders {
        const val TABLE = "orders"

        object Fields {
            const val ORDER_ID = "orderId"
            const val ORDER_STATUS = "orderStatus"
            const val DRIVER_LOCATION = "driverLocation"
            const val COMFORT_LEVEL = "comfortLevel"
            const val START_ADDRESS = "addressStart"
            const val DEST_ADDRESS = "addressDestination"
            const val DISTANCE = "distance"
            const val ARRIVAL_TIME = "arrivingTime"
            const val PRICE = "price"
            const val CAR_INFO = "carInfo"
            const val DRIVER_RATE = "driverRate"
            const val PASSENGER_RATE = "passengerRate"
            const val TRIP_RATE = "tripRate"
        }
    }

    object Car {
        const val BRAND = "brand"
        const val MODEL = "model"
        const val CAR_CLASS = "carClass"
        const val NUMBER = "regNumber"
        const val COLOR = "color"
    }

    object Address {
        const val LOCATION = "location"
        const val ADDRESS = "address"
    }
}