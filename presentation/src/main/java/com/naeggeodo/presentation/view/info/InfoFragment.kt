package com.naeggeodo.presentation.view.info

import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import com.naeggeodo.domain.utils.ReportType
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentInfoBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.Util.hideKeyboard
import com.naeggeodo.presentation.utils.Util.showShortToast
import com.naeggeodo.presentation.view.CommonDialogFragment
import com.naeggeodo.presentation.view.LoginActivity
import com.naeggeodo.presentation.viewmodel.InfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class InfoFragment : BaseFragment<FragmentInfoBinding>(R.layout.fragment_info) {
    val infoViewModel by activityViewModels<InfoViewModel>()
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
                if (isFocused) {
                    binding.editNicknameButton.visibility = View.VISIBLE
                } else {
                    binding.editNicknameButton.visibility = View.INVISIBLE
                }
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
        binding.editNicknameButton.setOnClickListener {
            // request to change nickname
            changeNickname()
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
                    Timber.e("type $type / contents $contents")
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
                    Timber.e("type $type / contents $contents")
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
                    App.prefs.clearAll()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
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
    }
}