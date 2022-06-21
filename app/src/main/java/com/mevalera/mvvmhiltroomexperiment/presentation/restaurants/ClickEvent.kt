package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants

import com.mevalera.mvvmhiltroomexperiment.data.model.Conference

interface ClickEvent {
    fun onItemClick(restaurant: Conference?)
}