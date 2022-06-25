package com.naeggeodo.presentation.view.create

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.naeggeodo.domain.model.Category
import com.naeggeodo.domain.utils.CategoryType
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.FragmentCategoryDialogBinding
import com.naeggeodo.presentation.utils.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class CategoryDialogFragment(
    private val items: List<Category>
) : DialogFragment() {
    val binding get() = _binding!!
    private var _binding: FragmentCategoryDialogBinding? = null

    private var selectedPos: Int? = null
    private var selectedView: TextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentCategoryDialogBinding.inflate(LayoutInflater.from(requireContext()))

        initView()

        val builder =
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog_rounded)

        return builder
            .setView(binding.root)
            .create()
    }

    private fun initView() {

        // insert items
        items.forEach { item ->
            val inflater = LayoutInflater.from(requireContext())
            // 카테고리 카드뷰
            val categoryView = inflater.inflate(R.layout.item_category_dialog, null)
            // 뷰 안의 TextView 에 카테고리 이름 저장
            val textView = categoryView.findViewById<TextView>(R.id.category_text_view)
            textView.text =
                enumValueOf<CategoryType>(item.category).korean
            // 크기, 마진 설정
            val lp = LinearLayout.LayoutParams(MATCH_PARENT, 35.dpToPx(requireContext()))
            lp.topMargin = 10.dpToPx(requireContext())
            categoryView.layoutParams = lp

            categoryView.setOnClickListener { clickEvent(textView, item.idx) }

            // 뷰 츄가
            binding.contentsBox.addView(categoryView)
        }

        binding.selectButton.setOnClickListener {

            dismiss()
        }
    }

    private fun clickEvent(view: TextView, pos: Int) {

        if (selectedPos == pos) return

        Timber.e("clicked $view / $pos")
        val prevPos = selectedPos
        selectedPos = pos
        val prevView = selectedView
        selectedView = view

        prevView?.let {
            it.background = ContextCompat.getDrawable(requireContext(), R.color.grey_F2F2F8)
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }

        view.background = ContextCompat.getDrawable(requireContext(), R.color.bg_selected_orange)
        view.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_selected_orange))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
