package com.naeggeodo.presentation.view.chat

import android.content.Intent
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.naeggeodo.domain.utils.ErrorType
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseActivity
import com.naeggeodo.presentation.databinding.ActivityChatBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.Util.goToLoginScreen
import com.naeggeodo.presentation.utils.Util.showShortToast
import com.naeggeodo.presentation.view.LoginActivity
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
    }

    override fun onBackPressed() {
        navHostFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
            when (fragment) {
                is ChatFragment -> {
                    (fragment as OnBackPressedListener).onBackPressed()
                }
                else -> {
                    super.onBackPressed()
                }
            }
        }
    }

    override fun observeViewModels() {
        chatViewModel.mutableErrorType.observe(this){
            when(it){
                ErrorType.ACCESS_TOKEN_EXPIRED -> {
                    Timber.e("ACCESS_TOKEN_EXPIRED")
                    // 토큰 재발급

                }
                ErrorType.NETWORK -> {
                    showShortToast(applicationContext, "NETWORK ERROR")
                }
                ErrorType.REFRESH_TOKEN_EXPIRED -> {
                    Timber.e("REFRESH_TOKEN_EXPIRED")
                    // 로그인 화면으로 이동
                    goToLoginScreen(applicationContext)
                }
                ErrorType.TIMEOUT -> {
                    showShortToast(applicationContext, "TIMEOUT ERROR")
                }
                else -> {

                }
            }
        }

    }
}