package com.naeggeodo.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.Category
import com.naeggeodo.domain.usecase.CreateChatUseCase
import com.naeggeodo.domain.utils.CategoryType
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class CreateChatViewModel @Inject constructor(
    private val createChatUseCase: CreateChatUseCase,
) : BaseViewModel() {
    private val _chatTitle: SingleLiveEvent<String> = SingleLiveEvent()
    val chatTitle: LiveData<String> get() = _chatTitle
    private val _category: SingleLiveEvent<Category> = SingleLiveEvent()
    val category: LiveData<Category> get() = _category
    private val _categoryKorean: SingleLiveEvent<String> = SingleLiveEvent()
    val categoryKorean: LiveData<String> get() = _categoryKorean
    private val _link: SingleLiveEvent<String> = SingleLiveEvent()
    val link: LiveData<String> get() = _link
    private val _place: SingleLiveEvent<String> = SingleLiveEvent()
    val place: LiveData<String> get() = _place
    private val _tag: SingleLiveEvent<String> = SingleLiveEvent()
    val tag: LiveData<String> get() = _tag
    private val _maxPeopleNum: SingleLiveEvent<Int> = SingleLiveEvent<Int>().apply {
        postValue(2)
    }
    val maxPeopleNum: LiveData<Int> get() = _maxPeopleNum
    private val _chatImage: SingleLiveEvent<Bitmap> = SingleLiveEvent()
    val chatImage: LiveData<Bitmap> get() = _chatImage

    private val _chatId: SingleLiveEvent<Int> = SingleLiveEvent()
    val chatId: LiveData<Int> get() = _chatId


    fun setChatTitle(str: String) = _chatTitle.postValue(str)
    fun setCategory(category: Category?) {
        if (category == null) {
            _categoryKorean.postValue("카테고리 선택")
        } else {
            _categoryKorean.postValue(enumValueOf<CategoryType>(category.category).korean)
        }
        category?.let { _category.postValue(category!!) }
    }

    fun setPlace(str: String) = _place.postValue(str)
    fun setLink(str: String) = _link.postValue(str)
    fun setTag(str: String) = _tag.postValue(str)
    fun setMaxPeopleNum(num: Int) = _maxPeopleNum.postValue(num)
    fun setChatImage(bitmap: Bitmap) = _chatImage.postValue(bitmap)

    //    = withContext(Dispatchers.IO) {
//        mutableScreenState.postValue(ScreenState.LOADING)
//        async {
//            val response =
//                bookmarkingUseCase.execute(this@CreateHistoryViewModel, chatId, userId = userId)
//            mutableScreenState.postValue(if (response) ScreenState.RENDER else ScreenState.ERROR)
//            response
//        }
//    }.await()
    suspend fun createChat(files: List<MultipartBody.Part>) = withContext(Dispatchers.IO) {
        mutableScreenState.postValue(ScreenState.LOADING)
        async {
            val response =
                createChatUseCase.execute(this@CreateChatViewModel, files)
            if (response == null) {
                mutableScreenState.postValue(ScreenState.ERROR)
                false
            } else {
                mutableScreenState.postValue(ScreenState.RENDER)
                _chatId.postValue(response!!.chatId)
                true
            }
        }
    }.await()

    fun getChatHistories(userId: String) = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
    }

    fun init() {
        setChatTitle("")
        setCategory(null)
        setPlace("")
        setLink("")
        setTag("")
        setMaxPeopleNum(2)
    }
}