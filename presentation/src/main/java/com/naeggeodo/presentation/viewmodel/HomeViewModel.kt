package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.Category
import com.naeggeodo.domain.usecase.CategoryUseCase
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: CategoryUseCase
) : BaseViewModel() {

    private val _categories: MutableLiveData<Category> = MutableLiveData()
    val categories: LiveData<Category> get() = _categories


    fun getCategories() = viewModelScope.launch {
        val response = getCategoriesUseCase.execute(this@HomeViewModel)
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            mutableScreenState.postValue(ScreenState.RENDER)
            _categories.postValue(response)
        }
    }
}