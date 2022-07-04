package com.naeggeodo.presentation.view.chat

import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseActivity
import com.naeggeodo.presentation.databinding.ActivityChatBinding
import com.naeggeodo.presentation.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding>(R.layout.activity_chat) {
    private lateinit var navController: NavController
    private val chatViewModel: ChatViewModel by viewModels()

    lateinit var navHostFragment: NavHostFragment

    interface OnBackPressedListener {
        fun onBackPressed()
    }

    override fun init() {
        val chatId = intent.getIntExtra("chatId", -1)
        if (chatId < 0) {
            finish()
            Timber.e("chatId is wrong. / $chatId")
        }
        chatViewModel.chatId = chatId
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostChat.id) as NavHostFragment
        navController = navHostFragment.navController


//        test
//        Handler(Looper.getMainLooper()).postDelayed({
//            Timber.e("test ${navController.backQueue}")
//            // no work
////            navController.popBackStack(R.id.navigation,false)
//        }, 1000)
    }

    override fun onBackPressed() {
        navHostFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
            when (fragment) {
                is ChatFragment -> {
                    (fragment as OnBackPressedListener).onBackPressed()
                }
                else -> {
//                    if (navController.backQueue.size <= 2) {
//                        finish()
//                    } else {
//                        navController.popBackStack(R.id.navigation, false)
//                    }
                    super.onBackPressed()
                }
            }
        }
    }
}