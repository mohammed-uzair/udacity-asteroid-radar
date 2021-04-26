package com.udacity.asteroidradar.data_source.database.dao

import androidx.room.*
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Transaction
    fun saveNearEarthAsteroids(asteroids: List<Asteroid>){
        removeAllAsteroids()
        insertNearEarthAsteroids(asteroids)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNearEarthAsteroids(asteroids: List<Asteroid>)

    @Query("SELECT * FROM Asteroid ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): Flow<List<Asteroid>>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate LIKE (Date('now'))")
    fun getTodayAsteroids(): Flow<List<Asteroid>>

    @Query("DELETE FROM Asteroid")
    fun removeAllAsteroids()
}