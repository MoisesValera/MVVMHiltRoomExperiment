package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevalera.mvvmhiltroomexperiment.data.model.Restaurant
import com.mevalera.mvvmhiltroomexperiment.data.source.repository.RestaurantRepository
import com.mevalera.mvvmhiltroomexperiment.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val repository: RestaurantRepository
) : ViewModel() {
    val restaurants = MutableLiveData<Resource<List<Restaurant>>>()

    fun getRestaurants() = viewModelScope.launch {
        repository.getRestaurants().collect {
            restaurants.value = it
        }
    }

    fun deleteRestaurant(story_id: Int) = viewModelScope.launch {
            repository.deleteRestaurant(story_id)
    }
}
