package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.ChatList
import com.naeggeodo.domain.model.ChatTitle
import com.naeggeodo.domain.usecase.ChangeChatTitleUseCase
import com.naeggeodo.domain.usecase.GetMyChatListUseCase
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyChatViewModel @Inject constructor(
    private val myChatListUseCase: GetMyChatListUseCase,
    private val changeChatTitleUseCase: ChangeChatTitleUseCase
) : BaseViewModel() {
    companion object {
        const val ERROR_CHANGE_TITLE_FAILURE = 600
    }

    private val _chatList = SingleLiveEvent<List<Chat>>()
    val chatList: LiveData<List<Chat>> get() = _chatList

    private val _chatTitle = SingleLiveEvent<ChatTitle>()
    val chatTitle: LiveData<ChatTitle> get() = _chatTitle

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

    fun changeTitle(chatId: Int, title: String) = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response =
            changeChatTitleUseCase.execute(this@MyChatViewModel, chatId, title)
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
            viewEvent(ERROR_CHANGE_TITLE_FAILURE)
        } else {
            mutableScreenState.postValue(ScreenState.RENDER)
            _chatTitle.postValue(response!!)
        }
    }
}