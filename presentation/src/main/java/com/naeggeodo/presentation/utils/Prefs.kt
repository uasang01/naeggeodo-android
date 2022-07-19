package com.naeggeodo.presentation.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class Prefs(context: Context) {
    private val prefNm = "naeggeodoPref"
    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    //    private val prefs = context.getSharedPreferences(prefNm, Context.MODE_PRIVATE)
    private val prefs = EncryptedSharedPreferences
        .create(
            context,
            prefNm,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

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
    var nickname: String?
        get() = prefs.getString("nickname", null)
        set(value) {
            prefs.edit().putString("nickname", value).apply()
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
        buildingCode = null
        apartment = null

        nickname = null
    }

    fun clearNickname() {
        nickname = null
    }

    fun clearAccessToken() {
        accessToken = null
    }

    fun clearUserId() {
        userId = null
    }
}