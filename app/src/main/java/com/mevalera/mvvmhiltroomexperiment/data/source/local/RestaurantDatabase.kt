package com.mevalera.mvvmhiltroomexperiment.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mevalera.mvvmhiltroomexperiment.data.model.Conference
import com.mevalera.mvvmhiltroomexperiment.data.model.FavoriteItems


@Database(entities = [Conference::class, FavoriteItems::class], version = 1)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
}