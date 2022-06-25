package com.naeggeodo.presentation.view.search

import android.app.Activity
import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentSearchBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.Util.hideKeyboard
import com.naeggeodo.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val tagAdapter by lazy { TagAdapter(requireContext(), arrayListOf()) }
    private val chatListAdapter by lazy { ChatListAdapter(requireContext(), arrayListOf()) }

    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()
        searchViewModel.getTags()
    }

    override fun init() {

    }

    override fun initView() {
        // search bar
        binding.searchBarText.hint = "검색어를 입력해 주세요"

        // tags recyclerview
        binding.tagRecyclerView.adapter = tagAdapter
        val lm = FlexboxLayoutManager(requireContext())
        lm.flexDirection = FlexDirection.ROW
        lm.justifyContent = JustifyContent.FLEX_START
        binding.tagRecyclerView.layoutManager = lm
//        val dump = arrayListOf<Tag>()
//        for (i in 0..40) {
//            dump.add(Tag(1,"치킨치킨치킨${i * -10}"))
//            dump.add(Tag(1,"치${i * -10}"))
//        }
//
//        tagAdapter.setData(dump)

        // chats recyclerview
        binding.chatListRecyclerView.adapter = chatListAdapter
        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun initListener() {
        // 엔터를 누르면 키보드 내림
        binding.searchBarText.setOnKeyListener { view, keyCode, event ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    Timber.e("${binding.searchBarText.text}")
                    val searchType = "search"
                    val keyWord = binding.searchBarText.text.toString()

                    searchChatList(searchType, keyWord)
                    hideKeyboard(activity as Activity)

                    return@setOnKeyListener true
                }
//                KeyEvent.KEYCODE_BACK -> {
//                    screenChanger(false)
//                    hideKeyboard(activity as Activity)
//
//                    return@setOnKeyListener true
//                }
                else -> return@setOnKeyListener false
            }
        }
        binding.searchBarText.onFocusChangeListener =
            View.OnFocusChangeListener { view, isFocused ->
                Timber.e("view:$view isFocused:$isFocused")

            }
    }

    override fun observeViewModels() {
        searchViewModel.tags.observe(viewLifecycleOwner) {
            tagAdapter.setData(ArrayList(it))
        }
        searchViewModel.chatList.observe(viewLifecycleOwner) {
            chatListAdapter.setData(ArrayList(it))
            recyclerViewChanger(false)
        }
    }

    private fun searchChatList(searchType: String, keyWord: String) =
        searchViewModel.searchChatList(searchType, keyWord)

    private fun recyclerViewChanger(showTags: Boolean) {
        if (showTags) {
            binding.chatListRecyclerView.visibility = View.GONE
            binding.tagRecyclerView.visibility = View.VISIBLE

        } else {
            binding.tagRecyclerView.visibility = View.GONE
            binding.chatListRecyclerView.visibility = View.VISIBLE
        }
    }

}