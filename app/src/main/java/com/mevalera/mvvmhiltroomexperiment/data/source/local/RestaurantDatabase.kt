package com.mevalera.mvvmhiltroomexperiment.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mevalera.mvvmhiltroomexperiment.data.model.HiddenRestaurants
import com.mevalera.mvvmhiltroomexperiment.data.model.Restaurant


@Database(entities = [Restaurant::class, HiddenRestaurants::class], version = 1)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
}