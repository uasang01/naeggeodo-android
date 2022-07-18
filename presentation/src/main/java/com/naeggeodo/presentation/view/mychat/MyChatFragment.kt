package com.naeggeodo.presentation.view.mychat

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentMyChatBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.Util.showShortToast
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
        adapter.setEditTitleListener { chatId, originTitle ->
            val dialog = ChangeTitleDialogFragment(
                originTitle = originTitle,
                colorButtonListener = { title ->
                    myChatViewModel.changeTitle(chatId, title)
                }
            )
            dialog.show(childFragmentManager, "ChangeTitleDialog")
        }
    }

    override fun observeViewModels() {
        myChatViewModel.chatList.observe(viewLifecycleOwner) {
            adapter.setDatas(ArrayList(it))
        }
        myChatViewModel.chatTitle.observe(viewLifecycleOwner) {
            showShortToast(requireContext(), "채팅방 이름 변경되었습니다")
            adapter.changeData(it)
        }
        myChatViewModel.viewEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    MyChatViewModel.ERROR_CHANGE_TITLE_FAILURE -> {
                        showShortToast(requireContext(), "채팅방 이름 변경 실패")
                    }
                }
            }
        }
    }
}