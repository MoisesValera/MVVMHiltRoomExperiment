package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants

import androidx.lifecycle.*
import com.mevalera.mvvmhiltroomexperiment.data.model.Conference
import com.mevalera.mvvmhiltroomexperiment.data.model.ConferenceWithBody
import com.mevalera.mvvmhiltroomexperiment.data.source.repository.RestaurantRepository
import com.mevalera.mvvmhiltroomexperiment.util.NetworkHelper
import com.mevalera.mvvmhiltroomexperiment.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val repository: RestaurantRepository
) : ViewModel() {
    private val _restaurants = MutableLiveData<Resource<List<Conference>>>()
    val restaurants: LiveData<Resource<List<Conference>>> = _restaurants.distinctUntilChanged()

    private val _yearFilter = MutableLiveData<String>()
    private val _monthFilter = MutableLiveData<String>()
    val yearFilter: LiveData<String> = _yearFilter.distinctUntilChanged()
    val monthFilter: LiveData<String> = _monthFilter.distinctUntilChanged()

    private val _letterSize = MutableLiveData<Int>()
    val letterSize: LiveData<Int> = _letterSize.distinctUntilChanged()

    private val _conference = MutableLiveData<Resource<ConferenceWithBody>>()
    val conference: LiveData<Resource<ConferenceWithBody>> = _conference

    private val networkAvailable = MutableLiveData<NetworkHelper>()

    init {
        getConferences()
    }

    private fun refreshConferences(resource: Resource<List<Conference>>) {
        _restaurants.value = resource
    }

    fun getConferences() = viewModelScope.launch {
        repository.getConferences().collect {
            refreshConferences(it)
        }
    }

    fun getConference(id: Int) = viewModelScope.launch {
        repository.getConferenceBody(id).collect {
            _conference.value = it
        }
    }

    fun getFilteredConferences(year: String, month: String) = viewModelScope.launch {
        repository.getFilteredConferences(year, month).collect {
            refreshConferences(it)
        }
    }

    fun bookmarkItem(conferenceId: Int) = viewModelScope.launch {
        repository.bookmarkConference(conference_id = conferenceId)
    }

    fun setLetterSize(size: Int) {
        _letterSize.value = size
    }

    fun getNetWorkAvailability(): Boolean {
        return networkAvailable.value?.isNetworkAvailable()!!
    }
}
