package com.naeggeodo.presentation.view.home

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.utils.CategoryType
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentHomeBinding
import com.naeggeodo.presentation.utils.Util
import com.naeggeodo.presentation.utils.Util.showShortSnackbar
import com.naeggeodo.presentation.viewmodel.HomeViewModel
import com.naeggeodo.presentation.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val categoryAdapter by lazy { CategoryAdapter(arrayListOf()) }
    private val chatListAdapter by lazy { ChatListAdapter(requireContext(), arrayListOf()) }
    private val homeViewModel: HomeViewModel by viewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()

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
        binding.serachBarCardview.setOnClickListener {
            //사용하기 위해 버튼을 클릭하면 다이얼로그가 실행되게

            //인터넷 연결 확인
            if (Util.isNetworkConnected(requireContext())) {
                //주소검색 웹 뷰를 띄울 DialogFragment 선언
                AddressSearchDialogFragment().show(parentFragmentManager, "addressDialog")
            } else {
                showShortSnackbar(binding.root, "인터넷 연결을 확인해주세요.")
            }
        }
    }

    override fun observeViewModels() {
        homeViewModel.categories.observe(viewLifecycleOwner) { value ->
            value.categories.map {
                Timber.e(it.category)
            }
            val list = arrayListOf<String>()
            list.addAll(value.categories.map { enumValueOf<CategoryType>(it.category).korean })

            categoryAdapter.setData(list)
        }

        locationViewModel.addressInfo.observe(viewLifecycleOwner) { value ->
            Timber.e("address $value")
            val address = value.first
            val buildingCode = value.second
            val apartment = value.third

            if (apartment == "Y") {
                binding.searchBarText.text = address
                requestChatList(buildingCode = buildingCode)
            } else {
                binding.searchBarText.text = getText(R.string.not_apartment)
            }
        }

        homeViewModel.chatList.observe(viewLifecycleOwner) { value ->
            Timber.e("$value")
            chatListAdapter.setData(ArrayList(value))
        }

    }

    private fun requestChatList(category: String? = null, buildingCode: String) {
        homeViewModel.getChatList(category, buildingCode)
    }

    private fun requestCategory() {
        homeViewModel.getCategories()
    }
}