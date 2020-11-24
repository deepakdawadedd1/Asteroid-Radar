package com.udacity.nanodegree.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.nanodegree.asteroidradar.database.AsteroidDatabase
import com.udacity.nanodegree.asteroidradar.repository.AsteroidRepository

class RefreshDataWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshFeedsWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getDataBase(applicationContext)
        val repository = AsteroidRepository(database.asteroidDao)
        return try {
            repository.loadFeeds()
            Result.success()
        } catch (ex: Exception) {
            Result.retry()
        }
    }
}