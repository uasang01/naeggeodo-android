package com.naeggeodo.presentation.view.home

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.naeggeodo.domain.utils.ApartmentFlag
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentHomeBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.Util
import com.naeggeodo.presentation.utils.Util.showShortToast
import com.naeggeodo.presentation.viewmodel.ChatViewModel
import com.naeggeodo.presentation.viewmodel.CreateChatViewModel
import com.naeggeodo.presentation.viewmodel.HomeViewModel
import com.naeggeodo.presentation.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val categoryAdapter by lazy { CategoryAdapter(requireContext(), arrayListOf()) }
    private var chatListAdapter: ChatListAdapter? = null
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val chatViewModel: ChatViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()
    private val createChatViewModel: CreateChatViewModel by activityViewModels()
    private var refreshedTime = System.currentTimeMillis()

    override fun init() {
        if (chatListAdapter == null) {
            chatListAdapter = ChatListAdapter(requireContext(), arrayListOf())
        }
    }

    override fun onStart() {
        super.onStart()
        requestCategory()

        App.prefs.buildingCode?.let {
            locationViewModel.setAddressInfo(
                Triple(
                    App.prefs.address!!,
                    App.prefs.buildingCode!!,
                    App.prefs.apartment ?: ApartmentFlag.Y.name
                )
            )
        }

        homeViewModel.getMyInfo(App.prefs.userId!!)
    }


    override fun initView() {
        // 채팅 바 텍스트
        Timber.e("adr ${App.prefs.address}")
        if (App.prefs.address == null) {
            binding.searchBarText.text = getText(R.string.not_apartment)
        } else {
            binding.searchBarText.text = App.prefs.address
        }

        // 카테고리 텝
        binding.categoryRecyclerView.adapter = categoryAdapter
        val lm = LinearLayoutManager(requireContext())
        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.categoryRecyclerView.layoutManager = lm
        binding.categoryRecyclerView.itemAnimator = null

        // 채팅방 리스트
        binding.chatListRecyclerView.adapter = chatListAdapter
        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatListRecyclerView.itemAnimator = null
    }

    override fun initListener() {
        chatListAdapter?.setListener { pos ->
            val chat = chatListAdapter!!.getData(pos)

            Timber.e("chat info : ${chat.chatId} ${chat.title} ${chat.userId}")

            val action = HomeFragmentDirections.actionHomeToChatActivity(chat.chatId)
//            val action = HomeFragmentDirections.actionHomeToNavigationChat()
            findNavController().navigate(action)
        }

        binding.searchBarText.setOnClickListener {
            showSearchAddressDialog()
        }

        categoryAdapter.setItemClickEvent { pos ->
            locationViewModel.addressInfo.value?.let { value ->
                val address = value.first
                val buildingCode = value.second
                val apartment = value.third
                if (apartment == ApartmentFlag.N.name) {
                    showShortToast(requireContext(), getString(R.string.not_apartment))
                    return@setItemClickEvent
                }
                requestChatList(
                    category = categoryAdapter.getSelectedCategory(),
                    buildingCode = buildingCode
                )
                return@setItemClickEvent
            }

            showShortToast(requireContext(), getString(R.string.not_apartment))
        }

        binding.chatListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                val itemCount = chatListAdapter!!.itemCount
                Timber.d("$itemCount")

                // 스크롤이 끝에 도달했는지 확인
                if (
                    !recyclerView.canScrollVertically(1) &&
                    lastVisibleItemPosition == itemCount - 1
                ) {
//                    Timber.d("touched chat list end")
                }
                // 스크롤이 처음에 도달했는지 확인
                val firstVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (homeViewModel.mutableScreenState.value != ScreenState.LOADING
                    && !recyclerView.canScrollVertically(-1)
                    && firstVisibleItemPosition == 0
                ) {
//                    Timber.d("touched chat list top")
//                    refreshChatList()
                }
            }
        })

        binding.noneResult.createChatButton.setOnClickListener {
            val navOptions = navOptions {
                popUpTo(R.id.home) { inclusive = false }
            }

            findNavController().navigate(R.id.create, null, navOptions)
        }
        binding.noneAddress.searchAddressButton.setOnClickListener {
            showSearchAddressDialog()
        }
    }

    private fun showSearchAddressDialog() {
        // 인터넷 연결 확인
        if (Util.isNetworkConnected(requireContext())) {
            // 주소검색 웹 뷰를 띄울 DialogFragment 로 이동
            AddressSearchDialogFragment().show(parentFragmentManager, "addressDialog")
        } else {
            showShortToast(requireContext(), "인터넷 연결을 확인해주세요.")
        }
    }

    override fun observeViewModels() {
        homeViewModel.chatList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.noneResult.root.visibility = View.VISIBLE
            } else {
                binding.noneResult.root.visibility = View.INVISIBLE
            }
            chatListAdapter!!.setDatas(ArrayList(it))
        }
        homeViewModel.myNickName.observe(viewLifecycleOwner) {
            App.prefs.nickname = it.nickname
        }

        homeViewModel.categories.observe(viewLifecycleOwner) { value ->
            binding.categoryRecyclerView.post {
                categoryAdapter.setData(ArrayList(value.categories))
            }
        }

        homeViewModel.mutableScreenState.observe(viewLifecycleOwner) { state ->
            val layout = binding.loadingView.root
            val view = binding.loadingView.progressImage
            when (state!!) {
                ScreenState.LOADING -> Util.loadingAnimation(requireContext(), layout, view, true)
                ScreenState.RENDER -> Util.loadingAnimation(requireContext(), layout, view, false)
                ScreenState.ERROR -> Util.loadingAnimation(requireContext(), layout, view, false)
            }
        }
        locationViewModel.mutableScreenState.observe(viewLifecycleOwner) { state ->
            val layout = binding.loadingView.root
            val view = binding.loadingView.progressImage
            when (state!!) {
                ScreenState.LOADING -> Util.loadingAnimation(requireContext(), layout, view, true)
                ScreenState.RENDER -> Util.loadingAnimation(requireContext(), layout, view, false)
                ScreenState.ERROR -> Util.loadingAnimation(requireContext(), layout, view, false)
            }
        }

        locationViewModel.addressInfo.observe(viewLifecycleOwner) { value ->
            if (value.third == ApartmentFlag.N.name) {
                chatListAdapter!!.clearData()
                binding.noneResult.root.visibility = View.GONE
                binding.noneAddress.root.visibility = View.VISIBLE
                binding.searchBarText.text = getText(R.string.not_apartment)
                showShortToast(requireContext(), getString(R.string.not_apartment))
            } else {
                addressInfoUpdated()
                binding.noneAddress.root.visibility = View.GONE
            }
        }
    }

    private fun addressInfoUpdated() {
        locationViewModel.addressInfo.value?.let { v ->
            Timber.e("address $v")
            val address = v.first
            val buildingCode = v.second
            val apartment = v.third

            App.prefs.address = address
            App.prefs.buildingCode = buildingCode
            App.prefs.apartment = apartment

            val textview = binding.searchBarText
            textview.text = address
            requestChatList(
                category = categoryAdapter.getSelectedCategory(),
                buildingCode = buildingCode
            )
        }
    }

    private fun refreshChatList() {
        // swipe listener를 이용하여 구현해야 할 것 같음
        Timber.e("Refresh chat list")
        if (chatListAdapter == null && App.prefs.buildingCode == null) return

        val diff = System.currentTimeMillis() - refreshedTime
        Timber.e("diffffff $diff")
        if (diff > 2000) {
            Util.toast?.cancel()

            refreshedTime = System.currentTimeMillis()
            binding.chatListRecyclerView.post {
                chatListAdapter!!.clearData()
            }
            requestChatList(
                category = categoryAdapter.getSelectedCategory(),
                buildingCode = App.prefs.buildingCode!!
            )
        }
//        else {
//            Util.showShortToast(requireContext(), "잠시 후 다시 시도해주세요")
//        }

    }

    private fun requestChatList(category: String? = null, buildingCode: String) {
        homeViewModel.getChatList(category, buildingCode)
    }

    private fun requestCategory() {
        homeViewModel.getCategories()
    }

    override fun onStop() {
        binding.chatListRecyclerView.post {
            chatListAdapter!!.clearData()
        }
        super.onStop()
    }
}