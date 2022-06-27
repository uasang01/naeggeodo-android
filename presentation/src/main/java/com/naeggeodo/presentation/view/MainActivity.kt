package com.naeggeodo.presentation.view

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseActivity
import com.naeggeodo.presentation.databinding.ActivityMainBinding
import com.naeggeodo.presentation.di.App
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun initView() {
        // init bottom navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        binding.navBar.setupWithNavController(navController)

        // bottom navigation item 클릭 시 nav backstack을 home까지 없앤다
        binding.navBar.setOnItemSelectedListener { item ->
            if (item.itemId != navHostFragment.navController.currentDestination?.id) {
                navHostFragment.navController.navigate(
                    item.itemId, null,
                    NavOptions.Builder()
                        .setLaunchSingleTop(true)               //  remove duplicated stacks
                        .setPopUpTo(R.id.home, false)   //  pop stacks till Id
                        .build()
                )
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        App.prefs.clearAccessToken()
        App.prefs.clearUserId()
    }
}