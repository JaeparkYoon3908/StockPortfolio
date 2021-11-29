package com.yjpapp.stockportfolio.function.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
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
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.constance.StockConfig
import com.yjpapp.stockportfolio.databinding.ActivityLoginBinding
import com.yjpapp.stockportfolio.function.main.MainActivity
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.model.response.RespFacebookUserInfo
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import com.yjpapp.stockportfolio.util.StockLog
import es.dmoral.toasty.Toasty
import org.koin.android.ext.android.inject


/**
 * 디자인 패턴 : MVVM
 * @author 윤재박
 * @since 2021.07
 */
class LoginActivity : BaseActivity() {
    private val TAG = LoginActivity::class.simpleName
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val gso by lazy {
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(StockConfig.GOOGLE_SIGN_CLIENT_ID)
            .requestEmail() // email addresses도 요청함
            .build()
    }
    private val mGoogleSignInClient by lazy { GoogleSignIn.getClient(applicationContext, gso) }
    private val facebookCallbackManager by lazy { CallbackManager.Factory.create() }

    private val viewModel: LoginViewModel by inject()

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
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initData()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    private fun initData() {
        preferenceController.apply {
            //마이 설정값이 없을 때 초기화
            if (!isExists(PrefKey.KEY_SETTING_AUTO_LOGIN)) {
                setPreference(PrefKey.KEY_SETTING_AUTO_LOGIN, true)
            }
            if (!isExists(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)) {
                setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH, true)
            }
            if (!isExists(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)) {
                setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD, true)
            }
            if (!isExists(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)) {
                setPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK, true)
            }
            if (!isExists(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)) {
                setPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK, true)
            }
            if (!isExists(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)) {
                setPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK, true)
            }
            //자동 로그인이 설정 돼 있으면
            getPreference(PrefKey.KEY_AUTO_LOGIN)?.let { isAutoLoginAble ->
                getPreference(PrefKey.KEY_SETTING_AUTO_LOGIN)?.let { isAutoLoginSetting ->
                    if (isAutoLoginAble == StockConfig.TRUE && isAutoLoginSetting == StockConfig.TRUE) {
                        val userEmail = getPreference(PrefKey.KEY_USER_EMAIL)?: ""
                        val userName = getPreference(PrefKey.KEY_USER_NAME)?: ""
                        val loginType = getPreference(PrefKey.KEY_USER_LOGIN_TYPE)?: ""
                        requestLogin(ReqSNSLogin(userEmail, userName, loginType))
                    }
                }
            }
        }
        binding.apply {
            lifecycleOwner = this@LoginActivity
            callback = loginCallBack
        }
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.apply {
            loginResultData.observe(this@LoginActivity, { response ->
                StockLog.d(TAG, "email = ${response.data.email}")
                StockLog.d(TAG, "name = ${response.data.name}")
                StockLog.d(TAG, "userIndex = ${response.data.userIndex}")
                StockLog.d(TAG, "login_type = ${response.data.login_type}")
                preferenceController.apply {
                    setPreference(PrefKey.KEY_USER_INDEX, response.data.userIndex)
                    setPreference(PrefKey.KEY_USER_NAME, response.data.name)
                    setPreference(PrefKey.KEY_USER_EMAIL, response.data.email)
                    setPreference(PrefKey.KEY_USER_LOGIN_TYPE, response.data.login_type)
                    setPreference(PrefKey.KEY_USER_TOKEN, response.data.token)
                    setPreference(PrefKey.KEY_AUTO_LOGIN, true)
                }
                startMainActivity()
            })

            respGetNaverUserInfo.observe(this@LoginActivity, { data ->
                if (data.message == "success") {
                    if (data.response.email.isEmpty() || data.response.name.isEmpty()) {
                        UserInfoInputDialog(
                            mContext,
                            UserInfoInputDialog.Data (
                                data.response.name.isNotEmpty(),
                                data.response.email.isNotEmpty())
                            { view: View, dialog: UserInfoInputDialog, sendData: UserInfoInputDialog.SendData ->
                                var user_email = data.response.email
                                var user_name = data.response.name
                                if (sendData.email.isNotEmpty()) {
                                    user_email = sendData.email
                                }
                                if (sendData.name.isNotEmpty()) {
                                    user_name = sendData.name
                                }
                                viewModel.requestLogin(
                                    ReqSNSLogin(
                                        user_email = user_email,
                                        user_name = user_name,
                                        login_type = StockConfig.LOGIN_TYPE_NAVER
                                    )
                                )
                            }
                        ).show()
                        return@observe
                    }
                    viewModel.requestLogin(
                        ReqSNSLogin(
                            user_email = data.response.email,
                            user_name = data.response.name,
                            login_type = StockConfig.LOGIN_TYPE_NAVER
                    ))
                } else {
                    ResponseAlertManger.showErrorAlert(
                        this@LoginActivity,
                        getString(R.string.Error_Msg_Normal)
                    )
                }
            })
        }
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        googleLoginResult.launch(signInIntent)
    }

    private val googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val acct: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                acct?.let {
                    val personName = it.displayName
                    val personGivenName = it.givenName
                    val personFamilyName = it.familyName
                    val personEmail = it.email
                    val personId = it.id
                    val personPhoto: Uri? = it.photoUrl
//                    StockLog.d(TAG, "handleSignInResult:personName $personName")
//                    StockLog.d(TAG, "handleSignInResult:personGivenName $personGivenName")
//                    StockLog.d(TAG, "handleSignInResult:personEmail $personEmail")
//                    StockLog.d(TAG, "handleSignInResult:personId $personId")
//                    StockLog.d(TAG, "handleSignInResult:personFamilyName $personFamilyName")
//                    StockLog.d(TAG, "handleSignInResult:personPhoto $personPhoto")
                    requestLogin(ReqSNSLogin(
                        it.email?: "",
                        it.displayName?: "",
                        StockConfig.LOGIN_TYPE_GOOGLE
                    ))
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
        val mOAuthLoginModule = OAuthLogin.getInstance()
        mOAuthLoginModule.init(
            this, StockConfig.NAVER_SIGN_CLIENT_ID, StockConfig.NAVER_SIGN_CLIENT_SECRET, getString(R.string.app_name))
        val mOAuthLoginHandler = @SuppressLint("HandlerLeak") object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                if (success) {
                    val accessToken: String = mOAuthLoginModule.getAccessToken(applicationContext)
                    val refreshToken: String = mOAuthLoginModule.getRefreshToken(applicationContext)
                    val expiresAt: Long = mOAuthLoginModule.getExpiresAt(applicationContext)
                    val tokenType: String = mOAuthLoginModule.getTokenType(applicationContext)
                    StockLog.d(TAG, "accessToken : $accessToken")
                    StockLog.d(TAG, "refreshToken : $refreshToken")
                    StockLog.d(TAG, "expiresAt : $expiresAt")
                    StockLog.d(TAG, "tokenType : $tokenType")
                    val authorization = "$tokenType $accessToken"
                    preferenceController.setPreference(PrefKey.KEY_NAVER_ACCESS_TOKEN, accessToken)
                    preferenceController.setPreference(PrefKey.KEY_NAVER_USER_TOKEN, authorization)
                    viewModel.requestGetNaverUserInfo()
                    startLoadingAnimation()
                } else {
                    val errorCode: String = mOAuthLoginModule.getLastErrorCode(applicationContext).code
                    val errorDesc: String = mOAuthLoginModule.getLastErrorDesc(applicationContext)
                    StockLog.d(TAG, "errorCode:$errorCode, errorDesc:$errorDesc")
                }
            }
        }
        mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler);
    }

    private fun facebookSignIn() {
        AppEventsLogger.activateApp(application)
        val config = ArrayList<String>()
        config.add("email")
        config.add("public_profile")
        LoginManager.getInstance().logInWithReadPermissions(this, config)
        LoginManager.getInstance().registerCallback(facebookCallbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                StockLog.d(TAG, "onSuccess")
                val accessToken = AccessToken.getCurrentAccessToken()
                val callback = GraphJSONObjectCallback { `object`, response ->
                    val respFacebookUserInfo = Gson().fromJson(response.rawResponse, RespFacebookUserInfo::class.java)
                    requestLogin(ReqSNSLogin(
                        user_email = respFacebookUserInfo.email,
                        user_name = respFacebookUserInfo.name,
                        login_type = StockConfig.LOGIN_TYPE_FACEBOOK)
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
        val intent = Intent(applicationContext, MainActivity::class.java)
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