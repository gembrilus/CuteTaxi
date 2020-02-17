package ua.com.cuteteam.cutetaxiproject.data.entities

data class Car (
    var comfortLevel: ComfortLevel? = null,
    val brand: String = "",
    val model: String = "",
    val color: String = "",
    val regNumber: String = ""
)