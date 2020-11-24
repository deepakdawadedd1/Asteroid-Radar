package com.udacity.nanodegree.asteroidradar.database.dao

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM asteroid_feed")
    fun getFeeds(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: Asteroid)
}
