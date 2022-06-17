package com.naeggeodo.presentation.di

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.naeggeodo.presentation.BuildConfig
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.utils.Prefs
import com.naeggeodo.presentation.utils.TimberDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App: Application() {
    companion object {
        lateinit var INSTANCE: Application
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()

        INSTANCE = this

        // print log on debug mode
        if (BuildConfig.DEBUG){
            Timber.plant(TimberDebugTree())
        }

        // initialize Kakao SDK
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}