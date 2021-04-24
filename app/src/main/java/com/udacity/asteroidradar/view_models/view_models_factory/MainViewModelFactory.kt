package com.udacity.asteroidradar.view_models.view_models_factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.view_models.MainViewModel

class MainViewModelFactory(
    private val asteroidRepository: AsteroidRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(asteroidRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}