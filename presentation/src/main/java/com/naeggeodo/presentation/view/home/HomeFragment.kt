package com.naeggeodo.presentation.view.home

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.naeggeodo.domain.utils.CategoryType
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentHomeBinding
import com.naeggeodo.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val categoryAdapter by lazy { CategoryAdapter(arrayListOf()) }
    private val chatListAdapter by lazy { ChatListAdapter(arrayListOf()) }
    private val homeViewModel: HomeViewModel by viewModels()

    override fun init() {
    }

    override fun onStart() {
        super.onStart()
        requestCategory()
    }


    override fun initView() {
        binding.categoryRecyclerView.adapter = categoryAdapter
        val lm = LinearLayoutManager(requireContext())
        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.categoryRecyclerView.layoutManager = lm

        binding.chatListRecyclerView.adapter = chatListAdapter
        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun initListener() {
    }

    override fun observeViewModels() {
        homeViewModel.categories.observe(viewLifecycleOwner) { value ->
            value.categories.map {
                Timber.e("${it.category}")
            }
            val list = arrayListOf<String>()
            list.addAll(value.categories.map { enumValueOf<CategoryType>(it.category).korean })

            categoryAdapter.setData(list)
        }
    }


    private fun requestCategory() {
        homeViewModel.getCategories()
    }
}