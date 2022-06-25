package com.naeggeodo.presentation.view

import android.content.Intent
import androidx.activity.viewModels
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.base.BaseActivity
import com.naeggeodo.presentation.databinding.ActivityLoginBinding
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.utils.ScreenState
import com.naeggeodo.presentation.utils.Util.loadingAnimation
import com.naeggeodo.presentation.utils.Util.showShortToast
import com.naeggeodo.presentation.viewmodel.LoginViewModel
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun initListener() {
        binding.naverButton.setOnClickListener {
            NaverIdLoginSDK.initialize(
                this,
                getString(R.string.naver_client_id),
                getString(R.string.naver_client_secret),
                getString(R.string.app_name)
            )

            // result callback
            val oauthLoginCallback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                    Timber.d("access token ${NaverIdLoginSDK.getAccessToken()}")
                    Timber.d("refresh token ${NaverIdLoginSDK.getRefreshToken()}")
                    Timber.d("expires at ${NaverIdLoginSDK.getExpiresAt()}")
                    Timber.d("token type ${NaverIdLoginSDK.getTokenType()}")
                    Timber.d("state ${NaverIdLoginSDK.getState()}")

                    // 프로필 불러오기. 뒤에 바로 로그아웃 하게되면 401 에러 발생.
//                    NidOAuthLogin().callProfileApi(object: NidProfileCallback<NidProfileResponse> {
//                        override fun onError(errorCode: Int, message: String) {
//                            Timber.e("onError to get profile / $errorCode / $message")
//                        }
//
//                        override fun onFailure(httpStatus: Int, message: String) {
//                            Timber.e("onFailure to get profile / $httpStatus / $message")
//                        }
//
//                        override fun onSuccess(result: NidProfileResponse) {
//                            Timber.e("profile result / ${result.profile} / ${result.message} / ${result.resultCode} ")
//                        }
//                    })


                    // 서버에 토큰 전송
                    requestLogin("naver", NaverIdLoginSDK.getAccessToken()!!)
//
//                    Timber.e("NaverIdLoginSDK.getState()  ${NaverIdLoginSDK.getState()}")
//                    // 로그아웃. 클라이언트에 저장된 토큰 삭제.
                    NaverIdLoginSDK.logout()
//                    Timber.e("NaverIdLoginSDK.getState()  ${NaverIdLoginSDK.getState()}")
//
////                    // 네이버 로그인 연동 해제
////                    NidOAuthLogin().callDeleteTokenApi(applicationContext, object : OAuthLoginCallback {
////                        override fun onSuccess() {
////                            //서버에서 토큰 삭제에 성공한 상태입니다.
////                        }
////                        override fun onFailure(httpStatus: Int, message: String) {
////                            // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
////                            // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
////                            Timber.d( "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
////                            Timber.d( "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
////                        }
////                        override fun onError(errorCode: Int, message: String) {
////                            // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
////                            // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
////                            onFailure(errorCode, message)
////                        }
////                    })

//                    NaverIdLoginSDK.logout()
                }


                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

                    Timber.e("naver login error occurred / errorCode:$errorCode, errorDesc:$errorDescription")
                    showShortToast(applicationContext, "네이버 로그인 실패")
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }

            // naver login start
            NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
        }


        binding.kakaoButton.setOnClickListener {
            // 로그인 성공
            val success: (OAuthToken) -> Unit = { token ->
                Timber.i("카카오계정으로 로그인 성공 ${token.accessToken}  ${token.scopes}")

                UserApiClient.instance.me { user, err ->
                    val kakaoId = user!!.id // ...
                }

                requestLogin("kakao", token.accessToken, token.scopes)
            }
            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Timber.e("카카오계정으로 로그인 실패. $error")
                } else if (token != null) {
                    success(token)
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(applicationContext)) {
                UserApiClient.instance.loginWithKakaoTalk(applicationContext) { token, error ->
                    if (error != null) {
                        Timber.e("카카오톡으로 로그인 실패. $error")

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(
                            applicationContext,
                            callback = callback
                        )
                    } else if (token != null) {
                        success(token)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(
                    applicationContext,
                    callback = callback
                )
            }
        }
    }

    override fun observeViewModels() {
        loginViewModel.loginResult.observe(this) { logIn ->
            App.prefs.accessToken = logIn.accessToken
            goToHome()
        }
        loginViewModel.mutableScreenState.observe(this) { state ->
            val layout = binding.progressView.root
            val view = binding.progressView.progressImage
            when (state!!) {
                ScreenState.LOADING -> loadingAnimation(applicationContext, layout, view, true)
                ScreenState.RENDER -> loadingAnimation(applicationContext, layout, view, false)
                ScreenState.ERROR -> loadingAnimation(applicationContext, layout, view, false)
            }
        }
    }

    private fun goToHome() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun requestLogin(platform: String, accessToken: String, scope: List<String>? = null) {
        val body = HashMap<String, String?>()
        body["access_token"] = accessToken
        body["token_type"] = "Bearer"
        body["scope"] = null
        loginViewModel.logIn(platform, body)
    }
}