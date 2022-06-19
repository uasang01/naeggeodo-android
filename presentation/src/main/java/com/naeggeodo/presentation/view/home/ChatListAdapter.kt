package com.naeggeodo.presentation.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemChatListBinding

class ChatListAdapter(private var datas: ArrayList<String>) :
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
//            image.setImageResource()
            title.text = ""
            time.text = ""
            count.text = ""
            enterContainer.setOnClickListener { }
        }
    }

    override fun getItemCount() = datas.size
    fun setData(chatList: ArrayList<String>) {
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