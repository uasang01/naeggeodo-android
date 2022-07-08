package com.naeggeodo.presentation.viewmodel

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.naeggeodo.domain.model.*
import com.naeggeodo.domain.usecase.*
import com.naeggeodo.domain.utils.ChatDetailType
import com.naeggeodo.presentation.base.BaseViewModel
import com.naeggeodo.presentation.data.Message
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
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
    private val getUsersInChatUseCase: GetUsersInChatUseCase,
    private val getPrevChatHistoryUseCase: GetPrevChatHistoryUseCase,
    private val getQuickChatUseCase: GetQuickChatUseCase,
    private val patchQuickChatUseCase: PatchQuickChatUseCase
) : BaseViewModel() {
    companion object {
        const val EVENT_CHAT_INFO_CHANGED = 311
        const val EVENT_USERS_CHANGED = 312
        const val EVENT_HISTORY_CHANGED = 313
        const val EVENT_MESSAGE_RECEIVED = 314
        const val EVENT_IMAGE_RECEIVED = 315
        const val EVENT_ENTER_CHAT = 316
        const val EVENT_EXIT_CHAT = 317
        const val EVENT_BAN_USER = 318
        const val EVENT_STOMP_CONNECTED = 319
        const val ERROR_OCCURRED = 31
//        const val EVENT = 316
    }


    var chatId: Int? = null

    // http -> ws
    // https -> wss
    private val url = "wss://api.naeggeodo.com/api/chat" // 소켓에 연결하는 엔드포인트가 /socket일때 다음과 같음

    // url 끝에 /websocket 은 꼭 붙여줘야한다.
    val stompClient by lazy {
        Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "$url/websocket"
        )
    }


    private val _chatInfo: MutableLiveData<Chat> = MutableLiveData()
    val chatInfo: LiveData<Chat> get() = _chatInfo
    private val _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>> get() = _users
    private val _history: MutableLiveData<List<ChatHistory>> = MutableLiveData()
    val history: LiveData<List<ChatHistory>> get() = _history
    private val _message: MutableLiveData<Message> = MutableLiveData()
    val message: LiveData<Message> get() = _message
    private val _quickChat: MutableLiveData<List<QuickChat>> = MutableLiveData()
    val quickChat: LiveData<List<QuickChat>> get() = _quickChat

//    val message: Message? = null

    fun getChatInfo() = viewModelScope.launch {
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
//            _users.postValue(response!!.users)
            mutableScreenState.postValue(ScreenState.RENDER)
//            viewEvent(HomeViewModel.EVENT_USERS_CHANGED)
        }
    }

    fun getChatHistory() = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response = withContext(Dispatchers.IO) {
            getPrevChatHistoryUseCase.execute(this@ChatViewModel, chatId!!, App.prefs.userId!!)
        }
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            _history.postValue(response.messages)
            mutableScreenState.postValue(ScreenState.RENDER)
