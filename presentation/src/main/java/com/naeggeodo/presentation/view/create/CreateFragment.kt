package com.naeggeodo.presentation.view.create

import androidx.navigation.fragment.findNavController
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentCreateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFragment : BaseFragment<FragmentCreateBinding>(R.layout.fragment_create) {
    override fun init() {

    }

    override fun initListener() {
        binding.writeSelfButton.setOnClickListener {
            val action = CreateFragmentDirections.actionCreateToSetNewChatFragment()
            findNavController().navigate(action)
        }
        binding.oneHourButton.setOnClickListener {
            val action = CreateFragmentDirections.actionCreateToSetNewChatFragment()
            findNavController().navigate(action)
        }
        binding.quickButton.setOnClickListener {
            val action = CreateFragmentDirections.actionCreateToSetNewChatFragment()
            findNavController().navigate(action)
        }
        binding.freedomButton.setOnClickListener {
            val action = CreateFragmentDirections.actionCreateToSetNewChatFragment()
            findNavController().navigate(action)
        }
    }

    override fun observeViewModels() {

    }
}