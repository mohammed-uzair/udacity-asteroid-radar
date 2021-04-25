package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.data_source.database.dao.AsteroidDao
import com.udacity.asteroidradar.data_source.database.dao.PictureOfDayDao

class AsteroidRepository(
    private val asteroidDao: AsteroidDao,
    private val pictureOfDayDao: PictureOfDayDao
) {
    /**
     * This will always return the PictureOfDay from the local cached database, keeping the database
     * as the Single Source Of Truth.
     *
     * This database is filled with data from the worker class that downloads the data from the
     * NASA server on a specified periodic basis.
     */
    fun getPictureOfDay() = pictureOfDayDao.getPictureOfDay()

    /**
     * This will always return the Asteroids from the local cached database, keeping the database
     * as the Single Source Of Truth.
     *
     * This database is filled with data from the worker class that downloads the data from the
     * NASA server on a specified periodic basis.
     */
    fun getAllAsteroids() = asteroidDao.getAllCachedAsteroids()

    /**
     * This will always return the Asteroids of today from the local cached database, keeping the database
     * as the Single Source Of Truth.
     *
     * This internally uses the #getAllAsteroids data, with filter query to filter just
     * today's records.
     *
     * This database is filled with data from the worker class that downloads the data from the
     * NASA server on a specified periodic basis.
     */
    fun getTodayAsteroid() = asteroidDao.getTodayAsteroids()
}