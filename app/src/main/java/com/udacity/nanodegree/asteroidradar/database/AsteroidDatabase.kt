package com.udacity.nanodegree.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.nanodegree.asteroidradar.database.dao.AsteroidDao
import com.udacity.nanodegree.asteroidradar.database.entities.Asteroid


@Database(entities = [Asteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AsteroidDatabase

        fun getDataBase(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "neo-feeds"
                    ).build()
                }
                return INSTANCE
            }
        }

    }
}

