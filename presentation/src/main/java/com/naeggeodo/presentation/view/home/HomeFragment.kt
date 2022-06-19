package com.naeggeodo.presentation.view.home

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentHomeBinding
import com.naeggeodo.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val adapter by lazy { CategoryAdapter(arrayListOf()) }
    private val homeViewModel: HomeViewModel by viewModels()

    override fun init() {
        initView()
        initListener()
        observeViewModels()
    }

    override fun onStart() {
        super.onStart()
        requestCategory()
    }


    override fun initView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun initListener() {
    }

    override fun observeViewModels() {
        homeViewModel.categories.observe(viewLifecycleOwner) {
            Timber.e("$it")
        }
    }


    private fun requestCategory() {
        homeViewModel.getCategories()
    }
}