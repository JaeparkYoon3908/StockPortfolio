package com.yjpapp.stockportfolio.function.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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
import com.yjpapp.stockportfolio.base.BaseMVVMActivity
import com.yjpapp.stockportfolio.constance.StockPortfolioConfig
import com.yjpapp.stockportfolio.databinding.ActivityLoginBinding
import com.yjpapp.stockportfolio.function.main.MainActivity
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.model.response.RespFacebookUserInfo
import com.yjpapp.stockportfolio.util.StockLog
import org.koin.android.ext.android.inject


/**
 * 디자인 패턴 : MVVM
 * @author 윤재박
 * @since 2021.07
 */
class LoginActivity : BaseMVVMActivity() {
    private val TAG = LoginActivity::class.simpleName
    private val binding = binding<ActivityLoginBinding>(R.layout.activity_login)
    private val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(StockPortfolioConfig.GOOGLE_SIGN_CLIENT_ID)
            .requestEmail() // email addresses도 요청함
            .build()
    }
    private val mGoogleSignInClient by lazy { GoogleSignIn.getClient(applicationContext, gso) }
    private val facebookCallbackManager by lazy { CallbackManager.Factory.create() }

    private val viewModel: LoginViewModel by inject()
    private val preferenceController: PreferenceController by inject()

    interface LoginCallBack {
        fun onClick(view: View)
    }

    private val loginCallBack = object : LoginCallBack {
        override fun onClick(view: View) { //            startMainActivity()
            binding.value.run {
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

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initData() {
        preferenceController.getPreference(PrefKey.KEY_AUTO_LOGIN)?.let {
            if (it == "true") {
                startMainActivity()
            }
        }
        binding.value.apply {
            lifecycleOwner = this@LoginActivity
            callback = loginCallBack
        }
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.apply {
            loginResultData.observe(this@LoginActivity, { data ->
                StockLog.d(TAG, "email = ${data.email}")
                StockLog.d(TAG, "name = ${data.name}")
                StockLog.d(TAG, "userIndex = ${data.userIndex}")
                StockLog.d(TAG, "login_type = ${data.login_type}")
                preferenceController.apply {
                    setPreference(PrefKey.KEY_USER_INDEX, data.userIndex)
                    setPreference(PrefKey.KEY_USER_NAME, data.name)
                    setPreference(PrefKey.KEY_USER_EMAIL, data.email)
                    setPreference(PrefKey.KEY_USER_LOGIN_TYPE, data.login_type)
                    setPreference(PrefKey.KEY_USER_TOKEN, data.token)
                    setPreference(PrefKey.KEY_AUTO_LOGIN, true)
                }
                startMainActivity()
            })
        }
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        googleLoginResult.launch(signInIntent)
    }

    private val googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_CANCELED) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val acct: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                acct?.let {
//                    val personName = it.displayName
//                    val personGivenName = it.givenName
//                    val personFamilyName = it.familyName
//                    val personEmail = it.email
//                    val personId = it.id
//                    val personPhoto: Uri? = it.photoUrl
//                    StockLog.d(TAG, "handleSignInResult:personName $personName")
//                    StockLog.d(TAG, "handleSignInResult:personGivenName $personGivenName")
//                    StockLog.d(TAG, "handleSignInResult:personEmail $personEmail")
//                    StockLog.d(TAG, "handleSignInResult:personId $personId")
//                    StockLog.d(TAG, "handleSignInResult:personFamilyName $personFamilyName")
//                    StockLog.d(TAG, "handleSignInResult:personPhoto $personPhoto")
                    snsLoginSuccess(ReqSNSLogin(it.email!!, it.displayName!!, StockPortfolioConfig.SIGN_TYPE_GOOGLE))
                }
            } catch (e: ApiException) { // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                StockLog.e(TAG, "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    private fun naverSignIn() {
        val mOAuthLoginModule = OAuthLogin.getInstance()
        mOAuthLoginModule.init(
            this, StockPortfolioConfig.NAVER_SIGN_CLIENT_ID, StockPortfolioConfig.NAVER_SIGN_CLIENT_SECRET, getString(R.string.app_name)
                              )
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
                    val params = hashMapOf<String, String>()
                    params["Authorization"] = "$tokenType $accessToken"
                    viewModel.requestNaverUserInfo(params)
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
                    snsLoginSuccess(ReqSNSLogin(
                        email = respFacebookUserInfo.email,
                        name = respFacebookUserInfo.name,
                        loginType = StockPortfolioConfig.SIGN_TYPE_FACEBOOK)
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
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private fun snsLoginSuccess(reqSnsLogin: ReqSNSLogin) {
        viewModel.requestSNSLogin(applicationContext, reqSnsLogin)
    }
}