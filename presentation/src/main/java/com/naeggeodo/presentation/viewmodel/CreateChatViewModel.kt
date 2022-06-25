package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naeggeodo.domain.model.Category
import com.naeggeodo.domain.utils.CategoryType
import com.naeggeodo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateChatViewModel @Inject constructor() : BaseViewModel() {
    private val _chatTitle: MutableLiveData<String> = MutableLiveData()
    val chatTitle: LiveData<String> get() = _chatTitle
    private val _category: MutableLiveData<Category> = MutableLiveData()
    val category: LiveData<Category> get() = _category
    private val _categoryKorean: MutableLiveData<String> = MutableLiveData()
    val categoryKorean: LiveData<String> get() = _categoryKorean
    private val _link: MutableLiveData<String> = MutableLiveData()
    val link: LiveData<String> get() = _link
    private val _place: MutableLiveData<String> = MutableLiveData()
    val place: LiveData<String> get() = _place
    private val _tag: MutableLiveData<String> = MutableLiveData()
    val tag: LiveData<String> get() = _tag
    private val _maxPeopleNum: MutableLiveData<Int> = MutableLiveData()
    val maxPeopleNum: LiveData<Int> get() = _maxPeopleNum
    private val _chatImage: MutableLiveData<String> = MutableLiveData()
    val chatImage: LiveData<String> get() = _chatImage

    fun setChatTitle(str: String) = _chatTitle.postValue(str)
    fun setCategory(category: Category?) {
        if (category == null) {
            _categoryKorean.postValue("카테고리 선택")
        } else {
            _categoryKorean.postValue(enumValueOf<CategoryType>(category.category).korean)
        }
        category?.let { _category.postValue(category) }

    }

    fun setPlace(str: String) = _place.postValue(str)
    fun setLink(str: String) = _link.postValue(str)
    fun setTag(str: String) = _tag.postValue(str)
    fun setMaxPeopleNum(num: Int) = _maxPeopleNum.postValue(num)
    fun setChatImage(str: String) = _chatImage.postValue(str)

}