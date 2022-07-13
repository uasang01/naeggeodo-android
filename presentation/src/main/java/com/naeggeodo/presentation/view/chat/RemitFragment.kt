package com.naeggeodo.presentation.view.chat

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.naeggeodo.domain.model.User
import com.naeggeodo.domain.utils.ChatState
import com.naeggeodo.domain.utils.RemittanceState
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentRemitBinding
import com.naeggeodo.presentation.databinding.ItemNotRemittedUserBinding
import com.naeggeodo.presentation.databinding.ItemRemittedUserBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.viewmodel.ChatViewModel
import com.naeggeodo.presentation.viewmodel.RemitViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemitFragment : BaseFragment<FragmentRemitBinding>(R.layout.fragment_remit) {
    private val chatViewModel: ChatViewModel by activityViewModels()
    private val remitViewModel: RemitViewModel by activityViewModels()
    override fun init() {

    }

    override fun initView() {
    }

    override fun onStart() {
        super.onStart()
        remitViewModel.getUsers(chatViewModel.chatId!!)
    }

    override fun initListener() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.finishChatButton.setOnClickListener {
            chatViewModel.finishChat(chatViewModel.chatId!!, ChatState.END.name)
        }
    }

    override fun observeViewModels() {
        remitViewModel.users.observe(viewLifecycleOwner){
            initUsersView(it)
        }
        remitViewModel.viewEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { event ->
                when(event){
                    RemitViewModel.EVENT_CHANGE_REMITTANCE_STATE_SUCCESS -> {
                        remitViewModel.getUsers(chatViewModel.chatId!!)
                    }
                }
            }
        }

        chatViewModel.viewEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { event ->
                when(event){
                    ChatViewModel.EVENT_CHAT_FINISHED -> {
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun initUsersView(users: List<User>?) {
        binding.notRemittedUsersContainer.removeAllViews()

        val inflater = LayoutInflater.from(requireContext())
        // 송금 한 유저들
        val remittedUsers = users!!.filter { it.remittanceState == RemittanceState.Y.name }
        if(remittedUsers.isEmpty()){
            binding.remittedUsersLayout.visibility = View.GONE
        }else{
            binding.remittedUsersLayout.visibility = View.VISIBLE
        }
        remittedUsers.forEach {
            val itemLayout = ItemRemittedUserBinding.inflate(inflater)
            itemLayout.nicknameTextView.text = it.nickname
            binding.remittedUsersContainer.addView(itemLayout.root)
        }
        // 송금 해야하는 유저들
        val notRemittedUsers = users.filter { it.remittanceState == RemittanceState.N.name }
        if(notRemittedUsers.isEmpty()){
            binding.notRemittedUsersLayout.visibility = View.GONE
        }else{
            binding.notRemittedUsersLayout.visibility = View.VISIBLE
        }
        notRemittedUsers.forEach { user ->
            val itemLayout = ItemNotRemittedUserBinding.inflate(inflater)
            itemLayout.nicknameTextView.text = user.nickname
            itemLayout.remitButton.setOnClickListener {
                remitViewModel.changeRemittanceState(chatViewModel.chatId!!, user.userId)
            }
            binding.notRemittedUsersContainer.addView(itemLayout.root)
        }
    }


}