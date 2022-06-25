package com.naeggeodo.presentation.view.create

import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentCreateNewBinding
import com.naeggeodo.presentation.viewmodel.CreateChatViewModel
import com.naeggeodo.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNewFragment : BaseFragment<FragmentCreateNewBinding>(R.layout.fragment_create_new) {
    private val createChatViewModel: CreateChatViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun init() {

    }

    override fun initView() {


    }

    override fun initListener() {
        binding.restaurantNameEditText.addTextChangedListener { text ->
            createChatViewModel.setRestaurantName(text.toString())
        }
        binding.categoryBox.setOnClickListener {
            val categories = homeViewModel.categories.value?.categories
            categories?.let {
                CategoryDialogFragment(it)
                    .show(parentFragmentManager, "addressDialog")
            }

        }
        binding.linkEditText.addTextChangedListener { text ->
            createChatViewModel.setLink(text.toString())
        }
        binding.tagEditText.addTextChangedListener { text ->
            createChatViewModel.setTag(text.toString())
        }
        binding.addButton.setOnClickListener {
            var num = binding.peopleCountTextView.text.toString().toInt()
            // 5 일 때 비활성화
            if (num >= 5) return@setOnClickListener

            // 1 일 때 더하는 순간 뺄 수 있으므로 빼기 비활성화
            if (num <= 1) {
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
            // 더하고 5이상이 되면 더하기 버튼 비활성화
            if (num >= 5) {
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
            if (num <= 1) return@setOnClickListener
            if (num >= 5) {
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
            if (num <= 1) {
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