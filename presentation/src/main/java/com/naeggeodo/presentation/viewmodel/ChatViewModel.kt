package com.naeggeodo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.naeggeodo.domain.model.Categories
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.Users
import com.naeggeodo.domain.usecase.GetChatInfoUseCase
import com.naeggeodo.domain.usecase.GetUsersInChatUseCase
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatInfoUseCase: GetChatInfoUseCase,
    private val getUsersInChatUseCase: GetUsersInChatUseCase
) : BaseViewModel() {
    companion object {
        const val EVENT_CHAT_INFO_CHANGED = 311
        const val EVENT_USERS_CHANGED = 312
    }
    var chatId: Int? = null

    private val _chatInfo: MutableLiveData<Chat> = MutableLiveData()
    val chatInfo: LiveData<Chat> get() = _chatInfo
    private val _users: MutableLiveData<Users> = MutableLiveData()
    val users: LiveData<Users> get() = _users


    fun getChat() = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response = withContext(Dispatchers.IO) {
            getChatInfoUseCase.execute(this@ChatViewModel, chatId!!)
        }
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            _chatInfo.postValue(response!!)
            mutableScreenState.postValue(ScreenState.RENDER)
//            viewEvent(HomeViewModel.EVENT_CHAT_INFO_CHANGED)
        }
    }
    fun getUsers() = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response = withContext(Dispatchers.IO) {
            getUsersInChatUseCase.execute(this@ChatViewModel, chatId!!)
        }
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            _users.postValue(response!!)
            mutableScreenState.postValue(ScreenState.RENDER)
//            viewEvent(HomeViewModel.EVENT_USERS_CHANGED)
        }
    }








    fun runStomp(){
        //    ### 커넥트 END Point
        //    ⇒ https//api.naeggeodo.com/api/chat

        //    Subscribe url
        //    1.  ‘/topic/’+채팅방id   : 전체 메세지
        //    2. ‘/user/queue/’+세션Id : 개인 메시지 ex)강퇴 , alert

        //    SEND url
        //    ⇒ prefix : ‘/app/chat’
        //    1. 메세지 전송 : ‘/send’
        //    2. 입장메시지 : ‘/enter’
        //    3. 퇴장메시지 : ‘/exit’
        //    4. 강퇴 : ‘/ban’


        // http -> ws
        // https -> wss
        // url 끝에 /websocket 은 꼭 붙여줘야한다.
        val url = "ws://api.naeggeodo.com/api/chat/websocket" // 소켓에 연결하는 엔드포인트가 /socket일때 다음과 같음
        val stompClient =  Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        // 메세지를 받기위한 구독 설정
        stompClient.topic("/topic/$chatId").subscribe { topicMessage ->
            Timber.i("message Recieve", topicMessage.payload)
        }

        // 필요한 헤더 추가
        val headerList = arrayListOf<StompHeader>()
        headerList.add(StompHeader("inviteCode","test0912"))
        headerList.add(StompHeader("positionType", "1"))
        stompClient.connect(headerList)

        // stomp의 lifecycle 구독
        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Timber.i("OPEND", "!!")
                }
                LifecycleEvent.Type.CLOSED -> {
                    Timber.i("CLOSED", "!!")

                }
                LifecycleEvent.Type.ERROR -> {
                    Timber.i("ERROR", "!!")
                    Timber.e("CONNECT ERROR", lifecycleEvent.exception.toString())
                }
                else ->{
                    Timber.i("ELSE", lifecycleEvent.message)
                }
            }
        }

        // 메세지형식
        // 일반
//        data = {
//            'chatMain_id': '채팅방 id',
//            'sender' :'보내는사람id',
//            'contents': '채팅내용',
//            'type' : "TEXT",  // 메시지타입
//            'nickname' : '보내는사람 닉네임'
//        }

        // 강퇴
//        data = {
//            'chatMain_id': '채팅방 id',
//            'sender' :'보내는사람id',
//            'contents': '강퇴할 유저아이디',
//            'target_id':'강퇴할유저아이디',
//            'type' : "TEXT",  // 메시지타입
//            'nickname' : '보내는사람 닉네임'
//        }


        // 메세지 전송
        val data = JSONObject()
//        data.put("userKey", text.value)
        data.put("positionType", "1")
        data.put("content", "test")
        data.put("messageType", "CHAT")
        data.put("destRoomCode", "test0912")

        stompClient.send("/stream/chat/send", data.toString()).subscribe()
    }


    fun setScreenState(state: ScreenState){
        mutableScreenState.postValue(state)
    }
}