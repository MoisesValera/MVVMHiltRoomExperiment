package com.mevalera.mvvmhiltroomexperiment.repository

import com.mevalera.mvvmhiltroomexperiment.data.model.Restaurant
import com.mevalera.mvvmhiltroomexperiment.data.source.repository.RestaurantRepository
import com.mevalera.mvvmhiltroomexperiment.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf


class FakeRepository : RestaurantRepository {

    private val mockRestaurant = mutableListOf(
        Restaurant(
            0,
            "type1",
            "logo1",
            "address1",
            "logo",
            "created at"
        ),
        Restaurant(
            1,
            "type2",
            "logo2",
            "address2",
            "logo",
            "created at"
        )
    )

    private val result: Resource<List<Restaurant>> =
        Resource.Success(mockRestaurant)

    override suspend fun getRestaurants(): Flow<Resource<List<Restaurant>>> = flow {
        this.emit(result)
    }

    override suspend fun deleteRestaurant(story_id: Int): Flow<Unit> {
        return flowOf(Unit)
    }
}