//            viewEvent(HomeViewModel.EVENT_USERS_CHANGED)
        }
    }

    fun getQuickChats(userId: String) = viewModelScope.launch {
        mutableScreenState.postValue(ScreenState.LOADING)
        val response = withContext(Dispatchers.IO) {
            getQuickChatUseCase.execute(this@ChatViewModel, userId)
        }
        if (response == null) {
            mutableScreenState.postValue(ScreenState.ERROR)
        } else {
            _quickChat.postValue(response.quickChats)
            mutableScreenState.postValue(ScreenState.RENDER)
        }
    }

    fun updateQuickChats(userId: String, body: HashMap<String, List<String?>>) =
        viewModelScope.launch {
            mutableScreenState.postValue(ScreenState.LOADING)
            val response = withContext(Dispatchers.IO) {
                patchQuickChatUseCase.execute(this@ChatViewModel, userId, body)
            }
            if (response == null) {
                mutableScreenState.postValue(ScreenState.ERROR)
            } else {
                _quickChat.postValue(response.quickChats)
                mutableScreenState.postValue(ScreenState.RENDER)
            }
        }

    //    ### 커넥트 END Point
    //    ⇒ https//api.naeggeodo.com/api/chat

    //    Subscribe url
    //    1.  ‘/topic/’+채팅방id   : 전체 메세지
    //    2. ‘/user/queue/’+세션Id : 개인 메시지 ex)강퇴 , alert


    var lifecycleDisposable: Disposable? = null
    var msgReceiverDisposable: Disposable? = null
    var msgSenderDisposable: Disposable? = null

    fun runStomp() {
        if (stompClient.isConnected) {
            Timber.e("STOMP ALREADY CONNECTED")
            return
        }

        // init
        val userId = App.prefs.userId
        Timber.e("chatId : $chatId userId: $userId  ")
        // 필요한 헤더 추가
        val headerList = arrayListOf<StompHeader>()
        headerList.add(StompHeader("chatMain_id", "$chatId"))
        headerList.add(StompHeader("sender", "${userId}"))
        headerList.add(StompHeader("Authorization", "Bearer ${App.prefs.accessToken}"))
        stompClient.connect(headerList)


        // 메세지를 받기위한 구독 설정
        msgReceiverDisposable = stompClient.topic("/topic/$chatId")
            .subscribe({ topicMessage ->
//                Timber.i("message Recieve ${topicMessage.payload}")
                val jsonObject = JsonParser.parseString(topicMessage.payload)
                val msgInfo = Gson().fromJson(jsonObject, Message::class.java)
                _message.postValue(msgInfo)

                when (msgInfo.type) {
                    ChatDetailType.CNT.name -> {
                        // CNT 타입의 메세지를 받으면 1명 추가하기.
                        val currentCount = JSONObject(msgInfo.contents).get("currentCount")
                        val users = Gson().fromJson(
                            "{\"users\":${JSONObject(msgInfo.contents).get("users")}}",
                            Users::class.java
                        )
                        _users.postValue(users.users)
                    }
                }

            }, { throwable ->
                Timber.i("error occurred. cause: ${throwable.cause}, message: ${throwable.message}")
            })


        // stomp의 lifecycle 구독
        lifecycleDisposable = stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Timber.i("OPEND")
                    // 연결 시 입장 메세지 전송
                    sendMsg("", ChatDetailType.WELCOME)
                    viewEvent(EVENT_STOMP_CONNECTED)
                }
                LifecycleEvent.Type.CLOSED -> {
                    Timber.i("CLOSED")
//                    stopStomp()
//                    runStomp()

                    viewEvent(ERROR_OCCURRED)
                }
                LifecycleEvent.Type.ERROR -> {
                    Timber.i("ERROR")
                    Timber.e("CONNECT ERROR ${lifecycleEvent.exception}")
                    viewEvent(ERROR_OCCURRED)
                }
                else -> {
                    Timber.i("ELSE ${lifecycleEvent.message}")
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


        //    SEND url
        //    ⇒ prefix : ‘/app/chat’
        //    1. 메세지 전송 : ‘/send’
        //    2. 입장메시지 : ‘/enter’
        //    3. 퇴장메시지 : ‘/exit’
        //    4. 강퇴 : ‘/ban’
    }


    fun setScreenState(state: ScreenState) {
        mutableScreenState.postValue(state)
    }

    fun sendMsg(content: String, type: ChatDetailType) {
        val prefix = "/app/chat"
        val send = "/send"
        val image = "/image"
        val enter = "/enter"
        val ban = "/ban"
        val exit = "/exit"

        val data = JSONObject()
        var destination = prefix
        val nickname = App.prefs.nickname
        when (type) {
            ChatDetailType.TEXT -> {
                data.put("chatMain_id", chatId)
                data.put("sender", App.prefs.userId)
                data.put("contents", content)
                data.put("type", type.name)
                data.put("nickname", nickname)
                destination += send
            }
            ChatDetailType.IMAGE -> {
                data.put("chatMain_id", chatId)
                data.put("sender", App.prefs.userId)
                data.put("contents", content)
                data.put("type", type.name)
                data.put("nickname", nickname)
                destination += send
            }
            ChatDetailType.WELCOME -> {
                data.put("chatMain_id", chatId)
                data.put("sender", App.prefs.userId)
                data.put("contents", "${nickname}님이 입장하셨습니다")
                data.put("type", type.name)
                data.put("nickname", nickname)
                destination += enter
            }
            ChatDetailType.EXIT -> {
                data.put("chatMain_id", chatId)
                data.put("sender", App.prefs.userId)
                data.put("contents", "${nickname}님이 퇴장하셨습니다")
                data.put("type", type.name)
                data.put("nickname", nickname)
                destination += exit
            }
//            ChatDetailType.WELCOME -> {
//
//            }
            else -> {
                return
            }
        }
        msgSenderDisposable = stompClient
            .send(destination, data.toString())
            .subscribe(
                {
                    //onComplete
                },
                { error ->
                    Timber.e("ERROR OCCURRED ON SEND MESSAGE\nmessage: ${error.message} / cause: ${error.cause}")
                    viewEvent(ERROR_OCCURRED)
                })

        // 메세지 전송


    }

    fun banUser() {}
    fun exitChat() {}

    fun stopStomp() {
        msgSenderDisposable?.dispose()
        msgReceiverDisposable?.dispose()
        lifecycleDisposable?.dispose()
        stompClient.disconnect()
    }

    fun getAllImagePaths(activity: Activity): ArrayList<String> {
        val listOfAllImages = ArrayList<String>()

        var absolutePathOfImage: String?
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_MODIFIED,

            )
        val cursor = activity.contentResolver.query(
            uri,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
        )
        val columnIndexData: Int = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(columnIndexData)
            listOfAllImages.add(absolutePathOfImage)
        }
        cursor.close()

        return listOfAllImages
    }
}