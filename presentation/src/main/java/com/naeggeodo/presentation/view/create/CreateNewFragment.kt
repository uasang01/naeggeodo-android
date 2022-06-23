package com.naeggeodo.presentation.view.create

import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentCreateNewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNewFragment : BaseFragment<FragmentCreateNewBinding>(R.layout.fragment_create_new) {
    override fun init() {
    }

    override fun initView() {
    }

    override fun initListener() {
        binding.categoryBox.setOnClickListener {

        }
    }

    override fun observeViewModels() {
    }
}