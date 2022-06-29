package com.naeggeodo.presentation.viewmodel

import com.naeggeodo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : BaseViewModel() {
    companion object {
        const val EVENT_ADDRESS_INFO_CHANGED = 111
    }
//
//    private val _addressInfo: MutableLiveData<Triple<String, String, String>> = MutableLiveData()
//    val addressInfo: LiveData<Triple<String, String, String>> get() = _addressInfo
//
//    fun setAddressInfo(info: Triple<String, String, String>){
//        _addressInfo.postValue(info)
//        viewEvent(EVENT_ADDRESS_INFO_CHANGED)
//    }
}