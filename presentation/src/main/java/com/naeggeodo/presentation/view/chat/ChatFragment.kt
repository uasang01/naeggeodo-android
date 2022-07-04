package com.naeggeodo.presentation.view.chat

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.naeggeodo.domain.utils.ChatDetailType
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentChatBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.Util
import com.naeggeodo.presentation.utils.Util.decodeString
import com.naeggeodo.presentation.utils.Util.encodeImage
import com.naeggeodo.presentation.utils.Util.getMessageTimeString
import com.naeggeodo.presentation.utils.Util.loadImageAndSetView
import com.naeggeodo.presentation.utils.Util.showShortToast
import com.naeggeodo.presentation.utils.dpToPx
import com.naeggeodo.presentation.viewmodel.ChatViewModel
import org.json.JSONObject
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat),
    ChatActivity.OnBackPressedListener {
    private val chatViewModel: ChatViewModel by activityViewModels()
    private val galleryAdapter by lazy { GalleryAdapter(requireContext(), arrayListOf()) }


    private var imageLoadStart = false
    private var totalImageSize = -1
    private var encodedImageString = ""

    private var isGalleryVisible = false
    override fun init() {
    }

    override fun onStart() {
        super.onStart()

        chatViewModel.getChat()
        chatViewModel.getUsers()
        chatViewModel.getChatHistory()
    }

    override fun initView() {
        super.initView()

        binding.galleryRecyclerview.adapter = galleryAdapter
        val lm = LinearLayoutManager(requireContext())
        lm.orientation = LinearLayoutManager.HORIZONTAL
        binding.galleryRecyclerview.layoutManager = lm


        // make recycler view not flickering
        binding.galleryRecyclerview.itemAnimator?.changeDuration = 0L
    }

    override fun initListener() {
        binding.hambergerButton.setOnClickListener {
            val drawerLayout = binding.drawerLayout
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        }

        binding.drawer.exitChatButton.setOnClickListener {
            val exitDialog = AlertDialog.Builder(requireContext())
            exitDialog.setTitle("나가기")
            exitDialog.setMessage("정말 나가시겠습니까?")
            exitDialog.setPositiveButton("나가기") { _, _ ->
                chatViewModel.sendMsg("님이 퇴장하셨습니다.", ChatDetailType.EXIT)
                requireActivity().finish()
            }
            exitDialog.setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            exitDialog.show()
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
                    binding.galleryRecyclerview.visibility = View.GONE

                    return@setOnClickListener
                }
            }
            // send text
            sendMessage(binding.messageEdittext.text.toString(), ChatDetailType.TEXT)
        }
        binding.showGalleryButton.setOnClickListener {
//            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
//            photoPickerIntent.type = "image/*"
//            getPictureResult.launch(pickPhoto)

            if (isGalleryVisible) {
                binding.galleryRecyclerview.visibility = View.GONE
                isGalleryVisible = false
            } else {
                val list = chatViewModel.getAllImagePaths(requireActivity())
                Timber.e("list $list")
                if (list.isEmpty()) {
                    showShortToast(requireContext(), "사진이 없습니다")
                    return@setOnClickListener
                }

                binding.galleryRecyclerview.post {
                    galleryAdapter.setData(list)
                    binding.galleryRecyclerview.visibility = View.VISIBLE
                }
                isGalleryVisible = true
            }
        }
        galleryAdapter.setItemClickEvent { pos ->
            // 없어도 됨
            val uri = galleryAdapter.getSelectedPicture()
            Timber.e("uri $uri")
        }
    }


    override fun observeViewModels() {
        chatViewModel.viewEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ChatViewModel.ERROR_OCCURRED -> {
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
//            Timber.e("chat received")
//            Timber.e("chat received ${chat}")
            binding.chatTitleText.text = chat.title
            loadImageAndSetView(requireContext(), chat.imgPath, binding.chatImage)
            binding.numOfPeople.text = "인원 ${chat.currentCount}명 / ${chat.maxCount}명"
        }

        chatViewModel.users.observe(viewLifecycleOwner) {
            val s = it.users
//            Timber.e("users received ${it.users.size}")
//            Timber.e("users received ${user.toString()}")
            it.users.forEach { user ->
            }
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

                        }
                        ChatDetailType.WELCOME.name, ChatDetailType.EXIT.name -> {
                            addNoticeView(h.contents)
                        }
//                        ChatDetailType.BAN -> {
//
//                        }
//                        ChatDetailType.TEXT.name -> {
//
//                        }
                    }
                } else {
                    addOthersMsgView(h.contents, LocalDateTime.parse(h.regDate))
                }
            }

            chatViewModel.runStomp()
        }

        chatViewModel.message.observe(viewLifecycleOwner) { msgInfo ->
            // 다른 유저의 이미지 추가해야 함
            when (msgInfo.type) {
                ChatDetailType.CNT.name -> {
                    // CNT 타입의 메세지를 받으면 1명 추가하기.
                    Timber.e("type cnt message received $msgInfo")
                    val currentCount = JSONObject(msgInfo.contents).get("currentCount")
                    binding.numOfPeople.text =
                        "인원 ${currentCount}명 / ${chatViewModel.chatInfo.value?.maxCount}명"
//                    val users = JSONObject(msgInfo.contents).get("users")
                }
                ChatDetailType.WELCOME.name -> {
                    addNoticeView("${msgInfo.nickname} 님이 입장하셨습니다")
                }
                ChatDetailType.EXIT.name -> {
                    addNoticeView("${msgInfo.nickname} 님이 퇴장하셨습니다")
                }
                ChatDetailType.TEXT.name -> {
                    // 내가 보낸 메세지는 추가하지 않음
                    if (msgInfo.sender != App.prefs.userId) {
                        addOthersMsgView(msgInfo.contents)
                    } else {
                        addMyMsgView(msgInfo.contents)
                    }
//                    Timber.i("message Recieve ${msgInfo}")
                }
                ChatDetailType.IMAGE.name -> {
                    if (!imageLoadStart) {
                        Timber.e("load image start")
                        imageLoadStart = true
                        totalImageSize = msgInfo.contents.toInt()
                    } else {
                        try {
                            if (totalImageSize < 0) {
                                imageLoadStart = false
                                totalImageSize = -1
                                encodedImageString = ""
                                return@observe
                            }
                            Timber.e("loading  image ${encodedImageString.length}/$totalImageSize ")
                            encodedImageString += msgInfo.contents
                            if (encodedImageString.length >= totalImageSize) {
                                Timber.e("load image finish ${encodedImageString.length}/$totalImageSize ")
                                imageLoadStart = false
                                totalImageSize = -1

                                if (msgInfo.sender != App.prefs.userId) {
                                    addOthersImageView(encodedImageString)
                                } else {
                                    addMyImageView(encodedImageString)
                                }

                                encodedImageString = ""
                            }
                        } catch (e: Exception) {
                            showShortToast(requireContext(), "에러")
                        }
                    }
                }
                else -> {

                }
            }
        }
    }


    private fun addMyMsgView(str: String, time: LocalDateTime? = null) {

        val inflater = LayoutInflater.from(requireContext())
        val msgLayout = inflater.inflate(R.layout.item_my_message_box, null)
        val msgView = msgLayout.findViewById<TextView>(R.id.my_msg_view)
        val timeView = msgLayout.findViewById<TextView>(R.id.my_time_view)

        msgView.text = str
        val curTime = TimeZone.getTimeZone(ZoneId.of("Asia/Seoul"))

        timeView.text = getMessageTimeString(time ?: LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        binding.msgContainer.addView(msgLayout)
        binding.msgScrollview.apply { post { binding.msgScrollview.fullScroll(View.FOCUS_DOWN) } }
    }

    private fun addMyImageView(encodedString: String, time: LocalDateTime? = null) {
        val inflater = LayoutInflater.from(requireContext())
        val imageLayout = inflater.inflate(R.layout.item_my_image_box, null)
        val imageView = imageLayout.findViewById<ImageView>(R.id.my_image_view)
        val byteArray = decodeString(encodedString)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val timeView = imageLayout.findViewById<TextView>(R.id.my_time_view)

        // scale image size
        val screenSize = Util.getScreenSize(requireContext())
        val maxWidth = ((screenSize.x - 58.dpToPx(requireContext())) * 0.5).toInt()
        val imageWith = if (bitmap.width > maxWidth) maxWidth else bitmap.width
        val imgLp = imageView.layoutParams
        imgLp.width = imageWith
        imageView.layoutParams = imgLp
        Timber.e("${((screenSize.x - 58.dpToPx(requireContext())) * 0.5).toInt()} / ${bitmap.width}")

        timeView.text = getMessageTimeString(time ?: LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        Glide.with(requireContext())
            .load(bitmap)
            .into(imageView)
        binding.msgContainer.addView(imageLayout)
        binding.msgScrollview.apply {
            postDelayed(
                { binding.msgScrollview.fullScroll(View.FOCUS_DOWN) },
                100
            )
        }
    }

    private fun addOthersImageView(encodedString: String, time: LocalDateTime? = null) {
        // view, image
        val inflater = LayoutInflater.from(requireContext())
        val imageLayout = inflater.inflate(R.layout.item_others_image_box, null)
        val imageView = imageLayout.findViewById<ImageView>(R.id.others_image_view)
        val byteArray = decodeString(encodedString)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val timeView = imageLayout.findViewById<TextView>(R.id.others_time_view)

        // scale image size
        val screenSize = Util.getScreenSize(requireContext())
        val maxWidth = ((screenSize.x - 58.dpToPx(requireContext())) * 0.5).toInt()
        val imageWith = if (bitmap.width > maxWidth) maxWidth else bitmap.width
        val imgLp = imageView.layoutParams
        imgLp.width = imageWith
        imageView.layoutParams = imgLp

        timeView.text = getMessageTimeString(time ?: LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        Glide.with(requireContext())
            .load(bitmap)
            .into(imageView)
        binding.msgContainer.addView(imageLayout)
        binding.msgScrollview.apply {
            postDelayed(
                { binding.msgScrollview.fullScroll(View.FOCUS_DOWN) },
                100
            )
        }
    }


    private fun addOthersMsgView(str: String, time: LocalDateTime? = null) {
        // view, image
        val inflater = LayoutInflater.from(requireContext())
        val msgLayout = inflater.inflate(R.layout.item_others_message_box, null)
        val profileView = msgLayout.findViewById<ImageView>(R.id.profile_image)
        val msgView = msgLayout.findViewById<TextView>(R.id.others_msg_view)
        val timeView = msgLayout.findViewById<TextView>(R.id.others_time_view)

        profileView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_hamberger
            )
        )

        timeView.text = getMessageTimeString(time ?: LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        msgView.text = str
        binding.msgContainer.addView(msgLayout)
        binding.msgScrollview.apply { post { binding.msgScrollview.fullScroll(View.FOCUS_DOWN) } }
    }


    private fun addNoticeView(str: String) {
        // view, image
        val inflater = LayoutInflater.from(requireContext())
        val msgLayout = inflater.inflate(R.layout.item_chat_notice, null)
        val noticeView = msgLayout.findViewById<TextView>(R.id.notice_textview)

        noticeView.text = str

        binding.msgContainer.addView(msgLayout)
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

    override fun onDestroyView() {


        super.onDestroyView()
    }

    override fun onDestroy() {

//        chatViewModel.sendMsg("님이 퇴장하셨습니다.", ChatDetailType.EXIT)
        chatViewModel.stopStomp()
        super.onDestroy()
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