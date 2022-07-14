package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.LogIn
import com.naeggeodo.domain.usecase.LoginUseCase
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _loginResult: SingleLiveEvent<LogIn> = SingleLiveEvent()
    val loginResult: LiveData<LogIn> get() = _loginResult


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
}