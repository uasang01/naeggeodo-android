package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.MyInfo
import com.naeggeodo.domain.model.MyNickName
import com.naeggeodo.domain.usecase.ChangeNickNameUseCase
import com.naeggeodo.domain.usecase.GetMyInfoUseCase
import com.naeggeodo.domain.usecase.GetMyNickNameUseCase
import com.naeggeodo.domain.usecase.ReportUseCase
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val changeNickNameUseCase: ChangeNickNameUseCase,
    private val getMyNickNameUseCase: GetMyNickNameUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val reportUseCase: ReportUseCase
) : BaseViewModel() {

    private val _myInfo = SingleLiveEvent<MyInfo>()
    val myInfo: LiveData<MyInfo> get() = _myInfo
    private val _nickname = SingleLiveEvent<MyNickName>()
    val nickname: LiveData<MyNickName> get() = _nickname


    fun getNickName(userId: String) = viewModelScope.launch {
        getMyNickNameUseCase.execute(this@InfoViewModel, userId)
    }

    fun getMyInfo(userId: String) = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response = withContext(Dispatchers.IO) {
            getMyInfoUseCase.execute(this@InfoViewModel, userId)
        }
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)

        } else {
            _myInfo.postValue(response!!)
            mutableScreenState.postValue(ScreenState.RENDER)
            viewEvent(HomeViewModel.EVENT_CATEGORIES_CHANGED)
        }
    }

    fun changeNickname(userId: String, nickname: String) = viewModelScope.launch {
        val response = withContext(Dispatchers.IO) {
            changeNickNameUseCase.execute(this@InfoViewModel, userId, nickname)
        }
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            _nickname.postValue(response!!)
            mutableScreenState.postValue(ScreenState.RENDER)
            viewEvent(HomeViewModel.EVENT_CATEGORIES_CHANGED)
        }
    }

    fun report(body: HashMap<String, String>) = viewModelScope.launch {
        val response = withContext(Dispatchers.IO) {
            reportUseCase.execute(this@InfoViewModel, body)
        }
        if (response) {
            mutableScreenState.postValue(ScreenState.RENDER)
        } else {
            mutableScreenState.postValue(ScreenState.ERROR)
        }
    }
}