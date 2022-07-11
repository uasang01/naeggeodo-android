package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.DeleteChat
import com.naeggeodo.domain.usecase.BookmarkingUseCase
import com.naeggeodo.domain.usecase.DeleteCreationChatHistoryUseCase
import com.naeggeodo.domain.usecase.GetChatCreationHistoryUseCase
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateHistoryViewModel @Inject constructor(
    private val getChatCreationHistoryUseCase: GetChatCreationHistoryUseCase,
    private val bookmarkingUseCase: BookmarkingUseCase,
    private val deleteCreationChatHistoryUseCase: DeleteCreationChatHistoryUseCase
) : BaseViewModel() {


    companion object {
        const val EVENT_BOOKMARK = 511
    }

    private val _chatList: MutableLiveData<List<Chat>> = SingleLiveEvent()
    val chatList: LiveData<List<Chat>> get() = _chatList
    private val _deleteResponse: MutableLiveData<DeleteChat> = SingleLiveEvent()
    val deleteResponse: LiveData<DeleteChat> get() = _deleteResponse

    val bookmarkId: MutableLiveData<Int> = MutableLiveData()


    fun getCreationHistories(userId: String) = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response = withContext(Dispatchers.IO) {
            getChatCreationHistoryUseCase.execute(this@CreateHistoryViewModel, userId = userId)
        }
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            _chatList.postValue(response.chatList)
            mutableScreenState.postValue(ScreenState.RENDER)
            viewEvent(HomeViewModel.EVENT_CATEGORIES_CHANGED)
        }
    }

    suspend fun bookmarking(chatId: Int, userId: String) = withContext(Dispatchers.IO) {
        mutableScreenState.postValue(ScreenState.LOADING)
        async {
            val response =
                bookmarkingUseCase.execute(this@CreateHistoryViewModel, chatId, userId = userId)
            mutableScreenState.postValue(if (response) ScreenState.RENDER else ScreenState.ERROR)
            response
        }
    }.await()

    suspend fun deleteHistory(chatId: Int) =
        withContext(Dispatchers.IO) {
            mutableScreenState.postValue(ScreenState.LOADING)
            async {
                val response = withContext(Dispatchers.IO) {
                    deleteCreationChatHistoryUseCase.execute(this@CreateHistoryViewModel, chatId)
                }
                val result = response?.deleted ?: false
                if (result) {
                    mutableScreenState.postValue(ScreenState.ERROR)
                } else {
                    mutableScreenState.postValue(ScreenState.RENDER)
                }
                result
            }
        }.await()
}