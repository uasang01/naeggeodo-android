package com.naeggeodo.presentation.view.chat

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import com.naeggeodo.presentation.utils.Util.loadImageAndSetView
import com.naeggeodo.presentation.viewmodel.ChatViewModel
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File


class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat) {
    private val chatViewModel: ChatViewModel by activityViewModels()
    private lateinit var getPictureResult: ActivityResultLauncher<Intent>
    private var bitmap: Bitmap? = null
    private val galleryAdapter by lazy { GalleryAdapter(requireContext(), arrayListOf()) }

    private var isGalleryVisible = false
    override fun init() {
//        Timber.e("${chatViewModel.chatId} test")

        getPictureResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Timber.e("activity result ok\n${result.data?.dataString}")
                    result.data?.let {
                        val uri = result.data!!.data
                        Timber.e(uri.toString())
                        bitmap = ImageDecoder
                            .decodeBitmap(
                                ImageDecoder.createSource(
                                    requireContext().contentResolver,
                                    uri!!
                                )
                            )
                            .copy(Bitmap.Config.ARGB_8888, true)


                        val stream = ByteArrayOutputStream()
                        bitmap!!.compress(Bitmap.CompressFormat.PNG, 50, stream)
                        val imageByteArray = stream.toByteArray()
//                        Timber.e("byte array as string : ${String(imageByteArray)}")
                        sendMessage(imageByteArray.toString(charset("UTF-8")), ChatDetailType.IMAGE)
//                        Glide.with(requireContext())
//                            .load(bitmap)
//                            .error(R.drawable.ic_error)
//                            .centerCrop()
//                            .into(binding.chatImage)
                    }
                } else {
                    Timber.e("activity result not ok")
                }
            }
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



