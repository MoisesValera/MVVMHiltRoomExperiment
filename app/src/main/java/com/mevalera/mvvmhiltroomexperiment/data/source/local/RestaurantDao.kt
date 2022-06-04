package com.mevalera.mvvmhiltroomexperiment.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mevalera.mvvmhiltroomexperiment.data.model.HiddenRestaurants
import com.mevalera.mvvmhiltroomexperiment.data.model.Restaurant
import kotlinx.coroutines.flow.Flow


@Dao
interface RestaurantDao {
    @Query("SELECT * FROM mvvmhiltroomexperiment LEFT JOIN hidden_restaurants ON story_id = id WHERE id IS NULL")
    fun getAllRestaurants(): Flow<List<Restaurant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurants(restaurants: List<Restaurant>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: Restaurant): Long

    @Query("DELETE FROM mvvmhiltroomexperiment")
    suspend fun deleteAllRestaurants()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun deleteFromFeed(hiddenRestaurants: HiddenRestaurants)
}