package com.mevalera.mvvmhiltroomexperiment.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mevalera.mvvmhiltroomexperiment.data.model.Conference
import com.mevalera.mvvmhiltroomexperiment.data.model.ConferenceWithBody
import com.mevalera.mvvmhiltroomexperiment.data.model.FavoriteItems
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM conferences WHERE date LIKE '%' || :date || '%'")
    fun getFilteredConferences(date: String): Flow<List<Conference>>

    @Query("SELECT * FROM conferences ORDER BY created_at DESC")
    fun getLastPublishedConferences(): Flow<List<Conference>>

    @Query("SELECT * FROM conferences WHERE _id IN (:id)")
    fun getConference(id: Int): Flow<Conference>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConferences(conferences: List<Conference>)

    suspend fun insertWithTimestamp(conferences: List<Conference>) {
        conferences.map { it ->
            it.copy(createdAt = System.currentTimeMillis(), date = it.date?.replace("-",""))
        }.also {
            insertConferences(it)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConference(conference: Conference): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConferenceBody(conference: ConferenceWithBody)

    @Query("SELECT body FROM conferences_body WHERE id IN (:id)")
    fun getConferenceBody(id: Int): Flow<ConferenceWithBody>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bookmarkItem(favoriteItem: FavoriteItems)
}