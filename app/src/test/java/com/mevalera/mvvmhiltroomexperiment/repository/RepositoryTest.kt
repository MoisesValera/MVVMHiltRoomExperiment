package com.mevalera.mvvmhiltroomexperiment.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mevalera.mvvmhiltroomexperiment.MainCoroutineRule
import com.mevalera.mvvmhiltroomexperiment.data.source.local.RestaurantDatabase
import com.mevalera.mvvmhiltroomexperiment.data.source.remote.api.ApiService
import com.mevalera.mvvmhiltroomexperiment.data.source.repository.RestaurantRepoImpl
import com.mevalera.mvvmhiltroomexperiment.util.NetworkHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.* // ktlint-disable no-wildcard-imports
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class RepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var testRule = MainCoroutineRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var database: RestaurantDatabase

    @Mock
    private lateinit var networkHelper: NetworkHelper

    private lateinit var repositoryImpl: RestaurantRepoImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repositoryImpl = RestaurantRepoImpl(
            apiService,
            database,
            networkHelper
        )
    }

    @After
    fun tearDown() {
        Mockito.reset(apiService)
        Mockito.reset(database)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetch restaurants`(): Unit = runBlocking {
        repositoryImpl.getRestaurants()
        Assert.assertNotNull(repositoryImpl.getRestaurants())
    }
}
