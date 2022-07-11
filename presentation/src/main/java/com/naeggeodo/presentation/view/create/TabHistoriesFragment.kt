package com.naeggeodo.presentation.view.create

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentTabHistoriesBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.Util
import com.naeggeodo.presentation.utils.Util.showShortToast
import com.naeggeodo.presentation.viewmodel.CreateHistoryViewModel
import com.naeggeodo.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class TabHistoriesFragment :
    BaseFragment<FragmentTabHistoriesBinding>(R.layout.fragment_tab_histories) {
    val adapter: ChatHistoryAdapter by lazy { ChatHistoryAdapter(requireContext(), arrayListOf()) }
    val homeViewModel: HomeViewModel by activityViewModels()
    private val createHistoryViewModel: CreateHistoryViewModel by activityViewModels()

    override fun init() {

    }

    override fun onStart() {
        super.onStart()
        App.prefs.userId?.let { createHistoryViewModel.getCreationHistories(it) }
    }

    override fun initView() {
        // 채팅방 리스트
        binding.historyRecyclerView.adapter = adapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.historyRecyclerView.itemAnimator = null
    }

    override fun initListener() {
        adapter.setFavoriteListener { pos ->
            val chat = adapter.getData(pos)
            CoroutineScope(Dispatchers.IO).launch {
                val result = createHistoryViewModel.bookmarking(
                    chat.chatId,
                    App.prefs.userId!!
                )
                if (result) {
                    binding.historyRecyclerView.post {
                        adapter.updateBookmark(pos)
                    }
                }
            }

        }
        adapter.setDeleteListener { pos ->
            CoroutineScope(Dispatchers.IO).launch {
                val result = createHistoryViewModel.deleteHistory(adapter.getData(pos).chatId)
                withContext(Dispatchers.Main){
                    if (result) {
                        adapter.deleteData(pos)
                    }else{
                        showShortToast(requireContext(), "삭제 실패")
                    }
                }
            }
        }
    }

    override fun observeViewModels() {
        createHistoryViewModel.chatList.observe(viewLifecycleOwner) {
            binding.historyRecyclerView.post {
                adapter.setData(ArrayList(it))
            }
        }
        createHistoryViewModel.mutableScreenState.observe(viewLifecycleOwner) { state ->
            val layout = binding.loadingView.root
            val view = binding.loadingView.progressImage
            when (state!!) {
                ScreenState.LOADING -> Util.loadingAnimation(requireContext(), layout, view, true)
                ScreenState.RENDER -> Util.loadingAnimation(requireContext(), layout, view, false)
                ScreenState.ERROR -> Util.loadingAnimation(requireContext(), layout, view, false)
            }
        }
        createHistoryViewModel.deleteResponse.observe(viewLifecycleOwner) { response ->
            if (response.deleted) {
                adapter.deleteData(response.chatId)
            }
        }
    }
}