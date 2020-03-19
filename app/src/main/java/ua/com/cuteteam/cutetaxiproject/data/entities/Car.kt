package ua.com.cuteteam.cutetaxiproject.data.entities

data class Car (
    val brand: String = "",
    val model: String = "",
    var carClass: ComfortLevel? = null,
    val regNumber: String = "",
    val color: String = ""
)
