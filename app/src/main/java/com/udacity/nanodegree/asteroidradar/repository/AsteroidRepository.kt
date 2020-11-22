package com.udacity.nanodegree.asteroidradar.repository

import com.udacity.nanodegree.asteroidradar.Asteroid
import com.udacity.nanodegree.asteroidradar.PictureOfDay
import com.udacity.nanodegree.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.nanodegree.asteroidradar.network.Networking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Created by @author Deepak Dawade on 11/16/2020 at 8:11 PM.
 * Copyright (c) 2020 deepak.dawade.dd@gmail.com All rights reserved.
 *
 */
class AsteroidRepository() {

    suspend fun loadFeeds(startDate: String, endDate: String): List<Asteroid> {
        return withContext(Dispatchers.IO) {
            val response = Networking.networkService.getFeeds(startDate, endDate)
            parseAsteroidsJsonResult(response)
        }
    }

    suspend fun getPictureOfDay(): PictureOfDay {
        return withContext(Dispatchers.IO) {
            val response = Networking.networkService.getPictureOfDay()
            response
        }
    }
}