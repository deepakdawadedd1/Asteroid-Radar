package com.udacity.nanodegree.asteroidradar.network

import com.udacity.nanodegree.asteroidradar.PictureOfDay
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET(Endpoints.PICTURE_OF_THE_DAY)
    suspend fun getPictureOfDay(@Query("api_key") apiKey: String = API_KEY): PictureOfDay

    @GET(Endpoints.FEED)
    suspend fun getFeeds(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = API_KEY
    ): JSONObject

}
