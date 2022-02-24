package com.yjpapp.stockportfolio.function.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.facebook.*
import com.facebook.GraphRequest.GraphJSONObjectCallback
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.nhn.android.naverlogin.OAuthLogin
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.common.dialog.CommonOneBtnDialog
import com.yjpapp.stockportfolio.databinding.ActivityLoginBinding
import com.yjpapp.stockportfolio.function.MainActivity
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.model.response.RespFacebookUserInfo
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import com.yjpapp.stockportfolio.network.ServerRespCode
import com.yjpapp.stockportfolio.util.StockLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * 디자인 패턴 : MVVM
 * @author 윤재박
 * @since 2021.07
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val TAG = LoginActivity::class.simpleName
    private val gso by lazy {
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(StockConfig.GOOGLE_SIGN_CLIENT_ID)
            .requestEmail() // email addresses도 요청함
            .build()
    }
    private val mGoogleSignInClient by lazy { GoogleSignIn.getClient(this@LoginActivity, gso) }
    private val facebookCallbackManager by lazy { CallbackManager.Factory.create() }

    private val viewModel: LoginViewModel by viewModels()

    interface LoginCallBack {
        fun onClick(view: View)
    }

    private val loginCallBack = object : LoginCallBack {
        override fun onClick(view: View) { //            startMainActivity()
            binding.run {
                when (view.id) {
                    btnGoogleLogin.id -> {
                        googleSignIn()
                    }

                    btnNaverLogin.id -> {
                        naverSignIn()
                    }

                    btnFacebookLogin.id -> {
                        facebookSignIn()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    private fun initData() {
        viewModel.run {
            val isAutoLoginAble = requestGetPreference(PrefKey.KEY_AUTO_LOGIN)
            val isAutoLoginSetting = requestGetPreference(PrefKey.KEY_SETTING_AUTO_LOGIN)
            if (isAutoLoginAble == StockConfig.TRUE && isAutoLoginSetting == StockConfig.TRUE) {
                val userEmail = requestGetPreference(PrefKey.KEY_USER_EMAIL)
                val userName = requestGetPreference(PrefKey.KEY_USER_NAME)
                val loginType = requestGetPreference(PrefKey.KEY_USER_LOGIN_TYPE)
                when (loginType) {
                    StockConfig.LOGIN_TYPE_NAVER -> {
                        naverSignIn()
                    }
                    else -> {
                        requestLogin(ReqSNSLogin(userEmail, userName, loginType))
                    }
                }
            }
        }
        binding.apply {
            callback = loginCallBack
            lifecycleOwner = this@LoginActivity
        }
        subscribeUI()
    }

    private fun subscribeUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResultData.collect { response ->
                    if (response.status == ServerRespCode.OK) {
                        StockLog.d(TAG, "email = ${response.data.email}")
                        StockLog.d(TAG, "name = ${response.data.name}")
                        StockLog.d(TAG, "userIndex = ${response.data.userIndex}")
                        StockLog.d(TAG, "login_type = ${response.data.login_type}")

                        viewModel.requestSetPreference(PrefKey.KEY_USER_INDEX, response.data.userIndex.toString())
                        viewModel.requestSetPreference(PrefKey.KEY_USER_NAME, response.data.name)
                        viewModel.requestSetPreference(PrefKey.KEY_USER_EMAIL, response.data.email)
                        viewModel.requestSetPreference(PrefKey.KEY_USER_LOGIN_TYPE, response.data.login_type)
                        viewModel.requestSetPreference(PrefKey.KEY_USER_TOKEN, response.data.token)
                        viewModel.requestSetPreference(PrefKey.KEY_AUTO_LOGIN, true.toString())

                        startMainActivity()
                    }
                }

                viewModel.serverError.collect { errorCode ->
                    when (errorCode) {
                        ServerRespCode.NetworkNotConnected -> {
                            ResponseAlertManger.showNetworkConnectErrorAlert(this@LoginActivity)
                            stopLoadingAnimation()
                        }
                    }
                }
            }
        }

        viewModel.apply {
            respDeleteNaverUserInfo.observe(this@LoginActivity) { data ->
                stopLoadingAnimation()
            }
        }

    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        googleLoginResult.launch(signInIntent)
    }

    private val googleLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {
                    val acct: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                    acct?.let {
                        val personName = it.displayName
                        val personGivenName = it.givenName
                        val personFamilyName = it.familyName
                        val personEmail = it.email
                        val personId = it.id
                        val personPhoto: Uri? = it.photoUrl
                        StockLog.d(TAG, "handleSignInResult:personName $personName")
                        StockLog.d(TAG, "handleSignInResult:personGivenName $personGivenName")
                        StockLog.d(TAG, "handleSignInResult:personEmail $personEmail")
                        StockLog.d(TAG, "handleSignInResult:personId $personId")
                        StockLog.d(TAG, "handleSignInResult:personFamilyName $personFamilyName")
                        StockLog.d(TAG, "handleSignInResult:personPhoto $personPhoto")

                        requestLogin(
                            ReqSNSLogin(
                                it.email ?: "",
                                it.displayName ?: "",
                                StockConfig.LOGIN_TYPE_GOOGLE
                            )
                        )
                    }
                } catch (e: ApiException) { // The ApiException status code indicates the detailed failure reason.
                    // Please refer to the GoogleSignInStatusCodes class reference for more information.
                    e.message?.let {
                        val msg = "${getString(R.string.Error_Msg_Normal)} code : ${e.statusCode}"
                        ResponseAlertManger.showErrorAlert(this, it)
                    }
                    StockLog.e(TAG, "signInResult:failed code=" + e.statusCode)
                }
            }
        }

    private fun naverSignIn() {
        NaverIdLoginSDK.initialize(
            context = this@LoginActivity,
            clientId = StockConfig.NAVER_SIGN_CLIENT_ID,
            clientSecret = StockConfig.NAVER_SIGN_CLIENT_SECRET,
            clientName = getString(R.string.app_name)
        )

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                val accessToken: String? = NaverIdLoginSDK.getAccessToken()
                val refreshToken: String? = NaverIdLoginSDK.getRefreshToken()
                val expiresAt: Long = NaverIdLoginSDK.getExpiresAt()
                val tokenType: String? = NaverIdLoginSDK.getTokenType()
                val state = NaverIdLoginSDK.getState().toString()
                StockLog.d(TAG, "accessToken : $accessToken")
                StockLog.d(TAG, "refreshToken : $refreshToken")
                StockLog.d(TAG, "expiresAt : $expiresAt")
                StockLog.d(TAG, "tokenType : $tokenType")
                StockLog.d(TAG, "state : $state")

                val authorization = "$tokenType $accessToken"
                viewModel.requestSetPreference(PrefKey.KEY_NAVER_ACCESS_TOKEN, accessToken)
                viewModel.requestSetPreference(PrefKey.KEY_NAVER_USER_TOKEN, authorization)
                startLoadingAnimation()

                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse>{
                    override fun onError(errorCode: Int, message: String) {
                        StockLog.d(TAG, "onError : $message")
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        StockLog.d(TAG, "onFailure : $message")
                    }

                    override fun onSuccess(result: NidProfileResponse) {
                        StockLog.d(TAG, "onSuccess : ${result.profile?.toString()}")
                        result.profile?.let {
                            if (it.email.isNullOrEmpty()) {
                                return
                            }
                            if (it.name.isNullOrEmpty()) {
                                return
                            }
                            viewModel.requestLogin(
                                ReqSNSLogin(
                                    user_email = it.email!!,
                                    user_name = it.name!!,
                                    login_type = StockConfig.LOGIN_TYPE_NAVER
                                )
                            )
                        }
                    }
                })
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@LoginActivity,"errorCode:$errorCode, errorDesc:$errorDescription",Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }

    private fun facebookSignIn() {
        AppEventsLogger.activateApp(application)
        val config = ArrayList<String>().apply {
            add("email")
            add("public_profile")
        }
        LoginManager.getInstance().logInWithReadPermissions(this, config)
        LoginManager.getInstance()
            .registerCallback(facebookCallbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    StockLog.d(TAG, "onSuccess")
                    val accessToken = AccessToken.getCurrentAccessToken()
                    val callback = GraphJSONObjectCallback { `object`, response ->
                        val respFacebookUserInfo =
                            Gson().fromJson(response.rawResponse, RespFacebookUserInfo::class.java)
                        requestLogin(
                            ReqSNSLogin(
                                user_email = respFacebookUserInfo.email,
                                user_name = respFacebookUserInfo.name,
                                login_type = StockConfig.LOGIN_TYPE_FACEBOOK
                            )
                        )
                    }
                    val request = GraphRequest.newMeRequest(accessToken, callback)
                    val originField = "name, email"
                    val parameters = Bundle()
                    parameters.putString("fields", originField)
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    StockLog.d(TAG, "onCancel")
                }

                override fun onError(exception: FacebookException) {
                    StockLog.d(TAG, "onError : ${exception.stackTrace}")
                }
            })
    }

    private fun startMainActivity() {
        stopLoadingAnimation()
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private fun requestLogin(reqSnsLogin: ReqSNSLogin) {
        startLoadingAnimation()
        viewModel.requestLogin(reqSnsLogin)
    }

    private fun startLoadingAnimation() {
        binding.viewMasking.visibility = View.VISIBLE
        binding.ivLoading.visibility = View.VISIBLE
        binding.ivLoading.startAnimation()
    }

    private fun stopLoadingAnimation() {
        binding.viewMasking.visibility = View.GONE
        binding.ivLoading.visibility = View.GONE
        binding.ivLoading.stopAnimation()
    }
}