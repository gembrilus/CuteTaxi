package ua.com.cuteteam.cutetaxiproject.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import org.jetbrains.annotations.NotNull

@Entity
data class Address(
    @Embedded(prefix = "location_" ) var location: Coordinates? = null,
    var address: String? = null
)