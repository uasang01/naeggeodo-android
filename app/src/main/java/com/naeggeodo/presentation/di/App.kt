package com.naeggeodo.presentation.di

import android.app.Application
import com.naeggeodo.presentation.BuildConfig
import com.naeggeodo.presentation.utils.TimberDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App: Application() {
    companion object {
        lateinit var INSTANCE: Application
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        // print log on debug mode
        if (BuildConfig.DEBUG){
            Timber.plant(TimberDebugTree())
        }
    }
}