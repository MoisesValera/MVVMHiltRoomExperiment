package com.mevalera.mvvmhiltroomexperiment.util

import com.mevalera.mvvmhiltroomexperiment.data.model.MonthsFilterConfig
import java.text.DateFormatSymbols
import javax.inject.Singleton

@Singleton
open class FiltersHelper {
    companion object {
        fun getYears(): List<Int> {
            val yearsArray = mutableListOf<Int>()
            for (year in MINIMUM_YEAR..MAXIMUM_YEAR) {
                yearsArray.add(year)
            }
            return yearsArray
        }

        fun getMonths(): List<MonthsFilterConfig> {
            val monthsList = mutableListOf<MonthsFilterConfig>()
            for (month in 1..MAXIMUM_MONTH) {
                monthsList.add(MonthsFilterConfig(month,(DateFormatSymbols().months[month - 1]).replaceFirstChar { it.uppercase() }))
            }
            return monthsList
        }

        private const val MAXIMUM_YEAR = 2018
        private const val MINIMUM_YEAR = 1980
        private const val MAXIMUM_MONTH = 12
    }
}