package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naeggeodo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateChatViewModel @Inject constructor() : BaseViewModel() {
    private val _restaurantName: MutableLiveData<String> = MutableLiveData()
    val restaurantName: LiveData<String> get() = _restaurantName
    private val _category: MutableLiveData<String> = MutableLiveData()
    val category: LiveData<String> get() = _category
    private val _link: MutableLiveData<String> = MutableLiveData()
    val link: LiveData<String> get() = _link
    private val _tag: MutableLiveData<String> = MutableLiveData()
    val tag: LiveData<String> get() = _tag
    private val _maxPeopleNum: MutableLiveData<Int> = MutableLiveData()
    val maxPeopleNum: LiveData<Int> get() = _maxPeopleNum
    private val _chatImage: MutableLiveData<String> = MutableLiveData()
    val chatImage: LiveData<String> get() = _chatImage

    fun setRestaurantName(str: String) = _restaurantName.postValue(str)
    fun setCategory(str: String) = _category.postValue(str)
    fun setLink(str: String) = _link.postValue(str)
    fun setTag(str: String) = _tag.postValue(str)
    fun setMaxPeopleNum(num: Int) = _maxPeopleNum.postValue(num)
    fun setChatImage(str: String) = _chatImage.postValue(str)
}