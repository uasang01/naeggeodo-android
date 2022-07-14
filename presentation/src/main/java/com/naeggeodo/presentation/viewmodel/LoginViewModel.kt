package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.LogIn
import com.naeggeodo.domain.usecase.LoginUseCase
import com.naeggeodo.domain.usecase.RefreshTokenUseCase
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase
) : BaseViewModel() {

    private val _loginResult: SingleLiveEvent<LogIn> = SingleLiveEvent()
    val loginResult: LiveData<LogIn> get() = _loginResult

    private val _refreshResult: SingleLiveEvent<LogIn> = SingleLiveEvent()
    val refreshResult: LiveData<LogIn> get() = _refreshResult


    fun logIn(platform: String, body: HashMap<String, String?>) = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response = loginUseCase.execute(this@LoginViewModel, platform, body)
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            mutableScreenState.postValue(ScreenState.RENDER)
            _loginResult.postValue(response!!)
        }
        Timber.d("$platform signing response ${response}")
    }

    fun refreshToken() = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response = withContext(Dispatchers.IO) {
            refreshTokenUseCase.execute(this@LoginViewModel)
        }
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            Timber.e("refresh token response : ${response}")
            mutableScreenState.postValue(ScreenState.RENDER)
            _refreshResult.postValue(response!!)
        }
    }
}