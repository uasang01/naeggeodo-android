package com.naeggeodo.presentation.utils

import android.content.Context

class Prefs(context: Context) {
    private val prefNm = "naeggeodoPref"
    private val prefs = context.getSharedPreferences(prefNm, Context.MODE_PRIVATE)

    var accessToken: String?
        get() = prefs.getString("accessToken", null)
        set(value) {
            prefs.edit().putString("accessToken", value).apply()
        }
    var refreshToken: String?
        get() = prefs.getString("refreshToken", null)
        set(value) {
            prefs.edit().putString("refreshToken", value).apply()
        }
    var userId: String?
        get() = prefs.getString("userId", null)
        set(value) {
            prefs.edit().putString("userId", value).apply()
        }
    var address: String?
        get() = prefs.getString("address", null)
        set(value) {
            prefs.edit().putString("address", value).apply()
        }

    var buildingCode: String?
        get() = prefs.getString("buildingCode", null)
        set(value) {
            prefs.edit().putString("buildingCode", value).apply()
        }

    var apartment: String?
        get() = prefs.getString("apartment", null)
        set(value) {
            prefs.edit().putString("apartment", value).apply()
        }


    fun clearAll() {
        accessToken = null
        refreshToken = null
        userId = null
        address = null
    }

    fun clearAccessToken() {
        accessToken = null
    }

    fun clearUserId() {
        userId = null
    }
}