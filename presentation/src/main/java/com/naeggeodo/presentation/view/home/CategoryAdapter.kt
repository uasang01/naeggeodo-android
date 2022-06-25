package com.naeggeodo.presentation.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.naeggeodo.domain.model.Category
import com.naeggeodo.domain.utils.CategoryType
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemCategoryTabBinding

class CategoryAdapter(private val context: Context, private var datas: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selected: Int = 0
    private var itemClickEvent: (Int) -> Unit = {}

    inner class ViewHolder(val binding: ItemCategoryTabBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_tab, parent, false)
        return ViewHolder(ItemCategoryTabBinding.bind(view))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {
            category.text = enumValueOf<CategoryType>(datas[position].category).korean

            if (selected == position) {
                category.setTextColor(ContextCompat.getColor(context, R.color.orange_EF6212))
            } else {
                category.setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            category.setOnClickListener {
                if (selected == position) return@setOnClickListener

                val prevPos = selected
                selected = datas[position].idx
                notifyItemChanged(prevPos)
                notifyItemChanged(selected)

                itemClickEvent(position)
            }
        }
    }

    override fun getItemCount() = datas.size
    fun getSelectedCategory() = if (selected == 0) null else datas[selected].category

    fun setData(categories: ArrayList<Category>) {
        clearData()
        datas.addAll(categories)
        notifyItemRangeChanged(0, categories.size)
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