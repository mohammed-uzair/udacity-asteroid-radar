package com.udacity.asteroidradar.data_source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.udacity.asteroidradar.models.Asteroid

@Dao
interface AsteroidDao {
    @Insert
    suspend fun saveNearEarthAsteroids(asteroids: List<Asteroid>)
}