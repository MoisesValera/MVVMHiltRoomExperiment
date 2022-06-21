package com.mevalera.mvvmhiltroomexperiment.data.source.repository

import com.mevalera.mvvmhiltroomexperiment.data.model.Conference
import com.mevalera.mvvmhiltroomexperiment.data.model.ConferenceWithBody
import com.mevalera.mvvmhiltroomexperiment.util.Resource
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    suspend fun getFilteredConferences(year: String, month: String): Flow<Resource<List<Conference>>>
    suspend fun getConferences(): Flow<Resource<List<Conference>>>
    suspend fun getConferenceBody(conference_id: Int): Flow<Resource<ConferenceWithBody>>
    suspend fun bookmarkConference(conference_id: Int): Flow<Unit>
}