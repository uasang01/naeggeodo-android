package com.naeggeodo.presentation.view.info

import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.naeggeodo.domain.utils.ErrorType
import com.naeggeodo.domain.utils.ReportType
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentInfoBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.Util.goToLoginScreen
import com.naeggeodo.presentation.utils.Util.hideKeyboard
import com.naeggeodo.presentation.utils.Util.showShortToast
import com.naeggeodo.presentation.view.CommonDialogFragment
import com.naeggeodo.presentation.view.LoginActivity
import com.naeggeodo.presentation.viewmodel.InfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class InfoFragment : BaseFragment<FragmentInfoBinding>(R.layout.fragment_info) {
    private val infoViewModel by activityViewModels<InfoViewModel>()
    var prevNickName = ""
    override fun init() {
    }

    override fun initView() {
    }

    override fun onStart() {
        super.onStart()
        infoViewModel.getMyInfo(App.prefs.userId!!)
    }

    override fun initListener() {
        binding.nicknameEditText.onFocusChangeListener =
            View.OnFocusChangeListener { view, isFocused ->
                if (isFocused) prevNickName = binding.nicknameEditText.text.toString()
            }
        binding.nicknameEditText.setOnKeyListener { view, keyCode, event ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    changeNickname()
                    return@setOnKeyListener true
                }
                else -> return@setOnKeyListener false
            }
        }
        binding.participatingContainer.setOnClickListener {
            val navOptions = navOptions {
                popUpTo(R.id.home) {
                    inclusive = false
                }
            }

            findNavController()
                .navigate(R.id.my_chat, null, navOptions)
        }
        binding.editNicknameButton.setOnClickListener {
            if (binding.nicknameEditText.hasFocus()) {
                changeNickname()
            } else {
                binding.nicknameEditText.requestFocus()
            }
            // request to change nickname

        }

        binding.noticeButton.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://fog-cowl-888.notion.site/63ad9843bb874e5797cff765419f47d7")
            )
            startActivity(browserIntent)
        }
        binding.suggestButton.setOnClickListener {
            val dialog = SuggestDialogFragment(
                normalButtonText = "취소",
                colorButtonText = "건의하기",
                colorButtonListener = { contents ->
                    // suggest!
                    val type = ReportType.FEEDBACK.name
                    Timber.e("suggest, type $type / contents $contents")
                    reportUser(contents, type)
                }
            )
            dialog.show(childFragmentManager, "SuggestDialog")
        }
        binding.reportButton.setOnClickListener {
            val dialog = ReportDialogFragment(
                normalButtonText = "취소",
                colorButtonText = "신고하기",
                colorButtonListener = { type, contents ->
                    // report!
                    Timber.e("report, type $type / contents $contents")
                    reportUser(contents, type)
                }
            )
            dialog.show(childFragmentManager, "ReportDialog")
        }
        binding.termsButton.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://fog-cowl-888.notion.site/b7b93231fbff405084d0a043025189e8")
            )
            startActivity(browserIntent)
        }
        binding.policyButton.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://fog-cowl-888.notion.site/b976365add8f40ecac4a54a5b67d156d")
            )
            startActivity(browserIntent)
        }
        binding.logoutButton.setOnClickListener {
            val dialog = CommonDialogFragment(
                contentText = "로그아웃 하시겠습니까?",
                colorButtonText = "취소",
                normalButtonText = "로그아웃",
                normalButtonListener = {
                    goToLoginScreen(requireContext())
                }
            )
            dialog.show(childFragmentManager, "Dialog")
        }
    }

    private fun changeNickname() {
        Timber.e("${binding.nicknameEditText.text}")
        hideKeyboard(requireActivity())
        binding.nicknameEditText.clearFocus()
        binding.nicknameEditText.clearComposingText()

        // 기존 닉네임과 같으면 요청 x
        Timber.e("!! ${prevNickName} ${binding.nicknameEditText.text}")
        if (prevNickName == binding.nicknameEditText.text.toString()) return
        infoViewModel.changeNickname(App.prefs.userId!!, binding.nicknameEditText.text.toString())
    }

    override fun observeViewModels() {
        infoViewModel.nickname.observe(viewLifecycleOwner) {
            binding.nicknameEditText.setText(it.nickname)
            showShortToast(requireContext(), "닉네임이 변경되었습니다")
        }
        infoViewModel.myInfo.observe(viewLifecycleOwner) {
            binding.participatingTextView.text = "${it.participatingChatCount}건"
            binding.recentOrderTextView.text = "${it.myOrdersCount}건"
            binding.nicknameEditText.setText(it.nickname)
        }
        infoViewModel.mutableErrorType.observe(viewLifecycleOwner){
            Timber.e("gdgdgdgdgd")
            when(it){
                ErrorType.ACCESS_TOKEN_EXPIRED -> {
                    showShortToast(requireContext(), "ACCESS_TOKEN_EXPIRED")
                }
                ErrorType.REFRESH_TOKEN_EXPIRED -> {
                    showShortToast(requireContext(), "REFRESH_TOKEN_EXPIRED")
                }
                ErrorType.TIMEOUT -> {
                    showShortToast(requireContext(), "TIMEOUT")
                }
                ErrorType.NETWORK -> {
                    showShortToast(requireContext(), "NETWORK")
                }
                ErrorType.UNKNOWN -> {
                    showShortToast(requireContext(), "UNKNOWN")
                }
                else -> {
                    showShortToast(requireContext(), "UNKNOWN")
                }
            }
        }
    }

    private fun reportUser(contents: String, type: String) {
        val body = HashMap<String, String>()
        body["user_id"] = App.prefs.userId!!
        body["contents"] = contents
        body["type"] = type

        infoViewModel.report(body)
    }
}