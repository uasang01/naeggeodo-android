package com.naeggeodo.presentation.view.chat

import android.os.Handler
import android.os.Looper
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseActivity
import com.naeggeodo.presentation.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding>(R.layout.activity_chat) {
    private lateinit var navController: NavController
    override fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostChat.id) as NavHostFragment
        navController = navHostFragment.navController
//        navController = findNavController(R.id.nav_host_chat)

        Timber.e("test hi")
//        navController = findNavController(binding.navHostChat)

//
        Handler(Looper.getMainLooper()).postDelayed({
            Timber.e("test ${navController.backQueue}")
//            if(navController.backQueue.size<=2){
//                finish()
//            }
            navController.popBackStack(R.id.navigation,false)
        }, 1000)
    }
}