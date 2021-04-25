package com.udacity.asteroidradar.data_source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.udacity.asteroidradar.models.PictureOfDay
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureOfDayDao {
    @Query("SELECT * FROM PictureOfDay ORDER BY date ASC LIMIT 1")
    fun getPictureOfDay(): Flow<PictureOfDay>

    @Insert
    fun savePictureOfDay(pictureOfDay: PictureOfDay)

    @Query("DELETE FROM PictureOfDay")
    fun deletePictureOfDay()
}