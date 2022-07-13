package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.User
import com.naeggeodo.domain.usecase.ChangeRemittanceStateUseCase
import com.naeggeodo.domain.usecase.GetUsersInChatUseCase
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
class RemitViewModel @Inject constructor(
    private val changeRemittanceStateUseCase: ChangeRemittanceStateUseCase,
    private val getUsersInChatUseCase: GetUsersInChatUseCase
) : BaseViewModel() {

    private val _users: SingleLiveEvent<List<User>> = SingleLiveEvent()
    val users: LiveData<List<User>> get() = _users

    companion object {
        const val EVENT_CHANGE_REMITTANCE_STATE_SUCCESS = 243
    }

    fun changeRemittanceState(chatId: Int, userId: String) = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response = withContext(Dispatchers.IO){
            changeRemittanceStateUseCase.execute(this@RemitViewModel, chatId, userId)
        }
        if(response == null){
            mutableScreenState.postValue(ScreenState.ERROR)
        }else{
            mutableScreenState.postValue(ScreenState.RENDER)
            Timber.e("changed remittance state to '$response'")
            viewEvent(EVENT_CHANGE_REMITTANCE_STATE_SUCCESS)
        }
    }


    fun getUsers(chatId: Int) = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response = withContext(Dispatchers.IO) {
            getUsersInChatUseCase.execute(this@RemitViewModel, chatId)
        }
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            _users.postValue(response!!.users)
            mutableScreenState.postValue(ScreenState.RENDER)
        }
    }
}