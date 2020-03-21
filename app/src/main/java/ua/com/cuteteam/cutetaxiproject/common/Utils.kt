@file: JvmName(name = "Utils")

package ua.com.cuteteam.cutetaxiproject.common

import ua.com.cuteteam.cutetaxiproject.data.entities.ComfortLevel

fun calculatePrice(tax: Double, distance: Double, level: ComfortLevel): Double {
    val koef = when(level){
        ComfortLevel.STANDARD -> 1.0
        ComfortLevel.COMFORT -> 1.25
        ComfortLevel.ECO -> 1.5
    }
    return tax * distance * koef
}

fun checkDriversRoute() {

}