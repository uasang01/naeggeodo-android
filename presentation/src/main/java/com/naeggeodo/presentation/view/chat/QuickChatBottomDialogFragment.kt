package com.naeggeodo.presentation.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.databinding.FragmentQuickChatBottomDialogBinding
import com.naeggeodo.presentation.utils.Util.showShortToast
import com.naeggeodo.presentation.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuickChatBottomDialogFragment(
    private val phraseClickListener: (str: String) -> Unit,
    private val updateListener: (body: List<String?>) -> Unit
) : BottomSheetDialogFragment() {

    private val chatViewModel: ChatViewModel by activityViewModels()

    private var _binding: FragmentQuickChatBottomDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuickChatBottomDialogBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModels()
        initListeners()
        initView()
    }

    private fun initListeners() {
        binding.addPhraseButton.setOnClickListener {
            if (chatViewModel.quickChat.value!!.count { it.msg == null } == 0) {
                showShortToast(requireContext(), "더이상 추가할 수 없습니다")
                return@setOnClickListener
            }
            val newPhrase = binding.phraseEditText.text.toString()
            if (newPhrase.isEmpty()) {
                showShortToast(requireContext(), "문구를 입력해 주세요")
                return@setOnClickListener
            }
            val idx = chatViewModel.quickChat.value!!.filter {
                it.msg == null
            }.map {
                it.idx
            }.first()
            val newList = chatViewModel.quickChat.value!!.map {
                if (it.idx == idx) newPhrase else it.msg
            }
            updateListener(newList)
            binding.phraseEditText.setText("")
        }
    }

    fun observeViewModels() {
        chatViewModel.quickChat.observe(viewLifecycleOwner) {
            initView()
        }
    }

    fun initView() {
        binding.phrasesContainer.removeAllViews()

        chatViewModel.quickChat.value?.forEachIndexed { idx, item ->
            if (item.msg == null) return@forEachIndexed // continue

            val phraseView = layoutInflater.inflate(R.layout.item_phrase, null)
            val textView = phraseView.findViewById<TextView>(R.id.phrase_text_view)
            val deleteButton = phraseView.findViewById<ImageView>(R.id.phrase_image_view)
            textView.text = item.msg
            phraseView.setOnClickListener { view ->
                phraseClickListener(item.msg!!)
                dismiss()
            }
            deleteButton.setOnClickListener {
                val newList = chatViewModel.quickChat.value!!.mapIndexed { i, v ->
                    if (i == idx) {
                        null
                    } else {
                        v.msg
                    }
                }
                updateListener(newList)
            }

            binding.phrasesContainer.addView(phraseView)
        }
    }
}