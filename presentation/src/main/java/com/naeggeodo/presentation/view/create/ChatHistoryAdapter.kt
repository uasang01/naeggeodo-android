package com.naeggeodo.presentation.view.create

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.utils.Bookmarks
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemChatHistoryBinding
import com.naeggeodo.presentation.utils.Util
import com.naeggeodo.presentation.utils.Util.getSvgRequestBuilder
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*


class ChatHistoryAdapter(private val context: Context, private var datas: ArrayList<Chat>) :
    RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder>() {

    private val drawableList = hashMapOf<Int, Drawable>()
    private val svgRequestBuilder = getSvgRequestBuilder(context)
    private var favoriteListener: (pos: Int) -> Unit = {}
    private var deleteListener: (pos: Int) -> Unit = {}
    private var selectedItemPos: Int? = null

    inner class ViewHolder(val binding: ItemChatHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_history, parent, false)
        return ViewHolder(ItemChatHistoryBinding.bind(view))
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

            // 배경(아이템 선택 여부)
            if (selectedItemPos != position) {
                chatCreationHistoryContainer.background =
                    ContextCompat.getDrawable(context, R.color.white)
            } else {
                chatCreationHistoryContainer.background =
                    ContextCompat.getDrawable(context, R.color.grey_F5F5F5)
            }

            // 북마크
            val bookmarkDrawable = if (datas[position].bookmarks == Bookmarks.Y.name) {
                R.drawable.ic_star_filled
            } else {
                R.drawable.ic_star_border
            }
            bookmarkButton.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    bookmarkDrawable
                )
            )

            // profile
            Util.loadImageAndSetView(context, datas[position].imgPath, image)


            // listeners
            chatCreationHistoryContainer.setOnClickListener {
                val prevPos = selectedItemPos
                selectedItemPos = if (selectedItemPos == position) {
                    null
                } else {
                    position
                }

                prevPos?.let { notifyItemChanged(it) }
                notifyItemChanged(position)
            }


            bookmarkButton.setOnClickListener {
                favoriteListener(position)
            }

            deleteButton.setOnClickListener {
                deleteListener(position)
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
    fun getSelectedItemPos() = selectedItemPos
    fun setData(chatList: ArrayList<Chat>) {
        clearData()
        datas.addAll(chatList)
        notifyItemRangeInserted(0, chatList.size)
    }

    fun getData(pos: Int): Chat {
        return datas[pos]
    }

    fun updateBookmark(pos: Int) {
        if (datas[pos].bookmarks == Bookmarks.Y.name) {
            datas[pos].bookmarks = Bookmarks.N.name
        } else {
            datas[pos].bookmarks = Bookmarks.Y.name
        }
        notifyItemChanged(pos)
    }

    fun clearData() {
        val size = datas.size
        datas.clear()
        drawableList.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun setFavoriteListener(listener: (p: Int) -> Unit) {
        favoriteListener = listener
    }

    fun setDeleteListener(listener: (p: Int) -> Unit) {
        deleteListener = listener
    }

}