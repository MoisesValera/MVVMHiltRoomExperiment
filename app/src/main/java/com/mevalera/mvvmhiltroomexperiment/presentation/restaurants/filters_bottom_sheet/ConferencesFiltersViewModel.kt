package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants.filters_bottom_sheet

import androidx.lifecycle.ViewModel
import com.mevalera.mvvmhiltroomexperiment.data.model.MonthsFilterConfig
import com.mevalera.mvvmhiltroomexperiment.util.FiltersHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConferencesFiltersViewModel @Inject constructor() : ViewModel() {

    fun getYearsFilters(): List<Int> {
        return FiltersHelper.getYears()
    }

    fun getMonthsFilters(): List<MonthsFilterConfig> {
        return FiltersHelper.getMonths()
    }
}