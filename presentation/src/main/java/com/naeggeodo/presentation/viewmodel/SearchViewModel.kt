package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.Tag
import com.naeggeodo.domain.usecase.GetTagsUseCase
import com.naeggeodo.domain.usecase.SearchChatListByKeyWordUseCase
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getTagsUseCase: GetTagsUseCase,
    private val searchChatListByKeyWordUseCase: SearchChatListByKeyWordUseCase
) : BaseViewModel() {
    private val _tags: MutableLiveData<List<Tag>> = MutableLiveData()
    val tags: LiveData<List<Tag>> get() = _tags
    private val _chatList: MutableLiveData<List<Chat>> = MutableLiveData()
    val chatList: LiveData<List<Chat>> get() = _chatList

    fun getTags() = viewModelScope.launch {
        val response = getTagsUseCase.execute(this@SearchViewModel)
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            mutableScreenState.postValue(ScreenState.RENDER)
            _tags.postValue(response.tags)
        }
    }

    fun searchChatList(searchType: String, keyWord: String) = viewModelScope.launch {
        val response =
            searchChatListByKeyWordUseCase.execute(this@SearchViewModel, searchType, keyWord)
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            mutableScreenState.postValue(ScreenState.RENDER)
            _chatList.postValue(response.chatList)
        }
    }
}