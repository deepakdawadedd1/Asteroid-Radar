package com.udacity.nanodegree.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.nanodegree.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.nanodegree.asteroidradar.PictureOfDay
import com.udacity.nanodegree.asteroidradar.database.AsteroidDatabase
import com.udacity.nanodegree.asteroidradar.database.entities.Asteroid
import com.udacity.nanodegree.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : ViewModel() {
    private val database = AsteroidDatabase.getDataBase(application)

    private val currentDate = MutableLiveData<Date>()
    private val repository = AsteroidRepository(database.asteroidDao)

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay> get() = _pictureOfDay

    val feeds: LiveData<List<Asteroid>> get() = repository.feeds

    private val _showProgress = MutableLiveData(true)
    val showProgress: LiveData<Boolean> get() = _showProgress

    private val startDate = Transformations.map(currentDate) {
        val formattedDate = SimpleDateFormat(API_QUERY_DATE_FORMAT).format(it)
        formattedDate
    }

    private val _navigator: MutableLiveData<Asteroid> = MutableLiveData()
    val navigator: LiveData<Asteroid>
        get() = _navigator

    private val endDate = Transformations.map(currentDate) {
        val calendar = Calendar.getInstance().apply {
            time = it
        }
        val d = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.set(Calendar.DAY_OF_WEEK, d + 7)
        val formattedDate = SimpleDateFormat(API_QUERY_DATE_FORMAT).format(calendar.time)
        formattedDate
    }

    init {
        fetchPictureOfDay()
        loadFeeds()
        currentDate.value = Calendar.getInstance().time
    }

    private fun loadFeeds() {
        _showProgress.value = true
        viewModelScope.launch {
            try {
                repository.loadFeeds(startDate.value ?: "", endDate.value ?: "")
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
}
