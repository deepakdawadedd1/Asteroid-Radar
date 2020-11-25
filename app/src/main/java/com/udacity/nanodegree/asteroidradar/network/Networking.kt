package com.udacity.nanodegree.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

const val BASE_URL = "https://api.nasa.gov/"
const val API_KEY = ""//TODO("PASTE Your API_KEY_HERE")

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Main entry point for network access. Call like `Network.devbytes.getPlaylist()`
 */
object Networking {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofitBuilder = Retrofit.Builder().baseUrl(BASE_URL)
    val scalerNetworkService: NetworkService = retrofitBuilder.addConverterFactory(
        ScalarsConverterFactory.create()
    )
        .build()
        .create(NetworkService::class.java)
    val moshiNetworkService: NetworkService = retrofitBuilder.addConverterFactory(
        MoshiConverterFactory.create(moshi)
    )
        .build()
        .create(NetworkService::class.java)
}
