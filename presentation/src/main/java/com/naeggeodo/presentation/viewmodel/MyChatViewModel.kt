package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.usecase.GetMyChatListUseCase
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyChatViewModel @Inject constructor(
    private val myChatListUseCase: GetMyChatListUseCase
) : BaseViewModel() {
    companion object {
//        const val EVENT = 516
    }

    private val _chatList = SingleLiveEvent<List<Chat>>()
    val chatList: LiveData<List<Chat>> get() = _chatList

    fun getMyChatList(userId: String) = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response =
            myChatListUseCase.execute(this@MyChatViewModel, userId)
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            mutableScreenState.postValue(ScreenState.RENDER)
            _chatList.postValue(response.chatList)
        }
    }
}