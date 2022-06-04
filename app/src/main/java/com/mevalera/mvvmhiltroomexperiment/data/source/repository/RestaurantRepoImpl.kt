package com.mevalera.mvvmhiltroomexperiment.data.source.repository

import androidx.room.withTransaction
import com.mevalera.mvvmhiltroomexperiment.data.model.HiddenRestaurants
import com.mevalera.mvvmhiltroomexperiment.data.source.local.RestaurantDatabase
import com.mevalera.mvvmhiltroomexperiment.data.source.remote.api.ApiService
import com.mevalera.mvvmhiltroomexperiment.util.NetworkHelper
import com.mevalera.mvvmhiltroomexperiment.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


class RestaurantRepoImpl @Inject constructor(
    private val api: ApiService,
    private val db: RestaurantDatabase,
    private val networkHelper: NetworkHelper
) : RestaurantRepository {

    private val restaurantDao = db.restaurantDao()

    override suspend fun getRestaurants() = networkBoundResource(
        query = {
            restaurantDao.getAllRestaurants()
        },
        fetch = {
            api.getRestaurants()
        },
        saveFetchResult = { result ->
            db.withTransaction {
                restaurantDao.deleteAllRestaurants()
                restaurantDao.insertRestaurants(result.hits)
            }
        },
        shouldFetch = {
            networkHelper.isNetworkAvailable()
        },
        onFetchFailed = {
        }
    )

    override suspend fun deleteRestaurant(story_id: Int): Flow<Unit> {
        return flowOf(restaurantDao.deleteFromFeed(
            HiddenRestaurants(
                id = story_id
            )
        ))
    }

}
