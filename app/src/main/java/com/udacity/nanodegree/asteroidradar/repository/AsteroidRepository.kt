package com.udacity.nanodegree.asteroidradar.repository

import com.udacity.nanodegree.asteroidradar.Asteroid
import com.udacity.nanodegree.asteroidradar.PictureOfDay
import com.udacity.nanodegree.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.nanodegree.asteroidradar.network.Networking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository() {

    suspend fun loadFeeds(startDate: String, endDate: String): List<Asteroid> {
        return withContext(Dispatchers.IO) {
            val response = Networking.scalerNetworkService.getFeeds(startDate, endDate)
            parseAsteroidsJsonResult(JSONObject(response))
        }
    }

    suspend fun getPictureOfDay(): PictureOfDay {
        return withContext(Dispatchers.IO) {
            val response = Networking.moshiNetworkService.getPictureOfDay()
            response
        }
    }
}