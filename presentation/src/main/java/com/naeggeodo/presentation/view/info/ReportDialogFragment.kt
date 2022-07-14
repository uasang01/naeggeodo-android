package com.naeggeodo.presentation.view.info

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
import com.naeggeodo.presentation.databinding.DialogFragmentReportBinding
import com.naeggeodo.presentation.databinding.ItemSpinnerReportBinding
import com.naeggeodo.presentation.utils.dpToPx
import timber.log.Timber

class ReportDialogFragment(
    private val normalButtonText: String = "예",
    private val colorButtonText: String = "아니오",
    private val normalButtonListener: () -> Unit = {},
    private val colorButtonListener: (String, String) -> Unit = { _, _ -> }
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    private lateinit var binding: DialogFragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentReportBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = listOf("채팅방 신고", "채팅내용 신고")
        val itemBinding = ItemSpinnerReportBinding.inflate(LayoutInflater.from(requireContext()))
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner_report, items)
        binding.spinner.adapter = adapter
        binding.spinner.dropDownVerticalOffset = 35.dpToPx(requireContext())
        binding.spinner.setSelection(0)
//        binding.spinner.selectedItem
        binding.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    Timber.e("spinner item selected $p0, $p1, $pos, $p3")

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                    Timber.e("spinner item nothing selected $p0")
                }
            }


        binding.normalTextView.text = normalButtonText
        binding.normalTextView.setOnClickListener {
            normalButtonListener()
            dismiss()
        }
        binding.colorTextView.text = colorButtonText
        binding.colorButton.setOnClickListener {
//            colorButtonListener()
            val type = when (binding.spinner.selectedItemPosition) {
                0 -> ReportType.CHATMAIN.name
                1 -> ReportType.CHATDETAIL.name
                else -> ReportType.CHATMAIN.name
            }
            val contents = binding.contentEditText.text.toString()
            colorButtonListener(type, contents)
            dismiss()
        }
    }
}