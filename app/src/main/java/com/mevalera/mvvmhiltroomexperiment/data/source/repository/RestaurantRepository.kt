package com.mevalera.mvvmhiltroomexperiment.data.source.repository

import com.mevalera.mvvmhiltroomexperiment.data.model.Restaurant
import com.mevalera.mvvmhiltroomexperiment.util.Resource
import kotlinx.coroutines.flow.Flow


interface RestaurantRepository {
    suspend fun getRestaurants(): Flow<Resource<List<Restaurant>>>
    suspend fun deleteRestaurant(story_id: Int): Flow<Unit>
}
