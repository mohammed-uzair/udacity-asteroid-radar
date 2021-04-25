package com.udacity.asteroidradar.data_source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNearEarthAsteroids(asteroids: List<Asteroid>)

    @Query("SELECT * FROM Asteroid ORDER BY closeApproachDate ASC")
    fun getAllCachedAsteroids(): Flow<List<Asteroid>>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate LIKE (Date('now'))")
    fun getTodayAsteroids(): Flow<List<Asteroid>>

    @Query("DELETE FROM Asteroid")
    fun removeAllAsteroids()
}