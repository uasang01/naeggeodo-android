package com.naeggeodo.presentation.view.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemChatListBinding
import com.naeggeodo.presentation.utils.Util
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

class ChatListAdapter(private val context: Context, private var datas: ArrayList<Chat>) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

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

            Glide.with(context)
                .asBitmap()
                .load(datas[position].imgPath)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        image.setImageBitmap(resource)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                        Timber.e("Error occurred loading image by Glide")
                    }
                })

            enterContainer.setOnClickListener {
                Util.showShortSnackbar(holder.binding.root, "order together clicked")
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
        notifyItemRangeChanged(0, chatList.size)
    }

    fun clearData() {
        val size = datas.size
        datas.clear()
        notifyItemRangeChanged(0, size)
    }
}