package com.mevalera.mvvmhiltroomexperiment.data.source.remote.api

import com.mevalera.mvvmhiltroomexperiment.data.model.Hits
import com.mevalera.mvvmhiltroomexperiment.data.model.HitsSingleConferences
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v3/conferencias/?locale=es_ES&&forceupdate=true&cache=false")
    suspend fun getConferencesFiltered(@Query("year") year: String, @Query("month") month: String): Hits

    @GET("v3/conferencias/?locale=es_ES&&forceupdate=true")
    suspend fun getLastPublishedConferences(): Hits

    @GET("v3/conferencias/?f=detail&locale=es_ES&cache=false")
    suspend fun getConference(@Query("id") id: Int): HitsSingleConferences
}