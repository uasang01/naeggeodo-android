package com.naeggeodo.presentation.view.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentSetNewChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetNewChatFragment : BaseFragment<FragmentSetNewChatBinding>(R.layout.fragment_set_new_chat) {
    private val tabTitleArray = arrayOf("새로입력", "주문목록")
    override fun init() {

    }

    override fun initView() {

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter = CreatePagerAdapter(activity!!.supportFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    override fun initListener() {
    }

    override fun observeViewModels() {
    }
}