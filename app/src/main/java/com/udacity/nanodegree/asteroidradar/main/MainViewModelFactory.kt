package com.udacity.nanodegree.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Created by @author Deepak Dawade on 11/24/2020 at 11:58 PM.
 * Copyright (c) 2020 deepak.dawade.dd@gmail.com All rights reserved.
 *
 */

/**
 * Factory for constructing DevByteViewModel with parameter
 */
class MainViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}