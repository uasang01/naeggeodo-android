package com.naeggeodo.presentation.view.mychat

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentMyChatBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.viewmodel.MyChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyChatFragment : BaseFragment<FragmentMyChatBinding>(R.layout.fragment_my_chat) {
    private val myChatViewModel by activityViewModels<MyChatViewModel>()
    private val adapter by lazy { MyChatListAdapter(requireContext(), arrayListOf()) }
    override fun onStart() {
        super.onStart()
        myChatViewModel.getMyChatList(App.prefs.userId!!)
    }

    override fun init() {

    }

    override fun initView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.itemAnimator = null
    }

    override fun initListener() {
        adapter.setListener { chatId ->
            val action = MyChatFragmentDirections.actionMyChatToChatActivity(chatId)
            findNavController().navigate(action)
        }
    }

    override fun observeViewModels() {
        myChatViewModel.chatList.observe(viewLifecycleOwner) {
            adapter.setDatas(ArrayList(it))
        }
    }
}