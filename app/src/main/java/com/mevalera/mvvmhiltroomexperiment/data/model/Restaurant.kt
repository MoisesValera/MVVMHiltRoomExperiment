package com.mevalera.mvvmhiltroomexperiment.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mevalera.mvvmhiltroomexperiment.R

@Entity(tableName = "conferences")
data class Conference(
    @PrimaryKey(autoGenerate = true)
    var _id: Int? = 0,
    val title: String,
    val hasText: Boolean? = null,
    val activity: String? = null,
    val date: String? = null,
    val body: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val thumbnail: String? = "drawable://" + R.drawable.lgccc_default_thumbnail,
    val nicedate: String? = null,
    val location: String? = null,
    val duration: String? = null,
    @ColumnInfo(name = "created_at") var createdAt: Long? = null,
    @Embedded val files: ConferenceFiles
)

@Entity(tableName = "favorite_items")
data class FavoriteItems(
    @PrimaryKey
    var id: Int
)

@Entity(tableName = "conferences_body")
data class ConferenceWithBody(
    @PrimaryKey
    var id: Int,
    val body: String? = null
)