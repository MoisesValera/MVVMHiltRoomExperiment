package com.mevalera.mvvmhiltroomexperiment.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mevalera.mvvmhiltroomexperiment.MainCoroutineRule
import com.mevalera.mvvmhiltroomexperiment.data.model.Restaurant
import com.mevalera.mvvmhiltroomexperiment.data.source.repository.RestaurantRepository
import com.mevalera.mvvmhiltroomexperiment.presentation.restaurants.RestaurantViewModel
import com.mevalera.mvvmhiltroomexperiment.util.Resource
import com.mevalera.mvvmhiltroomexperiment.util.getValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class ViewModelTest {

    private val data = mutableListOf(
        Restaurant(
            0,
            "",
            "",
            "",
            "",
            ""
        )
    )

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var testRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: RestaurantRepository
    private lateinit var viewModel: RestaurantViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = RestaurantViewModel(repository)
    }

    @After
    fun tearDown() {
        Mockito.reset(repository)
    }

    @Test
    fun `fetch restaurants`() = runBlocking {
        Mockito.`when`(repository.getRestaurants())
            .thenReturn(
                flowOf(Resource.Success(data))
            )

        viewModel.getRestaurants()
        Assert.assertNotNull(getValue(viewModel.restaurants).data)
        Assert.assertEquals(
            getValue(viewModel.restaurants)::class.java,
            Resource.Success::class.java
        )
    }
}
