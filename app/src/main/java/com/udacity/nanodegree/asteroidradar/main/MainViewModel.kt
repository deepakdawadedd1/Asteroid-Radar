package com.udacity.nanodegree.asteroidradar.main

import androidx.lifecycle.*
import com.udacity.nanodegree.asteroidradar.Asteroid
import com.udacity.nanodegree.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.nanodegree.asteroidradar.PictureOfDay
import com.udacity.nanodegree.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {
    private val currentDate = MutableLiveData<Date>()
    private val repository = AsteroidRepository()

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay> get() = _pictureOfDay

    private val _feeds = MutableLiveData<List<Asteroid>>()
    val feeds: LiveData<List<Asteroid>> get() = _feeds

    private val _showProgress = MutableLiveData<Boolean>()
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
            val feeds = repository.loadFeeds(startDate.value ?: "", endDate.value ?: "")
            _feeds.postValue(feeds)
            _showProgress.postValue(false)
        }
    }

    private fun fetchPictureOfDay() {
        viewModelScope.launch {
            val picture = repository.getPictureOfDay()
            _pictureOfDay.postValue(picture)
        }
    }

    fun navigateToDetails(asteroid: Asteroid) {
        _navigator.value = asteroid
    }

    fun navigationDone() {
        _navigator.value = null
    }
}