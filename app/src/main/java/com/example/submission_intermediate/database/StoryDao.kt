package com.example.submission_intermediate.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryDB>)

    @Query("SELECT * FROM story order by createdAt DESC")
    fun getAllStory(): PagingSource<Int, StoryDB>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}
