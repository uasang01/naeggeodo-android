package com.naeggeodo.presentation.view.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naeggeodo.domain.model.Tag
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemTagBinding

class TagAdapter(private val context: Context, private var datas: ArrayList<Tag>) :
    RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    private var itemClickEvent: (Int) -> Unit = {}

    inner class ViewHolder(val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tag, parent, false)
        return ViewHolder(ItemTagBinding.bind(view))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {
            tagText.text = datas[position].tag

            tagCardView.setOnClickListener {
                itemClickEvent(position)
            }
        }
    }

    override fun getItemCount() = datas.size
//    fun getSelectedCategory() = if (selected == 0) null else datas[selected].category

    fun getData(pos: Int): String = datas[pos].tag

    fun setData(Tags: ArrayList<Tag>) {
        clearData()
        datas.addAll(Tags)
        notifyItemRangeChanged(0, Tags.size)
    }

    fun clearData() {
        val size = datas.size
        datas.clear()
        notifyItemRangeChanged(0, size)
    }

    fun setItemClickEvent(e: (Int) -> Unit) {
        itemClickEvent = e
    }
}