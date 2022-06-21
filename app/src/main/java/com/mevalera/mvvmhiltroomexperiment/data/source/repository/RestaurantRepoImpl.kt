package com.mevalera.mvvmhiltroomexperiment.data.source.repository

import androidx.room.withTransaction
import com.mevalera.mvvmhiltroomexperiment.data.model.ConferenceWithBody
import com.mevalera.mvvmhiltroomexperiment.data.model.FavoriteItems
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

    override suspend fun getConferences() = networkBoundResource(
        query = {
            restaurantDao.getLastPublishedConferences()
        },
        fetch = {
            api.getLastPublishedConferences()
        },
        saveFetchResult = { apiResult ->
            db.withTransaction {
                restaurantDao.insertWithTimestamp(apiResult.result.conferencias)
            }
        },
        shouldFetch = {
            networkHelper.isNetworkAvailable()
        },
        onFetchFailed = {
        }
    )

    override suspend fun getFilteredConferences(year: String, month: String) = networkBoundResource(
        query = {
            restaurantDao.getFilteredConferences(year + month)
        },
        fetch = {
            api.getConferencesFiltered(year, month)
        },
        saveFetchResult = { apiResult ->
            db.withTransaction {
                restaurantDao.insertConferences(apiResult.result.conferencias)
            }
        },
        shouldFetch = {
            networkHelper.isNetworkAvailable()
        },
        onFetchFailed = {
        }
    )

    override suspend fun getConferenceBody(id: Int) = networkBoundResource(
        query = {
            restaurantDao.getConferenceBody(id)
        },
        fetch = {
            api.getConference(id)
        },
        saveFetchResult = { apiResult ->
            db.withTransaction {
                val conferenceWithBody = ConferenceWithBody(apiResult.result.conferences[0]._id!!,apiResult.result.conferences[0].body)
                restaurantDao.insertConferenceBody(conferenceWithBody)
            }
        },
        shouldFetch = {
            networkHelper.isNetworkAvailable()
        },
        onFetchFailed = {
        }
    )

    override suspend fun bookmarkConference(conference_id: Int): Flow<Unit> {
        return flowOf(
            restaurantDao.bookmarkItem(
                FavoriteItems(
                    id = conference_id
                )
            )
        )
    }
}
