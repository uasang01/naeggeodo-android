package com.naeggeodo.presentation.view

import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseActivity
import com.naeggeodo.presentation.databinding.ActivityMainBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController
    private var backKeyPressedTime = 0L

    override fun initView() {
        // init bottom navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHost.id) as NavHostFragment
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

        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            binding.navBar.isGone = windowInsets.isVisible(ime())
            view.onApplyWindowInsets(windowInsets)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        App.prefs.clearAccessToken()
        App.prefs.clearUserId()
        App.prefs.clearNickname()
    }

    override fun onBackPressed() {
        if (navController.backQueue.size > 2) {
            navController.popBackStack()
        } else {
            if (System.currentTimeMillis() >= backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis()
                Util.showShortToast(applicationContext, "'뒤로'버튼을 한번 더 누르시면 종료됩니다")
            } else {
                Util.toast?.cancel()
                finish()
            }
        }
    }
}