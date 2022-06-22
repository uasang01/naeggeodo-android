package com.naeggeodo.presentation.view.search

import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.naeggeodo.domain.model.Tag
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val tagAdapter by lazy { TagAdapter(requireContext(), arrayListOf()) }
    override fun init() {

    }

    override fun initView() {
        binding.tagRecyclerView.adapter = tagAdapter
        val lm = FlexboxLayoutManager(requireContext())
        lm.flexDirection = FlexDirection.ROW
        lm.justifyContent = JustifyContent.FLEX_START
        binding.tagRecyclerView.layoutManager = lm

        val dump = arrayListOf<Tag>()
        for (i in 0..40) {
            dump.add(Tag("치킨치킨치킨${i * -10}"))
            dump.add(Tag("치${i * -10}"))
        }

        tagAdapter.setData(dump)
    }

    override fun initListener() {

    }

    override fun observeViewModels() {

    }
}