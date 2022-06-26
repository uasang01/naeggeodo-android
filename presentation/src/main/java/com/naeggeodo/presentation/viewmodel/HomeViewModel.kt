package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.Categories
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.usecase.CategoryUseCase
import com.naeggeodo.domain.usecase.SearchChatListByCategoryUseCase
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: CategoryUseCase,
    private val searchChatListByCategoryUseCase: SearchChatListByCategoryUseCase
) : BaseViewModel() {

    private val _categories: MutableLiveData<Categories> = MutableLiveData()
    val categories: LiveData<Categories> get() = _categories

    private val _chatList: MutableLiveData<List<Chat>> = MutableLiveData()
    val chatList: LiveData<List<Chat>> get() = _chatList


    fun getCategories() = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response = getCategoriesUseCase.execute(this@HomeViewModel)
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            mutableScreenState.postValue(ScreenState.RENDER)
            _categories.postValue(response!!)
        }
    }

    fun getChatList(category: String?, buildingCode: String) = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response =
            searchChatListByCategoryUseCase.execute(this@HomeViewModel, category, buildingCode)
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            mutableScreenState.postValue(ScreenState.RENDER)
            _chatList.postValue(response!!.chatList)
        }
    }
}