package ua.com.cuteteam.cutetaxiproject.data.entities

import ua.com.cuteteam.cutetaxiproject.data.User

class Passenger(

    override val uid: String,
    override val name: String,
    override val phoneNumber: String,
    override val rate: Double

) : User