package ua.com.cuteteam.cutetaxiproject.data.room_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.com.cuteteam.cutetaxiproject.data.room_database.entities.FavoriteOrder

@Dao
interface FavoriteOrdersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: FavoriteOrder)

    @Query("select distinct address_start_address from orders")
    fun getAddresses(): LiveData<List<String>>

    @Query("select * from orders")
    fun getFavoritesList(): LiveData<List<FavoriteOrder>>
}