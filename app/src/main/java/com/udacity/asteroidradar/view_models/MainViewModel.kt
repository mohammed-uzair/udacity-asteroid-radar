package com.udacity.asteroidradar.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(private val asteroidRepository: AsteroidRepository, application: Application) :
    AndroidViewModel(application) {
    companion object {
        const val TAG = "MainViewModel"
    }

    private var _imageOfTheDay = MutableLiveData<PictureOfDay?>()
    var imageOfTheDay: LiveData<PictureOfDay?> = _imageOfTheDay

    private var _asteroids = MutableLiveData<List<Asteroid>>(listOf())
    var asteroids: LiveData<List<Asteroid>> = _asteroids

    init {
        imageOfTheDay()
        getAllAsteroids()
    }

    private fun imageOfTheDay() {
        viewModelScope.launch {
            //Get the Picture Of The Day from the data source
            val image = asteroidRepository.getImageOfTheDay()
            _imageOfTheDay.postValue(image)
        }
    }

    private fun getAllAsteroids() {
        try {
            asteroidRepository.getAllNearEarthAsteroids().onEach {
                _asteroids.postValue(it)
            }
        } catch (e: Exception) {
            val a = "a"
        }
    }
}