//        initTestView()
    }

    override fun initListener() {
        binding.hambergerButton.setOnClickListener {
            Timber.e("ContentResolver.SCHEME_ANDROID_RESOURCE: ${ContentResolver.SCHEME_ANDROID_RESOURCE} / getPackageName(): ${requireContext().packageName}")
        }

        binding.messageEdittext.setOnKeyListener { view, keyCode, event ->
//            when (keyCode) {
//                KeyEvent.KEYCODE_ENTER -> {
////                    if(event.action == KeyEvent.ACTION_DOWN){
////                        Timber.e("wehfiowfhiowfo")
////                        sendMessage()
////                    }
//                    return@setOnKeyListener true
//                }
//                else -> return@setOnKeyListener false
//            }
            false
        }
        binding.sendMessageButton.setOnClickListener {
            if(isGalleryVisible){
                //send image
                val uriString = galleryAdapter.getSelectedPicture()
                if(uriString != null){
                    val file = File(uriString)
//                    sendMessage()

                    galleryAdapter.clearSelected()
                    binding.galleryRecyclerview.visibility = View.GONE

                    return@setOnClickListener
                }
            }
            sendMessage(binding.messageEdittext.text.toString(), ChatDetailType.TEXT)
        }
        binding.showGalleryButton.setOnClickListener {
//            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
//            photoPickerIntent.type = "image/*"

//            getPictureResult.launch(pickPhoto)
//

            if (isGalleryVisible) {
                isGalleryVisible = false
                binding.galleryRecyclerview.visibility = View.GONE
                galleryAdapter.clearData()
                galleryAdapter.clearSelected()
            } else {
                isGalleryVisible = true
                val list = chatViewModel.getAllImagePaths(requireActivity())
                binding.galleryRecyclerview.visibility = View.VISIBLE
                galleryAdapter.setData(list)
                Timber.e("list $list")
            }

        }
        galleryAdapter.setItemClickEvent { pos ->
            val uri = galleryAdapter.getSelectedPicture()
            Timber.e("uri $uri")
        }
    }

    override fun observeViewModels() {
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
            it.users.forEach { user ->
//                Timber.e("users received ${user.toString()}")
            }
        }

        chatViewModel.history.observe(viewLifecycleOwner) { historyList ->
            Timber.e("history received size: ${historyList.size}")
            historyList.forEach { h ->
                Timber.e("history received ${h}")
            }

            chatViewModel.runStomp()
        }

        chatViewModel.message.observe(viewLifecycleOwner) { msgInfo ->
            // 다른 유저의 이미지 추가해야 함

            when (msgInfo.type) {
                ChatDetailType.CNT.name -> {
                    // CNT 타입의 메세지를 받으면 1명 추가하기.
                    Timber.e("fweiofhewoifhweiofhweiofhewiofhewiofhiweofioewfhiowefhioewhfiowehfi")
                }
                ChatDetailType.TEXT.name -> {
                    // 내가 보낸 메세지는 추가하지 않음
                    if (msgInfo.sender != App.prefs.userId) {
                        addOthersMsgView(msgInfo.contents)
                    } else {
                        addMyMsgView(msgInfo.contents)
                    }
                    Timber.i("message Recieve ${msgInfo}")
                }
                ChatDetailType.IMAGE.name -> {
                    // 내가 보낸 메세지는 추가하지 않음
//                    if (msg.sender != App.prefs.userId) {
//                        addOthersMsgView(msg.contents)
//                    }

                    addMyImageView(msgInfo.contents)
                    Timber.i("image Recieve ${msgInfo}")
                }
                else -> {

                }
            }

        }
    }


    private fun addMyMsgView(str: String) {

        val inflater = LayoutInflater.from(requireContext())
        val msgLayout = inflater.inflate(R.layout.item_my_message_box, null)
        val msgView = msgLayout.findViewById<TextView>(R.id.my_msg_view)
        msgView.text = str
        binding.msgContainer.addView(msgLayout)
        binding.msgScrollview.apply { post { fullScroll(View.FOCUS_DOWN) } }
    }

    private fun addMyImageView(str: String) {
        val inflater = LayoutInflater.from(requireContext())
        val imageLayout = inflater.inflate(R.layout.item_my_image_box, null)
        val imageView = imageLayout.findViewById<ImageView>(R.id.my_image_view)
        val imageByteArray = str.toByteArray(charset("UTF-8"))
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        Glide.with(requireContext())
            .load(bitmap)
            .into(imageView)
        binding.msgContainer.addView(imageLayout)
        binding.msgScrollview.apply { post { fullScroll(View.FOCUS_DOWN) } }
    }

    private fun addOthersMsgView(str: String, imagePath: String? = null) {
        val inflater = LayoutInflater.from(requireContext())
        val msgLayout = inflater.inflate(R.layout.item_others_message_box, null)
        val profileView = msgLayout.findViewById<ImageView>(R.id.profile_image)
        val msgView = msgLayout.findViewById<TextView>(R.id.others_msg_view)

        profileView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_hamberger
            )
        )
        msgView.text = str
        binding.msgContainer.addView(msgLayout)
        binding.msgScrollview.apply { post { fullScroll(View.FOCUS_DOWN) } }
    }

    override fun onDestroyView() {


        super.onDestroyView()
    }

    override fun onDestroy() {
        chatViewModel.stopStomp()
        super.onDestroy()
    }

    private fun sendMessage(content: String, type: ChatDetailType) {
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

    private fun initTestView() {
        val tempStr = "ewnklfnwelkflwefnlkfnwkeflnwlfk\nfwenlwkf"
        val tempStr2 = "ewnklfnwfnwkefwlfk\nfwenlwkf"
        val tempStr3 = "ewnklfnwelkflwefnlkfnwkeflnwlfk\n" +
                "fwenllwefnlkfnwkeflnwlfk\n" +
                "fwenllwefnlkfnwkeflnwlfk\n" +
                "fwenllwefnlkfnwkeflnwlfk\n" +
                "fwenllwefnlkfnwkekgnowengwkengwgpeflnwlfk\n" +
                "fwenlwkf"
        addMyMsgView(tempStr)
        addMyMsgView(tempStr2)
        addOthersMsgView(tempStr3)
        addMyMsgView(tempStr)
        addMyMsgView(tempStr)
        addOthersMsgView(tempStr2)
        addMyMsgView(tempStr)
        addMyMsgView(tempStr3)
        addOthersMsgView(tempStr)
        addMyMsgView(tempStr)
        addMyMsgView(tempStr2)
        addOthersMsgView(tempStr3)
    }
}