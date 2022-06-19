package com.naeggeodo.presentation.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.ItemCategoryBinding

class CategoryAdapter(private var datas: ArrayList<String>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(ItemCategoryBinding.bind(view))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {
            category.text = datas[position]
        }
    }

    override fun getItemCount() = datas.size
    fun setData(categories: ArrayList<String>) {
        clearData()
        datas.addAll(categories)
        notifyItemRangeChanged(0, categories.size)
    }
    fun clearData(){
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