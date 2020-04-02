package ua.com.cuteteam.cutetaxiproject.data.room_database.entities

import androidx.room.*
import org.jetbrains.annotations.NotNull
import ua.com.cuteteam.cutetaxiproject.data.entities.Address
import ua.com.cuteteam.cutetaxiproject.data.room_database.RoomTypeConverters

@Entity(tableName = "orders")
@TypeConverters(RoomTypeConverters::class)
data class FavoriteOrder (
    @PrimaryKey(autoGenerate = true) var id: Long,
    @Embedded(prefix = "address_start_") @NotNull var addressStart: Address? = null,
    @Embedded(prefix = "address_dest_") @NotNull var addressDestination: Address? = null
)