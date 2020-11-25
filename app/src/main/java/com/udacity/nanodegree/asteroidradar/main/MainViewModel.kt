package com.udacity.nanodegree.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.nanodegree.asteroidradar.PictureOfDay
import com.udacity.nanodegree.asteroidradar.database.AsteroidDatabase
import com.udacity.nanodegree.asteroidradar.database.entities.Asteroid
import com.udacity.nanodegree.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {
    private val database = AsteroidDatabase.getDataBase(application)

    private val repository = AsteroidRepository(database.asteroidDao)

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay> get() = _pictureOfDay

    val feeds: LiveData<List<Asteroid>> = Transformations.map(repository.feeds) {
        it
    }

    private val _showProgress = MutableLiveData(true)
    val showProgress: LiveData<Boolean> get() = _showProgress

    private val _navigator: MutableLiveData<Asteroid> = MutableLiveData()
    val navigator: LiveData<Asteroid>
        get() = _navigator


    init {
        fetchPictureOfDay()
        loadFeeds()
    }

    private fun loadFeeds() {
        _showProgress.value = true
        viewModelScope.launch {
            try {
                repository.fetchFeeds()
            } catch (ex: Exception) {
                Log.e("MainViewModel", ex.message, ex.cause)
            }
        }
    }

    private fun fetchPictureOfDay() {
        viewModelScope.launch {
            try {
                val picture = repository.getPictureOfDay()
                _pictureOfDay.postValue(picture)
            } catch (ex: Exception) {
                Log.e("MainViewModel", ex.message, ex.cause)
            }
        }
    }

    fun navigateToDetails(asteroid: Asteroid) {
        _navigator.value = asteroid
    }

    fun navigationDone() {
        _navigator.value = null
    }

    fun progress(empty: Boolean) {
        _showProgress.value = empty
    }

    fun filter(filter: Filter) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.filterFeeds(filter)
        }
    }
}
