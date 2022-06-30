package com.naeggeodo.presentation.view.chat

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseFragment
import com.naeggeodo.presentation.databinding.FragmentChatBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.Util
import com.naeggeodo.presentation.viewmodel.ChatViewModel
import timber.log.Timber

class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat) {
    private val chatViewModel: ChatViewModel by activityViewModels()
    override fun init() {
        Timber.e("${chatViewModel.chatId} test")
    }

    override fun onStart() {
        super.onStart()

        chatViewModel.getChat()
        chatViewModel.getUsers()
        chatViewModel.getChatHistory()

    }

    override fun initView() {
        super.initView()

//        initTestView()
    }

    override fun initListener() {
        binding.hambergerButton.setOnClickListener {
            val msg = "testMsg"
            chatViewModel.sendMsg(msg)
            addMyMsgView(msg)
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

        chatViewModel.message.observe(viewLifecycleOwner) { msg ->
            // 다른 유저의 이미지 추가해야 함

            // 내가 보낸 메세지는 추가하지 않음
            if (msg.sender != App.prefs.userId) {
                addOthersMsgView(msg.contents)
            }
            Timber.i("message Recieve ${msg}")
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
        chatViewModel.stopStomp()

        super.onDestroyView()
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