package com.udacity.asteroidradar.view_models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val asteroidRepository: AsteroidRepository, application: Application) :
    AndroidViewModel(application) {
    companion object {
        const val TAG = "MainViewModel"
    }

    private var _pictureOfDay = MutableLiveData<PictureOfDay?>()
    var imageOfTheDay: LiveData<PictureOfDay?> = _pictureOfDay

    private var _asteroids = MutableLiveData<List<Asteroid>>(listOf())
    var asteroids: LiveData<List<Asteroid>> = _asteroids

    init {
        pictureOfDay()
        getAllAsteroids()
    }

    private fun pictureOfDay() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                asteroidRepository.getPictureOfDay().collect {
                    _pictureOfDay.postValue(it)
                }
            } catch (exception: Exception) {
                Log.e(TAG, exception.message, exception)
            }
        }
    }

    /**
     * Get all the Asteroids
     */
    fun getAllAsteroids() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                asteroidRepository.getAllAsteroids().collect {
                    _asteroids.postValue(it)
                }
            } catch (exception: Exception) {
                Log.e(TAG, exception.message, exception)
            }
        }
    }

    fun getTodayAsteroid() {
        viewModelScope.launch(Dispatchers.IO) {
            asteroidRepository.getTodayAsteroid().collect {
                _asteroids.postValue(it)
            }
        }
    }
}