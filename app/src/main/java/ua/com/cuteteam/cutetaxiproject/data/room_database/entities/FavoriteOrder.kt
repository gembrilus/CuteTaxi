package ua.com.cuteteam.cutetaxiproject.data.room_database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.com.cuteteam.cutetaxiproject.data.entities.Address

@Entity(tableName = "orders")
data class FavoriteOrder (
    @PrimaryKey(autoGenerate = true) var id: Long,
    @Embedded(prefix = "address_start_")
    var addressStart: Address? = null,
    @Embedded(prefix = "address_dest_")
    var addressDestination: Address? = null
)