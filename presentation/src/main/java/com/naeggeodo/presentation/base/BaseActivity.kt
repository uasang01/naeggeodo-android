package com.naeggeodo.presentation.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding>
    (@LayoutRes private val layoutId: Int) : AppCompatActivity() {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSetContentView()

        super.onCreate(savedInstanceState)

        // ignore dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = DataBindingUtil.setContentView(this, layoutId)

        init()
        initView()
        initViewModel()
        initListener()
        observeViewModels()
        afterOnCreate()
    }

    protected open fun init() {}
    protected open fun beforeSetContentView() {}
    protected open fun initView() {}
    protected open fun initViewModel() {}
    protected open fun initListener() {}
    protected open fun observeViewModels() {}
    protected open fun afterOnCreate() {}

    override fun onDestroy() {
        super.onDestroy()
    }

//    protected fun shortShowToast(msg: String) =
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
//
//    protected fun longShowToast(msg: String) =
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}