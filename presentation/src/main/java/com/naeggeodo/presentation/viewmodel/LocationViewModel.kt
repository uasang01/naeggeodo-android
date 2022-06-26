package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naeggeodo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor() : BaseViewModel() {

    // first, second, third => address, buildingCode, apartment
    private val _addressInfo: MutableLiveData<Triple<String, String, String>> = MutableLiveData()
    val addressInfo: LiveData<Triple<String, String, String>> get() = _addressInfo

    fun setAddressInfo(info: Triple<String, String, String>) = _addressInfo.postValue(info)
}