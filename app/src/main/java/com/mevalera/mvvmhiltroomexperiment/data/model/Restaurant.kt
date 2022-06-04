package com.mevalera.mvvmhiltroomexperiment.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mevalera.mvvmhiltroomexperiment.util.formatDateTime


@Entity(tableName = "mvvmhiltroomexperiment")
data class Restaurant(
    @PrimaryKey(autoGenerate = true)
    var story_id: Int? = 0,
    val story_title: String?,
    val story_url: String?,
    val author: String,
    val logo: String? = "https://loremflickr.com/500/500/coding",
    val created_at: String
) {
    fun formatDate(): String {
        return created_at.formatDateTime(created_at)
    }
}

@Entity(tableName = "hidden_restaurants")
data class HiddenRestaurants(
    @PrimaryKey
    var id: Int
)
