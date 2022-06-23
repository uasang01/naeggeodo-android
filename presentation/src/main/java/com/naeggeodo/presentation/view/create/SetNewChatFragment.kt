package com.naeggeodo.presentation.view.create

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentSetNewChatBinding
import com.naeggeodo.presentation.utils.dpToPx
import com.naeggeodo.presentation.viewmodel.CreateChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SetNewChatFragment : BaseFragment<FragmentSetNewChatBinding>(R.layout.fragment_set_new_chat) {
    private val createChatViewModel: CreateChatViewModel by activityViewModels()

    private val tabTitleArray = arrayOf("새로입력", "주문목록")
    override fun init() {

    }

    override fun initView() {
        // TabLayout, ViewPager
        val viewPager = binding.viewPager
        viewPager.adapter = CreatePagerAdapter(requireActivity().supportFragmentManager, lifecycle)

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        // padding 양쪽 0으로 한 후 오른쪽 margin 줌
        val tabs = tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabs.childCount) {
            val tab = tabs.getChildAt(i)
            val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 1f
            layoutParams.marginEnd = 20.dpToPx(requireContext())
            layoutParams.marginStart = 0.dpToPx(requireContext())
            tab.layoutParams = layoutParams
            tabLayout.requestLayout()
        }
    }

    override fun initListener() {
    }

    override fun observeViewModels() {

        createChatViewModel.restaurantName.observe(viewLifecycleOwner){ str ->

            if (str.isNotEmpty()){
                binding.createTextView.background = ColorDrawable(Color.BLACK)
            }else{
                binding.createTextView.background = ColorDrawable(ContextCompat.getColor(requireContext(), R.color.grey_E0E0E0))
            }

        }
    }
}
