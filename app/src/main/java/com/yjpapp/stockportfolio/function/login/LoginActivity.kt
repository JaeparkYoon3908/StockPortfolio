package com.yjpapp.stockportfolio.function.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
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
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.model.request.ReqSNSLogin
import com.yjpapp.data.model.response.RespFacebookUserInfo
import com.yjpapp.data.network.ServerRespCode
import com.yjpapp.stockportfolio.BuildConfig
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.common.dialog.CommonOneBtnDialog
import com.yjpapp.stockportfolio.databinding.ActivityLoginBinding
import com.yjpapp.stockportfolio.extension.repeatOnStarted
import com.yjpapp.stockportfolio.function.MainActivity
import com.yjpapp.stockportfolio.util.StockLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * 디자인 패턴 : MVVM
 * @author 윤재박
 * @since 2021.07
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private val TAG = LoginActivity::class.java.simpleName
    private lateinit var _binding: ActivityLoginBinding
    private val binding get() = _binding
    private val gso by lazy {
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_SIGN_CLIENT_ID)
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
        override fun onClick(view: View) {
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
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        initData()
    }

    override fun onResume() {
        super.onResume()
        startAutoLogin()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    private fun initData() {
        binding.apply {
            callback = loginCallBack
            lifecycleOwner = this@LoginActivity
        }
        lifecycleScope.launch {
            repeatOnStarted {
                viewModel.uiState.collect { handleEvent(it) }
            }
        }
    }
    private fun handleEvent(event: LoginViewModel.Event) {
        when (event) {
            is LoginViewModel.Event.ResponseServerError -> {
                showOneBtnDialog(event.msg)
            }
            is LoginViewModel.Event.ResponseDeleteNaverUserInfo -> {
                stopLoadingAnimation()
            }
            is LoginViewModel.Event.ResponseLoginResultData -> {
                when (event.respLoginUserInfo.status) {
                    200 -> {
                        StockLog.d(TAG, "email = ${event.respLoginUserInfo.data.user_email}")
                        StockLog.d(TAG, "name = ${event.respLoginUserInfo.data.user_name}")
                        StockLog.d(TAG, "userIndex = ${event.respLoginUserInfo.data.user_index}")
                        StockLog.d(TAG, "login_type = ${event.respLoginUserInfo.data.login_type}")

                        viewModel.requestSetPreference(PrefKey.KEY_USER_INDEX, event.respLoginUserInfo.data.user_index.toString())
                        viewModel.requestSetPreference(PrefKey.KEY_USER_NAME, event.respLoginUserInfo.data.user_name)
                        viewModel.requestSetPreference(PrefKey.KEY_USER_EMAIL, event.respLoginUserInfo.data.user_email)
                        viewModel.requestSetPreference(PrefKey.KEY_USER_LOGIN_TYPE, event.respLoginUserInfo.data.login_type)
                        viewModel.requestSetPreference(PrefKey.KEY_USER_TOKEN, event.respLoginUserInfo.data.token)
                        viewModel.requestSetPreference(PrefKey.KEY_AUTO_LOGIN, true.toString())

                        startMainActivity()
                    }
                }
            }
        }
    }

    private fun startAutoLogin() {
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
                        this@LoginActivity.requestLogin(ReqSNSLogin(userEmail, userName, loginType))
                    }
                }
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

                        showOneBtnDialog(it)
                    }
                    StockLog.e(TAG, "signInResult:failed code=" + e.statusCode)
                }
            }
        }

    private fun naverSignIn() {
        NaverIdLoginSDK.initialize(
            context = this@LoginActivity,
            clientId = BuildConfig.NAVER_SIGN_CLIENT_ID,
            clientSecret = BuildConfig.NAVER_SIGN_CLIENT_SECRET,
            clientName = getString(R.string.app_name)
        )

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                val accessToken: String = NaverIdLoginSDK.getAccessToken()?: ""
                val refreshToken: String = NaverIdLoginSDK.getRefreshToken()?: ""
                val expiresAt: Long = NaverIdLoginSDK.getExpiresAt()
                val tokenType: String = NaverIdLoginSDK.getTokenType()?: ""
                val state: String = NaverIdLoginSDK.getState().toString()
                StockLog.d(TAG, "accessToken : $accessToken")
                StockLog.d(TAG, "refreshToken : $refreshToken")
                StockLog.d(TAG, "expiresAt : $expiresAt")
                StockLog.d(TAG, "tokenType : $tokenType")
                StockLog.d(TAG, "state : $state")

                if (accessToken.isEmpty()) {
                    onFailure(700, "accessToken is empty")
                    return
                }
                if (refreshToken.isEmpty()) {
                    onFailure(701, "refreshToken is empty")
                    return
                }
                if (tokenType.isEmpty()) {
                    onFailure(702, "tokenType is empty")
                    return
                }
                val authorization = "$tokenType $accessToken"
                viewModel.requestSetPreference(PrefKey.KEY_NAVER_ACCESS_TOKEN, accessToken)
                viewModel.requestSetPreference(PrefKey.KEY_NAVER_USER_TOKEN, authorization)
                startLoadingAnimation()

                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse>{
                    override fun onError(errorCode: Int, message: String) {
                        StockLog.d(TAG, "onError : $message")
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.Login_Naver_Login_Error_Msg, message)
                            ,Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        StockLog.d(TAG, "onFailure : $message")
                    }

                    override fun onSuccess(result: NidProfileResponse) {
                        StockLog.d(TAG, "onSuccess : ${result.profile?.toString()}")
                        result.profile?.let {
                            if (it.email.isNullOrEmpty()) {
                                onError(704, "email data is empty")
                                return
                            }
                            if (it.name.isNullOrEmpty()) {
                                onError(704, "name data is empty")
                                return
                            }
                            requestLogin(
                                ReqSNSLogin(
                                    user_email = it.email?: "",
                                    user_name = it.name?: "",
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
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.Login_Naver_Login_Error_Msg, message)
                    ,Toast.LENGTH_SHORT
                ).show()
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
                        val respFacebookUserInfo = Json.decodeFromString<RespFacebookUserInfo>(response.rawResponse)
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
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        stopLoadingAnimation()
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
//        binding.ivLoading.startAnimation()
    }

    private fun stopLoadingAnimation() {
        binding.viewMasking.visibility = View.GONE
        binding.ivLoading.visibility = View.GONE
//        binding.ivLoading.stopAnimation()
    }

    private fun showOneBtnDialog(msg: String) {
        if (viewModel.isDialogShowing) {
            return
        }
        val fragmentDialog = CommonOneBtnDialog(
            this@LoginActivity,
            CommonOneBtnDialog.CommonOneBtnData(
                noticeText = msg,
                btnText = getString(R.string.Common_Ok),
                btnListener = { _: View, dialog ->
                    dialog.dismiss()
                    viewModel.isDialogShowing = false
                }
            )
        )
        fragmentDialog.show(supportFragmentManager, TAG)
        viewModel.isDialogShowing = true
    }
}