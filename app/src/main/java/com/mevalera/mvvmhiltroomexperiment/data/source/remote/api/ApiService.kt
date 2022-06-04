package com.mevalera.mvvmhiltroomexperiment.data.source.remote.api

import com.mevalera.mvvmhiltroomexperiment.data.model.Hits
import retrofit2.http.GET


interface ApiService {

    @GET("api/v1/search_by_date?query=mobile")
    suspend fun getRestaurants(): Hits
}