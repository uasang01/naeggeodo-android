package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.naeggeodo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor() : BaseViewModel() {
    private val _address: MutableLiveData<String> = MutableLiveData()
    val address: LiveData<String> get() = _address

    private val _buildingCode: MutableLiveData<String> = MutableLiveData()
    val buildingCode: LiveData<String> get() = _buildingCode

    private val _apartment: MutableLiveData<String> = MutableLiveData()
    val apartment: LiveData<String> get() = _apartment

    fun setAddress(str: String) = _address.postValue(str)
    fun setBuildingCode(str: String) = _buildingCode.postValue(str)
    fun setApartment(str: String) = _apartment.postValue(str)
}