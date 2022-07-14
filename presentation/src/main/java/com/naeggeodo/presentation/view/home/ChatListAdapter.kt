package com.naeggeodo.presentation.view.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naeggeodo.domain.model.Chat
import com.naeggeodo.domain.utils.OrderTimeType
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemChatListBinding
import com.naeggeodo.presentation.utils.Util.getSvgRequestBuilder
import com.naeggeodo.presentation.utils.Util.getTimeDiff
import com.naeggeodo.presentation.utils.Util.getTimeStr


class ChatListAdapter(
    private val context: Context,
    private var datas: ArrayList<Chat>,
    private var listener: (pos: Int) -> Unit = {}
) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    val drawableList = hashMapOf<Int, Drawable>()
    val svgRequestBuilder = getSvgRequestBuilder(context)

    inner class ViewHolder(val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_list, parent, false)
        return ViewHolder(ItemChatListBinding.bind(view))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {
            val prevDate = datas[position].createDate

            val timeDiff = getTimeDiff(prevDate)

            title.text = datas[position].title
            time.text = getTimeStr(timeDiff)
            count.text = "인원 ${datas[position].currentCount}명 / ${datas[position].maxCount}명"
            orderTypeTextView.text =
                enumValueOf<OrderTimeType>(datas[position].orderTimeType).korean

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
            enterContainer.setOnClickListener {
                listener(position)
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

    fun clearData() {
        val size = datas.size
        datas.clear()
        drawableList.clear()
        notifyItemRangeRemoved(0, size)
    }


    fun setListener(l: (pos: Int) -> Unit) {
        listener = l
    }
}