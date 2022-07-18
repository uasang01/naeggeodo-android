package com.naeggeodo.presentation.view.chat

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.naeggeodo.domain.model.ChatHistory
import com.naeggeodo.domain.model.User
import com.naeggeodo.domain.utils.ChatDetailType
import com.naeggeodo.presentation.BuildConfig
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.data.Message
import com.naeggeodo.presentation.databinding.*
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.Util
import com.naeggeodo.presentation.utils.Util.decodeString
import com.naeggeodo.presentation.utils.Util.encodeImage
import com.naeggeodo.presentation.utils.Util.getMessageTimeString
import com.naeggeodo.presentation.utils.Util.loadImageAndSetView
import com.naeggeodo.presentation.utils.Util.showShortToast
import com.naeggeodo.presentation.utils.Util.showSnackBar
import com.naeggeodo.presentation.utils.dpToPx
import com.naeggeodo.presentation.view.CommonDialogFragment
import com.naeggeodo.presentation.viewmodel.ChatViewModel
import org.json.JSONObject
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId


class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat),
    ChatActivity.OnBackPressedListener {
    private val chatViewModel: ChatViewModel by activityViewModels()

    private val galleryAdapter by lazy { GalleryAdapter(requireContext(), arrayListOf()) }

    private val DRAWER_GALLERY_ITEM_MAX = 5

    private var totalImageSize = -1
    private var encodedImageString = ""
    private var quickChatDialog: QuickChatBottomDialogFragment? = null
    private var isGalleryVisible = false

    private var permissionLauncher: ActivityResultLauncher<String>? = null

    override fun init() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                showGallery()
            } else {
                showSnackBar(binding.root,"사진에 접근하기 위해 권한이 필요합니다\n권한을 허용해 주세요", "허용하기"){
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.data = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        chatViewModel.getChatInfo()
        if (!chatViewModel.stompClient.isConnected()) {
            chatViewModel.runStomp()
        } else {
            chatViewModel.getChatHistory()
            chatViewModel.getQuickChats(App.prefs.userId!!)
        }
    }


    override fun initView() {
        super.initView()

        binding.galleryRecyclerview.adapter = galleryAdapter
        val lm = LinearLayoutManager(requireContext())
        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.galleryRecyclerview.layoutManager = lm


        // make recycler view not flickering
        binding.galleryRecyclerview.itemAnimator = null

        // initialize bottom sheet dialog
        val phraseClickListener: (String) -> Unit = { str ->
            Timber.e("hihi $str")
            sendMessage(str, ChatDetailType.TEXT)
        }
        val updateListener: (List<String?>) -> Unit = { list ->
            Timber.e("hihi $list")
            val body = HashMap<String, List<String?>>()
            body["quickChat"] = list
            chatViewModel.updateQuickChats(App.prefs.userId!!, body)
        }
        quickChatDialog = QuickChatBottomDialogFragment(
            phraseClickListener = phraseClickListener,
            updateListener = updateListener
        )
    }

    override fun initListener() {
        binding.backButton.setOnClickListener {
            requireActivity().finish()
        }
        binding.hambergerButton.setOnClickListener {
            val drawerLayout = binding.drawerLayout
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        }

        binding.checkDepositButton.setOnClickListener {
            val action = ChatFragmentDirections.actionChatFragmentToRemitFragment()
            findNavController().navigate(action)
        }

        binding.drawer.exitChatButton.setOnClickListener {
            val dialog = CommonDialogFragment(
                contentText = "정말 나가시겠습니까?",
                normalButtonText = "취소",
                colorButtonText = "나가기",
                colorButtonListener = {
                    chatViewModel.exitChat()
                    requireActivity().finish()
                }
            )
            dialog.show(childFragmentManager, "Dialog")
        }

        binding.sendMessageButton.setOnClickListener {
            if (isGalleryVisible) {
                //send image
                val uriString = galleryAdapter.getSelectedPicture()
                if (uriString != null) {
                    val encodedString =
                        encodeImage(uriString).replace("\n", "")    // encoded with Base64
                    val chopSize = 10240
                    val loopCount = encodedString.length / chopSize + 1
                    sendMessage(
                        encodedString.length.toString(),
                        ChatDetailType.IMAGE
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        for (i in 0 until loopCount) {
                            if (i != loopCount - 1) {
                                sendMessage(
                                    encodedString.slice(i * chopSize until (i + 1) * chopSize),
                                    ChatDetailType.IMAGE
                                )
                            } else {
                                sendMessage(
                                    encodedString.slice(i * chopSize until encodedString.length),
                                    ChatDetailType.IMAGE
                                )
                            }
                        }
                    }, 100)
                    galleryAdapter.clearSelected()
                    showGallery()

                    return@setOnClickListener
                }
            }
            // send text
            sendMessage(binding.messageEdittext.text.toString(), ChatDetailType.TEXT)
        }

        binding.quickChatDrawerButton.setOnClickListener {
            quickChatDialog!!.show(parentFragmentManager, "QuickChatDialog")
            quickChatDialog!!.dialog?.window?.attributes?.gravity = Gravity.BOTTOM
        }

        binding.showGalleryButton.setOnClickListener {
            // 권한 체크
//            val permissions = HashMap<String, String>()
//            permissions["storageRead"] = Manifest.permission.READ_EXTERNAL_STORAGE
//            val result = PermissionHelper.checkPermission(requireActivity(), permissions)

            checkPermission()
        }
        galleryAdapter.setItemClickEvent { pos ->
            // 없어도 됨
            val uri = galleryAdapter.getSelectedPicture()
            Timber.e("uri $uri")
        }
    }

    private fun checkPermission() {
        permissionLauncher?.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    override fun observeViewModels() {
        chatViewModel.viewEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ChatViewModel.FAILED_TO_SEND_MESSAGE -> {
                        showShortToast(requireContext(), "메세지 전송 실패")
                    }
                    ChatViewModel.ERROR_BANNED_FROM_CHAT -> {
                        requireActivity().finish()
                        showShortToast(requireContext(), "추방당한 방입니다")
                    }
                    ChatViewModel.ERROR_SESSION_DUPLICATION -> {
                        showShortToast(requireContext(), "중복된 접속입니다")
                        requireActivity().finish()
                    }
                    ChatViewModel.ERROR_UNAUTHORIZED -> {
                        showShortToast(requireContext(), "인증되지 않은 아이디 입니다")
                        requireActivity().finish()
                    }
                    ChatViewModel.ERROR_INVALID_STATE -> {
                        showShortToast(requireContext(), "입장할 수 없습니다")
                        requireActivity().finish()
                    }
                    ChatViewModel.ERROR_INVALID_ACCESS -> {
                        showShortToast(requireContext(), "잘못된 접근입니다")
                        requireActivity().finish()
                    }
                    ChatViewModel.ERROR_BAD_REQUEST -> {
                        showShortToast(requireContext(), "잘못된 요청입니다")
                        requireActivity().finish()
                    }
                }
            }
        }
        chatViewModel.mutableScreenState.observe(viewLifecycleOwner) { state ->
            val layout = binding.loadingView.root
            val view = binding.loadingView.progressImage
            when (state!!) {
                ScreenState.LOADING -> Util.loadingAnimation(requireContext(), layout, view, true)
                ScreenState.RENDER -> Util.loadingAnimation(requireContext(), layout, view, false)
                ScreenState.ERROR -> Util.loadingAnimation(requireContext(), layout, view, false)
            }
        }

        chatViewModel.chatInfo.observe(viewLifecycleOwner) { chat ->
            Timber.e("chat received ${chat}")

            // 헤더 뷰 설정
            binding.chatTitleText.text = chat.title
            loadImageAndSetView(requireContext(), chat.imgPath, binding.chatImage)
            binding.numOfPeople.text = "인원 ${chat.currentCount}명 / ${chat.maxCount}명"

            val masterId = chatViewModel.chatInfo.value!!.userId
            if (masterId == App.prefs.userId) {
                binding.checkDepositButton.visibility = View.VISIBLE
            }
        }

        chatViewModel.users.observe(viewLifecycleOwner) { users ->
            Timber.e("users / $users")
            initDrawerView(users)
        }

        chatViewModel.history.observe(viewLifecycleOwner) { historyList ->
            Timber.e("history received size: ${historyList.size}")
            historyList.forEach { h ->
                Timber.e("history received ${h}")
                if (h.userId == App.prefs.userId) {
                    when (h.type) {
                        ChatDetailType.TEXT.name -> {
                            addMyMsgView(h.contents, LocalDateTime.parse(h.regDate))
                        }
                        ChatDetailType.IMAGE.name -> {
                            imageReceiver(h)
                        }
                        ChatDetailType.WELCOME.name -> {
                            addNoticeView("${h.nickname}${h.contents}")
                        }
                        ChatDetailType.EXIT.name -> {
                            addNoticeView("${h.nickname}${h.contents}")
                        }
                    }
                } else {
                    when (h.type) {
                        ChatDetailType.TEXT.name -> {
                            addOthersMsgView(
                                h.contents,
                                h.userId,
                                h.nickname!!,
                                LocalDateTime.parse(h.regDate)
                            )
                        }
                        ChatDetailType.IMAGE.name -> {
                            imageReceiver(h)
                        }
                        ChatDetailType.WELCOME.name, ChatDetailType.EXIT.name -> {
                            addNoticeView("${h.nickname}${h.contents}")
                        }
                    }
                }
            }
        }

        chatViewModel.message.observe(viewLifecycleOwner) { msgInfo ->
            // 다른 유저의 이미지 추가해야 함
            when (msgInfo.type) {
                ChatDetailType.CNT.name -> {
                    // 총인원, 현재인원, 유저리스트
                    Timber.e("type cnt message received $msgInfo")
                    val currentCount = JSONObject(msgInfo.contents).get("currentCount")
                    sendMessage("", ChatDetailType.WELCOME)
                    binding.numOfPeople.text =
                        "인원 ${currentCount}명 / ${chatViewModel.chatInfo.value?.maxCount}명"
                }
                ChatDetailType.WELCOME.name -> {
                    addNoticeView("${msgInfo.nickname}${msgInfo.contents}")
                }
                ChatDetailType.EXIT.name -> {
                    addNoticeView("${msgInfo.nickname}${msgInfo.contents}")
                    if (msgInfo.contents.contains(App.prefs.nickname!!)) {
                        binding.checkDepositButton.visibility = View.VISIBLE
                    }
                }
                ChatDetailType.TEXT.name -> {
                    // 내가 보낸 메세지는 추가하지 않음
                    if (msgInfo.sender != App.prefs.userId) {
                        addOthersMsgView(msgInfo.contents, msgInfo.sender, msgInfo.nickname)
                    } else {
                        addMyMsgView(msgInfo.contents)
                    }
                }
                ChatDetailType.IMAGE.name -> {
                    imageReceiver(msgInfo)
                }
                else -> {

                }
            }
        }
    }

    private fun showGallery() {
        // granted
        if (isGalleryVisible) {
            binding.galleryRecyclerview.visibility = View.GONE
            isGalleryVisible = false
        } else {
            //
            val list = chatViewModel.getAllImagePaths(requireActivity())
            Timber.e("list $list")
            if (list.isEmpty()) {
                showShortToast(requireContext(), "사진이 없습니다")
                return
            }

            binding.galleryRecyclerview.post {
                galleryAdapter.setData(list)
                binding.galleryRecyclerview.visibility = View.VISIBLE
            }
            isGalleryVisible = true
        }
    }

    private fun imageReceiver(msgInfo: Message) {
        try {
            val isStartOfLoading = msgInfo.contents.toIntOrNull()
            if (isStartOfLoading is Int) {
                initImageLoadVariables()
                totalImageSize = isStartOfLoading
            } else {
                if (totalImageSize < 0) {
                    initImageLoadVariables()
                    return
                }
                Timber.e("loading image ${encodedImageString.length}/$totalImageSize ")
//                val payloads = msgInfo.contents.split(",")
//                if(payloads.size>1){
//                    Timber.e("!!!!!!!!!!!!! ${payloads.size} !! ${payloads.first().length} ${payloads.first()}")
//                    totalImageSize -= payloads.first().length
//                }
                encodedImageString += msgInfo.contents/*.split(",").last()*/
                if (encodedImageString.length >= totalImageSize) {
                    Timber.e("load image finish ${encodedImageString.length}/$totalImageSize \n$encodedImageString ")

                    if (msgInfo.sender != App.prefs.userId) {
                        addOthersImageView(encodedImageString, msgInfo.sender, msgInfo.nickname)
                    } else {
                        addMyImageView(encodedImageString)
                    }
                    addImageToDrawerGallery(encodedImageString)
                    initImageLoadVariables()
                }
            }
        } catch (e: Exception) {
            showShortToast(requireContext(), "이미지 로드 에러")
            Timber.e("이미지 로드 에러 / $e")
            initImageLoadVariables()
        }
    }

    private fun imageReceiver(chatHistory: ChatHistory) {
        try {
            // 이미지를 전송할 때 처음엔 파일 사이즈를 보내기 때문에 인트형으로 케스팅된다면 이미지 전송의 처음임.
            val isStartOfLoading = chatHistory.contents.toIntOrNull()
            if (isStartOfLoading is Int) {
                initImageLoadVariables()
                totalImageSize = isStartOfLoading
            } else {
                if (totalImageSize < 0) {
                    initImageLoadVariables()
                    return
                }
                Timber.e("loading  image ${encodedImageString.length}/$totalImageSize ")
                encodedImageString += chatHistory.contents
                if (encodedImageString.length >= totalImageSize) {
                    Timber.e("load image finish ${encodedImageString.length}/$totalImageSize ")

                    if (chatHistory.userId != App.prefs.userId) {
                        addOthersImageView(
                            encodedImageString,
                            chatHistory.userId,
                            chatHistory.nickname!!
                        )
                    } else {
                        addMyImageView(encodedImageString)
                    }
                    addImageToDrawerGallery(encodedImageString)
                    initImageLoadVariables()
                }
            }
        } catch (e: Exception) {
            showShortToast(requireContext(), "이미지 로드 에러")
            Timber.e("이미지 로드 에러 / $e")
            initImageLoadVariables()
        }
    }

    private fun addImageToDrawerGallery(encodedString: String) {
        val byteArray = decodeString(encodedString.split(",").last())
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        val imageView = ImageView(requireContext())
        val lp = LinearLayout.LayoutParams(40.dpToPx(requireContext()), 40.dpToPx(requireContext()))
        lp.marginEnd = 5.dpToPx(requireContext())
        imageView.layoutParams = lp

        Glide.with(requireContext())
            .load(bitmap)
            .centerCrop()
            .into(imageView)
        if(binding.drawer.galleryContainer.childCount>=DRAWER_GALLERY_ITEM_MAX){
            binding.drawer.galleryContainer.removeViewAt(0)
        }
        binding.drawer.galleryContainer.addView(imageView)
        binding.drawer.galleryNothingText.visibility = View.GONE
    }

    private fun initImageLoadVariables() {
        totalImageSize = -1
        encodedImageString = ""
    }

    private fun initDrawerView(users: List<User>) {
        val inflater = LayoutInflater.from(requireContext())
        binding.drawer.participantContainer.removeAllViews()
        users.forEach { user ->
            val layoutBinding = ItemParticipantBinding.inflate(inflater)
            val ownerId = chatViewModel.chatInfo.value?.userId

            // user nickname
            layoutBinding.nicknameTextView.text = user.nickname

            // show me icon
            if (App.prefs.userId != user.userId) {
                layoutBinding.meTextView.visibility = View.GONE
            }
            if (ownerId == user.userId) {
                // owner user profile
                layoutBinding.profileImageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_king
                    )
                )
            } else {
                // normal user profile
                layoutBinding.profileImageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_user
                    )
                )
                // show ban button
                if (ownerId == App.prefs.userId) {
                    layoutBinding.banButton.visibility = View.VISIBLE
                    layoutBinding.banButton.setOnClickListener {
                        val dialog = CommonDialogFragment(
                            contentText = "강퇴하시겠습니까?",
                            colorButtonText = "강퇴",
                            colorButtonListener = {
                                chatViewModel.banUser(user.userId)
                            },
                            normalButtonText = "취소"
                        )
                        dialog.show(childFragmentManager, "Dialog")
                    }
                }
            }


            val lp = LinearLayout.LayoutParams(MATCH_PARENT, 40.dpToPx(requireContext()))
            lp.topMargin = 4.dpToPx(requireContext())
            layoutBinding.root.layoutParams = lp
            binding.drawer.participantContainer.addView(layoutBinding.root)
        }
    }

    private fun addMyMsgView(str: String, time: LocalDateTime? = null) {
        val inflater = LayoutInflater.from(requireContext())
        val layoutBinding = ItemMyMessageBoxBinding.inflate(inflater)

        layoutBinding.myMsgView.text = str
        layoutBinding.myTimeView.text =
            getMessageTimeString(time ?: LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        binding.msgContainer.addView(layoutBinding.root)
        binding.msgScrollview.apply { post { binding.msgScrollview.fullScroll(View.FOCUS_DOWN) } }
    }

    private fun addMyImageView(encodedString: String, time: LocalDateTime? = null) {
        val byteArray = decodeString(encodedString.split(",").last())
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        if(bitmap == null){
            Timber.e("Image null")
            return
        }

        val inflater = LayoutInflater.from(requireContext())
        val layoutBinding = ItemMyImageBoxBinding.inflate(inflater)
        layoutBinding.apply {
            // scale image size
            val screenSize = Util.getScreenSize(requireContext())
            val maxWidth = ((screenSize.x - 58.dpToPx(requireContext())) * 0.5).toInt()
            val imageWith = if (bitmap.width > maxWidth) maxWidth else bitmap.width
            val imgLp = myImageView.layoutParams
            imgLp.width = imageWith
            myImageView.layoutParams = imgLp
//            Timber.e("${((screenSize.x - 58.dpToPx(requireContext())) * 0.5).toInt()} / ${bitmap.width}")

            myTimeView.text =
                getMessageTimeString(time ?: LocalDateTime.now(ZoneId.of("Asia/Seoul")))
            Glide.with(requireContext())
                .load(bitmap)
                .into(myImageView)
        }

        binding.msgContainer.addView(layoutBinding.root)
        binding.msgScrollview.apply {
            postDelayed(
                { binding.msgScrollview.fullScroll(View.FOCUS_DOWN) },
                100
            )
        }
    }

    private fun addOthersImageView(
        encodedString: String,
        id: String,
        nickname: String,
        time: LocalDateTime? = null
    ) {
        val byteArray = decodeString(encodedString.split(",").last())
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        // view, image
        val inflater = LayoutInflater.from(requireContext())
        val layoutBinding = ItemOthersImageBoxBinding.inflate(inflater)
        layoutBinding.apply {
            val masterId = chatViewModel.chatInfo.value?.userId
            profileImage.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (id == masterId) R.drawable.ic_king else R.drawable.ic_user
                )
            )

            // scale image size
            val screenSize = Util.getScreenSize(requireContext())
            val maxWidth = ((screenSize.x - 58.dpToPx(requireContext())) * 0.5).toInt()
            val imageWith = if (bitmap.width > maxWidth) maxWidth else bitmap.width
            val imgLp = othersImageView.layoutParams
            imgLp.width = imageWith
            othersImageView.layoutParams = imgLp

            othersTimeView.text =
                getMessageTimeString(time ?: LocalDateTime.now(ZoneId.of("Asia/Seoul")))
            Glide.with(requireContext())
                .load(bitmap)
                .into(othersImageView)
            othersNameView.text = nickname
        }


        binding.msgContainer.addView(layoutBinding.root)
        binding.msgScrollview.apply {
            postDelayed(
                { binding.msgScrollview.fullScroll(View.FOCUS_DOWN) },
                200
            )
        }
    }


    private fun addOthersMsgView(
        str: String,
        id: String,
        nickname: String,
        time: LocalDateTime? = null
    ) {
        // view, image
        val inflater = LayoutInflater.from(requireContext())
        val layoutBinding = ItemOthersMessageBoxBinding.inflate(inflater)
        layoutBinding.apply {
            val masterId = chatViewModel.chatInfo.value?.userId
            profileImage.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (id == masterId) R.drawable.ic_king else R.drawable.ic_user
                )
            )
            othersTimeView.text =
                getMessageTimeString(time ?: LocalDateTime.now(ZoneId.of("Asia/Seoul")))
            othersMsgView.text = str
            othersNameView.text = nickname
        }
        binding.msgContainer.addView(layoutBinding.root)
        binding.msgScrollview.apply { post { binding.msgScrollview.fullScroll(View.FOCUS_DOWN) } }
    }


    private fun addNoticeView(str: String) {
        // view, image
        val inflater = LayoutInflater.from(requireContext())
        val layoutBinding = ItemChatNoticeBinding.inflate(inflater)

        layoutBinding.noticeTextview.text = str

        binding.msgContainer.addView(layoutBinding.root)
        binding.msgScrollview.apply { post { binding.msgScrollview.fullScroll(View.FOCUS_DOWN) } }
    }

    private fun sendMessage(content: String, type: ChatDetailType) {
        Timber.e("content: $content")
        if (content.isEmpty()) return
        when (type) {
            ChatDetailType.TEXT -> {
                chatViewModel.sendMsg(content, type)
                binding.messageEdittext.text.clear()
            }
            ChatDetailType.IMAGE -> {
                chatViewModel.sendMsg(content, type)
            }
            else -> {}
        }
    }

    override fun onStop() {
        chatViewModel.stopStomp()
        super.onStop()
    }


    override fun onBackPressed() {
        val drawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            requireActivity().finish()
        }
    }
}