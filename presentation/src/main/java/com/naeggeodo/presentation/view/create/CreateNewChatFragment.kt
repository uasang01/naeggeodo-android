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
import com.naeggeodo.presentation.databinding.FragmentCreateNewChatBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.dpToPx
import com.naeggeodo.presentation.viewmodel.CreateChatViewModel
import com.naeggeodo.presentation.viewmodel.HomeViewModel
import com.naeggeodo.presentation.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CreateNewChatFragment :
    BaseFragment<FragmentCreateNewChatBinding>(R.layout.fragment_create_new_chat) {
    private val createChatViewModel: CreateChatViewModel by activityViewModels()
//    private val homeViewModel: HomeViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()
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
        binding.createTextView.setOnClickListener {
            if(!checkFieldValidation()) return@setOnClickListener

            Timber.e("buildingCode ${locationViewModel.addressInfo.value!!.second}")
            Timber.e("userId ${App.prefs.userId}")
//            Timber.e()

//            val body = HashMap<String, Any>()
//            body["buildingCode"] = locationViewModel.addressInfo.value!!.second
//            body["category"] = createChatViewModel.category.value!!
//            body["link"] = createChatViewModel.link.value!!
//            body["place"] = createChatViewModel.place.value!!
//            body["title"] = createChatViewModel.chatTitle.value!!
//            body["user_id"] = App.prefs.userId
//            body["tag"] = createChatViewModel.tag.value!!.split(",")
//            body["orderTimeType"] = ""
//            body["maxCount"] = createChatViewModel.maxPeopleNum.value!!
//            createChatViewModel.createChat(body)
        }
    }

    override fun observeViewModels() {

        createChatViewModel.chatTitle.observe(viewLifecycleOwner) {
            createButtonEnable()
        }
        createChatViewModel.category.observe(viewLifecycleOwner) {
            createButtonEnable()
        }
    }

    private fun checkFieldValidation(): Boolean = createChatViewModel.let {
        // how to check building code ?!?
        it.chatTitle.value != null && it.chatTitle.value!!.isNotEmpty() && it.category.value != null
    }

    private fun createButtonEnable() {
        if (checkFieldValidation()) {
            binding.createTextView.background = ColorDrawable(Color.BLACK)
        } else {
            binding.createTextView.background =
                ColorDrawable(ContextCompat.getColor(requireContext(), R.color.grey_E0E0E0))
        }
    }
}
}
