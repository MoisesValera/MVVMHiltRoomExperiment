package com.mevalera.mvvmhiltroomexperiment.di

import android.content.Context
import androidx.room.Room
import com.mevalera.mvvmhiltroomexperiment.data.source.local.RestaurantDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RestaurantDatabase =
        Room.databaseBuilder(
            context, RestaurantDatabase::class.java,
            "restaurant_db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
}
