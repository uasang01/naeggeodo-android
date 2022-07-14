package com.naeggeodo.presentation.view.create

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 2

class CreatePagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    private val tabNewFragment by lazy { TabNewFragment() }
    private val tabHistoriesFragment by lazy { TabHistoriesFragment() }
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> tabNewFragment
            1 -> tabHistoriesFragment
            else -> tabNewFragment
        }
    }
}