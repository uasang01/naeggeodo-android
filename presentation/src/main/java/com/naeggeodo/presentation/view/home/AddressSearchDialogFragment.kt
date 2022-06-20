package com.naeggeodo.presentation.view.home

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.naeggeodo.presentation.databinding.FragmentAddressSearchDialogBinding
import com.naeggeodo.presentation.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class AddressSearchDialogFragment : DialogFragment() {
    val binding get() = _binding!!
    private var _binding: FragmentAddressSearchDialogBinding? = null
    private val locationViewModel: LocationViewModel by activityViewModels()

    private var client: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return false
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentAddressSearchDialogBinding.inflate(LayoutInflater.from(requireContext()))

        initView()

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    private fun initView() {
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        binding.daumWebview.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            addJavascriptInterface(AndroidBridge(), "naeggeodoApp")
            settings.domStorageEnabled = true
            webViewClient = client
            webChromeClient = object : WebChromeClient() {
                // Grant permissions for cam
                override fun onCreateWindow(
                    view: WebView,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message
                ): Boolean {
                    val newWebView = WebView(requireContext())
                    newWebView.settings.javaScriptEnabled = true
                    val newDialog = Dialog(requireContext()).apply {
                        setContentView(newWebView)
                        setOnKeyListener { v, keyCode, event ->
                            if (keyCode == KeyEvent.KEYCODE_BACK && event.action != KeyEvent.ACTION_DOWN) {
                                this.dismiss()
                                dialog?.dismiss()
                                true
                            } else {
                                false
                            }
                        }
                    }
                    newDialog.show()
                    val lp = WindowManager.LayoutParams().apply {
                        copyFrom(newDialog.window!!.attributes)
                        width = WindowManager.LayoutParams.MATCH_PARENT
                        height = WindowManager.LayoutParams.MATCH_PARENT
                    }
                    newDialog.window!!.attributes = lp
                    newWebView.webChromeClient = object : WebChromeClient() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        override fun onPermissionRequest(request: PermissionRequest) {
                            request.grant(request.resources)
                        }

                        override fun onCloseWindow(window: WebView) {
                            newDialog.dismiss()
                        }
                    }
                    (resultMsg.obj as WebView.WebViewTransport).webView = newWebView
                    resultMsg.sendToTarget()
                    return true
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onReceivedSslError(
                    view: WebView,
                    handler: SslErrorHandler,
                    error: SslError
                ) {
                    // SSL 에러가 발생해도 계속 진행
//                    handler.proceed()
                }

                // 페이지 로딩 시작시 호출
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    Timber.e("페이지 시작 / $url")
                    binding.webProgress.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView, url: String) {
                    binding.webProgress.visibility = View.GONE
                    Timber.e("페이지 로딩 / $url")
                }
            }
            loadUrl("https://sanghun.s3.ap-northeast-2.amazonaws.com/postcode.html")
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.daumWebview.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    inner class AndroidBridge {
        @JavascriptInterface
        fun setAddress(address: String, buildingCode: String, apartment: String) {
            lifecycleScope.launch {
                Timber.e("address : $address, buildingCode : $buildingCode, apartment : $apartment")

                locationViewModel.apply {
                    setAddress(address)
                    setBuildingCode(buildingCode)
                    setApartment(apartment)
                }
                dialog?.dismiss()
            }
        }
    }
}
