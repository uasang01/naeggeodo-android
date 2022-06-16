package com.naeggeodo.presentation.view

import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseActivity
import com.naeggeodo.presentation.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {


    override fun initView() {
        // init bottom navigation
        NavigationUI.setupWithNavController(binding.navBar, findNavController(R.id.nav_host))
    }
}