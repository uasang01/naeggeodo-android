package com.naeggeodo.presentation.view.create

import android.app.Activity
import android.view.KeyEvent
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentCreateNewBinding
import com.naeggeodo.presentation.utils.Util.hideKeyboard
import com.naeggeodo.presentation.viewmodel.CreateChatViewModel
import com.naeggeodo.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val PEOPLE_MAX = 5
private const val PEOPLE_MIN = 2

@AndroidEntryPoint
class CreateNewFragment : BaseFragment<FragmentCreateNewBinding>(R.layout.fragment_create_new) {
    val createChatViewModel: CreateChatViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun init() {

    }

    override fun initView() {
        binding.fragment = this

        createChatViewModel.setCategory(null)
        binding.createChatViewModel = createChatViewModel
    }

    override fun initListener() {
        binding.chatTitleEditText.addTextChangedListener { text ->
            createChatViewModel.setChatTitle(text.toString())
        }
        binding.categoryBox.setOnClickListener {
            val categories = homeViewModel.categories.value?.categories
            categories?.let {
                CategoryDialogFragment(it)
                    .show(parentFragmentManager, "categoryDialog")
            }
        }
        binding.placeEditText.addTextChangedListener { text ->
            createChatViewModel.setPlace(text.toString())
        }

        binding.linkEditText.addTextChangedListener { text ->
            createChatViewModel.setLink(text.toString())
        }

        binding.tagEditText.addTextChangedListener { text ->
            createChatViewModel.setTag(text.toString())
        }
        // 마지막 EditText. 엔터를 누르면 키보드 내림
        binding.tagEditText.setOnKeyListener { view, keyCode, event ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    hideKeyboard(activity as Activity)

                    return@setOnKeyListener true
                }
                else -> return@setOnKeyListener false
            }
        }
        binding.addButton.setOnClickListener {
            var num = binding.peopleCountTextView.text.toString().toInt()
            // PEOPLE_MAX 일 때 비활성화
            if (num >= PEOPLE_MAX) return@setOnClickListener

            // PEOPLE_MIN 일 때 더하는 순간 뺄 수 있으므로 빼기 비활성화
            if (num <= PEOPLE_MIN) {
                binding.subtractButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_subtract_black
                    )
                )
            }
            num += 1
            binding.peopleCountTextView.text = num.toString()
            createChatViewModel.setMaxPeopleNum(num)
            // 더하고 PEOPLE_MAX이상이 되면 더하기 버튼 비활성화
            if (num >= PEOPLE_MAX) {
                binding.addButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_add_grey
                    )
                )
            }
        }
        binding.subtractButton.setOnClickListener {
            var num = binding.peopleCountTextView.text.toString().toInt()
            if (num <= PEOPLE_MIN) return@setOnClickListener
            if (num >= PEOPLE_MAX) {
                binding.addButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_add_black
                    )
                )
            }
            num -= 1
            binding.peopleCountTextView.text = num.toString()
            createChatViewModel.setMaxPeopleNum(num)
            if (num <= PEOPLE_MIN) {
                binding.subtractButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_subtract_grey
                    )
                )
            }
        }
    }

    override fun observeViewModels() {
    }
}