package com.naeggeodo.presentation.view.create

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentTabHistoriesBinding
import com.naeggeodo.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabHistoriesFragment :
    BaseFragment<FragmentTabHistoriesBinding>(R.layout.fragment_tab_histories) {
    val adapter: ChatHistoryAdapter by lazy { ChatHistoryAdapter(requireContext(), arrayListOf()) }
    val homeViewModel: HomeViewModel by activityViewModels()

    override fun init() {

    }

    override fun initView() {
        // 채팅방 리스트
        binding.historyRecyclerView.adapter = adapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun initListener() {
    }

    override fun observeViewModels() {
    }

}