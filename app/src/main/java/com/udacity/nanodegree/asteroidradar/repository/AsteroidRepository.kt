package com.udacity.nanodegree.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.nanodegree.asteroidradar.PictureOfDay
import com.udacity.nanodegree.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.nanodegree.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.nanodegree.asteroidradar.database.dao.AsteroidDao
import com.udacity.nanodegree.asteroidradar.database.entities.Asteroid
import com.udacity.nanodegree.asteroidradar.network.Networking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository(private val asteroidDao: AsteroidDao) {

    val feeds: LiveData<List<Asteroid>>
        get() = asteroidDao.getFeeds()

    val getPictureOfDay: PictureOfDay?
        get() = null

    suspend fun loadFeeds() {
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