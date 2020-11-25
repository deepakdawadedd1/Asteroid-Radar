package com.udacity.nanodegree.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.udacity.nanodegree.asteroidradar.PictureOfDay
import com.udacity.nanodegree.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.nanodegree.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.nanodegree.asteroidradar.database.dao.AsteroidDao
import com.udacity.nanodegree.asteroidradar.database.entities.Asteroid
import com.udacity.nanodegree.asteroidradar.main.Filter
import com.udacity.nanodegree.asteroidradar.network.Networking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository(private val asteroidDao: AsteroidDao) {

    private val allFeeds: LiveData<List<Asteroid>> = asteroidDao.getAllFeeds()
    private val _feeds: MediatorLiveData<List<Asteroid>> = MediatorLiveData<List<Asteroid>>()
    val feeds: LiveData<List<Asteroid>> get() = _feeds

    init {
        _feeds.addSource(allFeeds) {
            _feeds.value = it
        }
    }

    suspend fun filterFeeds(filter: Filter) {
        val days = getNextSevenDaysFormattedDates()
        val data = when (filter) {
            Filter.TODAY -> asteroidDao.feedByDate(days.first())
            Filter.WEEK -> asteroidDao.feedByWeek(days.first(), days.last())
            Filter.SAVED -> allFeeds.value
        }
        _feeds.postValue(data)
    }

    suspend fun fetchFeeds() {
        val days = getNextSevenDaysFormattedDates()
        val response = Networking.scalerNetworkService.getFeeds(days.first(), days.last())
        val feeds = parseAsteroidsJsonResult(JSONObject(response))
        asteroidDao.insertAll(* feeds.toTypedArray())
    }

    suspend fun getPictureOfDay(): PictureOfDay {
        return withContext(Dispatchers.IO) {
            val response = Networking.moshiNetworkService.getPictureOfDay()
            response
        }
    }
}