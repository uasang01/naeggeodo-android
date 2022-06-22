package com.naeggeodo.presentation.view.create

import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentCreateBinding

class CreateFragment : BaseFragment<FragmentCreateBinding>(R.layout.fragment_create) {
    override fun init() {

    }

    override fun initView() {
        super.initView()
    }

    override fun initListener() {
        super.initListener()
        binding.writeSelfButton.setOnClickListener {
            showShortToast("직접 입력")
        }
        binding.oneHourButton.setOnClickListener {
            showShortToast("1시간 이내")
        }
        binding.quickButton.setOnClickListener {
            showShortToast("최대한 빨리")
        }
        binding.freedomButton.setOnClickListener {
            showShortToast("상관없음")
        }
    }

    override fun observeViewModels() {
        super.observeViewModels()
    }
}