package com.naeggeodo.presentation.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.naeggeodo.domain.model.Category
import com.naeggeodo.domain.utils.CategoryType
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemCategoryBinding
import timber.log.Timber

class CategoryAdapter(private val context: Context, private var datas: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selected: Int = 0

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(ItemCategoryBinding.bind(view))
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
                val prevPos = selected
                selected = datas[position].idx
                notifyItemChanged(prevPos)
                notifyItemChanged(selected)
                Timber.e(getSelectedCategory())
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
//    fun addList(data: List<Restaurant>){
//        datas.addAll(data)
//        notifyItemInserted(datas.size)
//    }

//    fun clearList() {
//        Timber.e("data - $datas")
//
//        val s = datas.size
//        datas.clear()
//        notifyItemRangeRemoved(0, s);
//        Timber.e("data - $datas")
//    }

//    fun removeAt(pos: Int) {
//        println("datas = ${datas}")
//        println("pos = ${pos}")
//        datas.removeAt(pos)
//        notifyItemRemoved(pos);
//        notifyItemRangeChanged(pos, datas.size);
//    }
}