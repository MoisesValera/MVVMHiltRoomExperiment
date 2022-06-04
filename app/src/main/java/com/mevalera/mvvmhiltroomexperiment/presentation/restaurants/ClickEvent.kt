package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants

import com.mevalera.mvvmhiltroomexperiment.data.model.Restaurant

interface ClickEvent {
    fun onItemClick(restaurant: Restaurant?)
}