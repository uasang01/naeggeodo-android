package com.naeggeodo.presentation.view.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.ViewSizeResolver
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemChatListBinding
import com.naeggeodo.presentation.utils.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*


class ChatListAdapter(private val context: Context, private var datas: ArrayList<Chat>) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>(), ImageLoaderFactory {

    val drawableList = hashMapOf<Int, Drawable>()
    val imageLoader by lazy { newImageLoader() }

    inner class ViewHolder(val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_list, parent, false)
        return ViewHolder(ItemChatListBinding.bind(view))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {
            val d = datas[position].createDate
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREAN)
            val date = sdf.parse(d)!!

            val sdf2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREAN)
            sdf.timeZone = TimeZone.getTimeZone(ZoneId.of("Asia/Seoul"))
            val curDate = sdf2.parse(sdf.format(Date()))

            val timeDiff = curDate!!.time - date.time
            Timber.e("${datas[position].title}, $date, $curDate / ${timeDiff} ${timeDiff / 1000}")

            title.text = datas[position].title
            time.text = getTimeStr(timeDiff)
            count.text = "인원 ${datas[position].currentCount}명 / ${datas[position].maxCount}명"

            // 이미지를 한번 불러왔다면 또 url로부터 로드하지 않고 사용
            if (drawableList[position] == null) {
                val imgRequest = ImageRequest
                    .Builder(context)
                    .data(datas[position].imgPath)
                    .size(ViewSizeResolver(image))
                    .target { drawable ->
                        CoroutineScope(Dispatchers.Main).launch {
                            image.setImageDrawable(drawable)
                        }
                    }
                    .listener(
                        onSuccess = { _, result ->
                            Timber.e("imageLoad Success. pos:$position")
                            drawableList[position] = result.drawable
                        },
                        onError = { a, result ->
                            Timber.e("imageLoad Fail. pos:$position")
                        }
                    )
                    .build()
                CoroutineScope(Dispatchers.IO).launch {
                    val a = imageLoader.execute(imgRequest)
                    Timber.e("position $position : ${a.request.listener}")
                }

                enterContainer.setOnClickListener {
                    Util.showShortSnackbar(holder.binding.root, "order together clicked")
                }
            } else {
                Timber.e("position $position : ${drawableList[position]}")
                image.setImageDrawable(drawableList[position])
            }
        }
    }

    private fun getTimeStr(timeDiff: Long): String {
        val seconds = timeDiff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30

        return if (seconds < 0) {
            "???"
        } else if (seconds < 60) {
            "방금 전"
        } else if (minutes < 60) {
            "${minutes}분 전"
        } else if (hours < 24) {
            "${hours}시간 전"
        } else if (days < 30) {
            "${days}일 전"
        } else if (months < 30) {
            "${months}달 전"
        } else {
            "오래 전"
        }
    }

    override fun getItemCount() = datas.size
    fun setData(chatList: ArrayList<Chat>) {
        clearData()
        datas.addAll(chatList)
        notifyItemRangeInserted(0, chatList.size)
    }

    fun clearData() {
        val size = datas.size
        datas.clear()
        drawableList.clear()
        notifyItemRangeRemoved(0, size)
    }


    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }
}