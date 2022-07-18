package com.naeggeodo.presentation.view.mychat

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.naeggeodo.domain.utils.ReportType
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.DialogFragmentChangeTitleBinding
import com.naeggeodo.presentation.databinding.DialogFragmentReportBinding
import com.naeggeodo.presentation.databinding.ItemSpinnerReportBinding
import com.naeggeodo.presentation.utils.dpToPx
import timber.log.Timber

class ChangeTitleDialogFragment(
    private val originTitle: String = "",
    private val normalButtonText: String = "취소",
    private val colorButtonText: String = "변경하기",
    private val normalButtonListener: () -> Unit = {},
    private val colorButtonListener: (String) -> Unit = {}
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    private lateinit var binding: DialogFragmentChangeTitleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentChangeTitleBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleEditText.setText(originTitle)

        binding.normalTextView.text = normalButtonText
        binding.normalTextView.setOnClickListener {
            normalButtonListener()
            dismiss()
        }
        binding.colorTextView.text = colorButtonText
        binding.colorButton.setOnClickListener {
            colorButtonListener(binding.titleEditText.text.toString())
            dismiss()
        }
    }
}