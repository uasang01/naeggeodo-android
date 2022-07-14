package com.naeggeodo.presentation.view.info

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.naeggeodo.presentation.databinding.DialogFragmentSuggestBinding

class SuggestDialogFragment(
    private val normalButtonText: String = "예",
    private val colorButtonText: String = "아니오",
    private val normalButtonListener: () -> Unit = {},
    private val colorButtonListener: (String) -> Unit = {}
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    private lateinit var binding: DialogFragmentSuggestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentSuggestBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.normalTextView.text = normalButtonText
        binding.normalTextView.setOnClickListener {
            normalButtonListener()
            dismiss()
        }
        binding.colorTextView.text = colorButtonText
        binding.colorButton.setOnClickListener {
            colorButtonListener(binding.contentEditText.text.toString())
            dismiss()
        }
    }
}