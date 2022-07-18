package com.naeggeodo.presentation.view.mychat

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.model.ChatTitle
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemMyChatBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.Util.getSvgRequestBuilder
import com.naeggeodo.presentation.utils.Util.getTimeDiff
import com.naeggeodo.presentation.utils.Util.getTimeStr


class MyChatListAdapter(
    private val context: Context,
    private var datas: ArrayList<Chat>,
    private var listener: (chatId: Int) -> Unit = {},
    private var editTitleListener: (chatId: Int, title: String) -> Unit = { _, _ -> }
) :
    RecyclerView.Adapter<MyChatListAdapter.ViewHolder>() {

    val drawableList = hashMapOf<Int, Drawable>()
    val svgRequestBuilder = getSvgRequestBuilder(context)

    var clickedPos = 0

    inner class ViewHolder(val binding: ItemMyChatBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_chat, parent, false)
        return ViewHolder(ItemMyChatBinding.bind(view))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {
            val prevDate = datas[position].createDate

            val timeDiff = getTimeDiff(prevDate)

            titleTextView.text = datas[position].title
            timeTextView.text = getTimeStr(timeDiff)
            lastMassgaeTextView.text = datas[position].latestMessage
            locationTextView.text = datas[position].address

            val uri = Uri.parse(datas[position].imgPath)
            if (uri.toString().split((".")).last() == "svg") {
                svgRequestBuilder.load(uri)
                    .centerCrop()
                    .error(R.drawable.ic_error)
                    .into(image)
            } else {
                Glide.with(context)
                    .load(uri)
                    .error(R.drawable.ic_error)
                    .centerCrop()
                    .into(image)
            }
            if (datas[position].userId == App.prefs.userId) {
                editButton.setOnClickListener {
                    editTitleListener(datas[position].chatId, titleTextView.text.toString())
                    clickedPos = position
                }
            } else {
                editButton.visibility = View.GONE
            }
            myChatLayout.setOnClickListener {
                listener(datas[position].chatId)
            }
        }
    }


    override fun getItemCount() = datas.size
    fun setDatas(chatList: ArrayList<Chat>) {
        clearData()
        datas.addAll(chatList)
        notifyItemRangeInserted(0, chatList.size)
    }

    fun getData(pos: Int) = datas[pos]

    fun changeData(chatTitle: ChatTitle) {
        datas[clickedPos].title = chatTitle.title
        notifyItemChanged(clickedPos)
    }

    fun clearData() {
        val size = datas.size
        datas.clear()
        drawableList.clear()
        notifyItemRangeRemoved(0, size)
    }


    fun setListener(l: (chatId: Int) -> Unit) {
        listener = l
    }

    fun setEditTitleListener(l: (chatId: Int, title: String) -> Unit) {
        editTitleListener = l
    }
}