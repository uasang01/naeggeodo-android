package com.naeggeodo.presentation.view.create

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentCreateNewChatBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.Util.persistImage
import com.naeggeodo.presentation.utils.Util.showShortToast
import com.naeggeodo.presentation.utils.dpToPx
import com.naeggeodo.presentation.viewmodel.CreateChatViewModel
import com.naeggeodo.presentation.viewmodel.CreateHistoryViewModel
import com.naeggeodo.presentation.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber


@AndroidEntryPoint
class CreateNewChatFragment :
    BaseFragment<FragmentCreateNewChatBinding>(R.layout.fragment_create_new_chat) {
    private val createChatViewModel: CreateChatViewModel by activityViewModels()
    private val createHistoryViewModel: CreateHistoryViewModel by activityViewModels()

    //    private val homeViewModel: HomeViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()
    private val pagerAdapter by lazy {
        CreatePagerAdapter(
            childFragmentManager,
            lifecycle
        )
    }
    private val tabTitleArray = arrayOf("새로입력", "생성내역")
    private lateinit var orderTimeType: String
    private var tabState = 0    // 0 = TabNewChatFragment, 1 = TabHistoriesFragment
    override fun init() {
        val args: CreateNewChatFragmentArgs by navArgs()
        orderTimeType = args.orderTimeType
    }


    override fun initView() {
        // TabLayout, ViewPager
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.adapter = pagerAdapter

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
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
        createButtonEnable()
    }

    override fun initListener() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabState = position
                createButtonEnable()
                if (tabState == 1) {
                }
            }
        })

        binding.createTextView.setOnClickListener {
            if (!checkFieldValidation()) return@setOnClickListener

            val addr = if (locationViewModel.addressInfo.value == null) {
                if (App.prefs.buildingCode == null) {
                    showShortToast(requireContext(), "위치 정보가 없습니다")
                    return@setOnClickListener
                } else {
                    App.prefs.buildingCode!!
                }
            } else {
                locationViewModel.addressInfo.value!!.second
            }
            if (App.prefs.userId == null) {
                showShortToast(requireContext(), "user id not found error")
                return@setOnClickListener
            }


            try {
                val json = JSONObject()
                val parts = arrayListOf<MultipartBody.Part>()
                when (tabState) {
                    0 -> {
                        json.put("buildingCode", addr)
                        json.put("category", createChatViewModel.category.value!!.category)
                        json.put("title", createChatViewModel.chatTitle.value!!)
                        json.put("user_id", App.prefs.userId!!)
                        json.put("address", App.prefs.address!!)
                        json.put("orderTimeType", orderTimeType)
                        json.put("maxCount", createChatViewModel.maxPeopleNum.value!!)
                        json.put("tag", createChatViewModel.tag.value?.split(","))
                        json.put("link", createChatViewModel.link.value)
                        json.put("place", createChatViewModel.place.value)

                        val body = json.toString()
                            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                        val partJson =
                            MultipartBody.Part.createFormData("chat", "post-chatRooms.json", body)
                        parts.add(partJson)

                        val bitmap = createChatViewModel.chatImage.value
                        bitmap?.let {
                            val file = persistImage(requireContext(), bitmap, "file")
                            val imageBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                            val partImage =
                                MultipartBody.Part.createFormData("file", "file", imageBody)
                            parts.add(partImage)
                        }
                    }
                    1 -> {
                        val chat = createHistoryViewModel.selectedChat.value!!
                        json.put("buildingCode", addr)
                        json.put("category", chat.category)
                        json.put("title", chat.title)
                        json.put("user_id", App.prefs.userId!!)
                        json.put("address", App.prefs.address!!)
                        json.put("orderTimeType", orderTimeType)
                        json.put("maxCount", chat.maxCount)
                        json.put("tag", chat.tags.ifEmpty { null })
                        json.put("link", chat.link)
                        json.put("place", chat.place)

                        val body = json.toString()
                            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                        val partJson =
                            MultipartBody.Part.createFormData("chat", "post-chatRooms.json", body)
                        parts.add(partJson)

                        val bitmap = createChatViewModel.chatImage.value
                        bitmap?.let {
                            val file = persistImage(requireContext(), bitmap, "file")
                            val imageBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                            val partImage =
                                MultipartBody.Part.createFormData("file", "file", imageBody)
                            parts.add(partImage)
                        }
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    val result = createChatViewModel.createChat(parts)
                    if (!result) {
                        showShortToast(requireContext(), "채팅방 생성 실패")
                    }
                }
            } catch (e: Exception) {
                showShortToast(requireContext(), "Error occurred")
                Timber.e("error occurred on request CreateChatApi / $e")
            }
        }
    }

    override fun observeViewModels() {

        createChatViewModel.chatTitle.observe(viewLifecycleOwner) {
            createButtonEnable()
        }
        createChatViewModel.category.observe(viewLifecycleOwner) {
            createButtonEnable()
        }
        createChatViewModel.chatId.observe(viewLifecycleOwner) {
            createChatViewModel.init()
            val action =
                CreateNewChatFragmentDirections.actionCreateNewChatFragmentToChatActivity(it)
            findNavController().navigate(action)
        }
        createHistoryViewModel.selectedChat.observe(viewLifecycleOwner) {
            createButtonEnable()
        }
    }

    private fun checkFieldValidation(): Boolean {
//        if (locationViewModel.addressInfo.value == null){
////            showShortToast("위치 정보가 없습니다")
//            return false
//        }
        when (tabState) {
            0 -> {
                if (createChatViewModel.chatTitle.value == null || createChatViewModel.chatTitle.value!!.isEmpty()) {
//            showShortToast("제목을 입력해 주세요")
                    return false
                }
                if (createChatViewModel.category.value == null) {
//            showShortToast("카테고리를 선택해 주세요")
                    return false
                }
            }
            1 -> {
                return createHistoryViewModel.selectedChat.value != null
            }
        }
        return true
    }

    private fun createButtonEnable() {
        if (checkFieldValidation()) {
            binding.createTextView.background = ColorDrawable(Color.BLACK)
            binding.createTextView.isEnabled = true
        } else {
            binding.createTextView.background =
                ColorDrawable(ContextCompat.getColor(requireContext(), R.color.grey_E0E0E0))
            binding.createTextView.isEnabled = false
        }
    }
}

