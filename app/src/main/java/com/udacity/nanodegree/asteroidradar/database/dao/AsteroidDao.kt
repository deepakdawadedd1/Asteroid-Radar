package com.udacity.nanodegree.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.nanodegree.asteroidradar.database.entities.Asteroid


/**
 * Created by @author Deepak Dawade on 11/24/2020 at 11:57 PM.
 * Copyright (c) 2020 deepak.dawade.dd@gmail.com All rights reserved.
 *
 */

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroid_feed ORDER BY closeApproachDate DESC")
    fun getAllFeeds(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroid: Asteroid)

    @Query("SELECT * FROM asteroid_feed WHERE closeApproachDate == :date ORDER BY closeApproachDate DESC")
    suspend fun feedByDate(date: String): List<Asteroid>

    @Query("SELECT * FROM asteroid_feed WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate DESC")
    suspend fun feedByWeek(startDate: String, endDate: String): List<Asteroid>

}
