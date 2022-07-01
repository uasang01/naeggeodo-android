package com.naeggeodo.presentation.view.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemGalleryBinding
import java.io.File

class GalleryAdapter(private val context: Context, private var datas: ArrayList<String>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private var selected: Int? = null
    private var itemClickEvent: (Int) -> Unit = {}

    inner class ViewHolder(val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(ItemGalleryBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {

            val pos = holder.adapterPosition
//            if(selected == null){
//                galleryCardView.strokeColor = ContextCompat.getColor(context, R.color.white)
//            }
            if (selected == pos) {
                galleryCardView.strokeColor = ContextCompat.getColor(context, R.color.main_color)
            } else {
                galleryCardView.strokeColor = ContextCompat.getColor(context, R.color.white)
            }

            Glide.with(context)
                .load(File(datas[pos]))
                .centerCrop()
                .into(galleryImageView)

            galleryCardView.setOnClickListener {
                val prevPos = selected
                if (selected == pos) {
                    galleryCardView.strokeColor = ContextCompat.getColor(context, R.color.white)
                    selected = null

                } else {
                    galleryCardView.strokeColor =
                        ContextCompat.getColor(context, R.color.main_color)
                    selected = pos
                }

                prevPos?.let { notifyItemChanged(it) }
                selected?.let { notifyItemChanged(it) }

                itemClickEvent(pos)
            }
        }
    }

    override fun getItemCount() = datas.size
    fun getSelectedPicture() = if (selected != null) datas[selected!!] else null

    fun setData(categories: ArrayList<String>) {
        clearData()
        datas.addAll(categories)
        notifyItemRangeChanged(0, categories.size)
    }

    fun clearData() {
        val size = datas.size
        datas.clear()
        notifyItemRangeChanged(0, size)
        selected = null
    }

    fun setItemClickEvent(pos: (Int) -> Unit) {
        itemClickEvent = pos
    }

    fun clearSelected() {
        selected?.let{ p ->
            notifyItemChanged(p)
            selected = null
        }
    }
}