package com.naeggeodo.presentation.di

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.naeggeodo.presentation.BuildConfig
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.utils.Prefs
import com.naeggeodo.presentation.utils.TimberDebugTree
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import okio.IOException
import timber.log.Timber
import java.net.SocketException

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

        RxJavaPlugins.setErrorHandler { e ->
            var error = e
            if (error is UndeliverableException) {
                error = e.cause
            }
            if (error is IOException || error is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (error is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (error is NullPointerException || error is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler
                    ?.uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            if (error is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler
                    ?.uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            Timber.w("Undeliverable exception received, not sure what to do $error")
        }
    }
}