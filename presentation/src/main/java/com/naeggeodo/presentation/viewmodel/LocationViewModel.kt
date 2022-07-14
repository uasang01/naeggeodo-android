package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor() : BaseViewModel() {
    // first, second, third => address, buildingCode, apartment
    companion object {
        const val EVENT_ADDRESS_INFO_CHANGED = 111
    }

    private val _addressInfo: SingleLiveEvent<Triple<String, String, String>> = SingleLiveEvent()
    val addressInfo: LiveData<Triple<String, String, String>> get() = _addressInfo

    fun setAddressInfo(info: Triple<String, String, String>) {
        _addressInfo.postValue(info)
        viewEvent(EVENT_ADDRESS_INFO_CHANGED)
    }
}