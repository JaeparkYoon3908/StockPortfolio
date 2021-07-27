package com.yjpapp.stockportfolio.function.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
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
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVVMActivity
import com.yjpapp.stockportfolio.constance.StockPortfolioConfig
import com.yjpapp.stockportfolio.databinding.ActivityLoginBinding
import com.yjpapp.stockportfolio.function.main.MainActivity
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.SNSLoginRequest
import com.yjpapp.stockportfolio.network.ServerRespCode
import com.yjpapp.stockportfolio.util.StockLog
import org.koin.android.ext.android.inject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
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

    private val loginViewModel: LoginViewModel by inject()
    private val preferenceController by lazy { PreferenceController.getInstance(applicationContext) }

    interface LoginCallBack {
        fun onClick(view: View)
    }

    private val loginCallBack = object : LoginCallBack {
        override fun onClick(view: View) { //            startMainActivity()
            binding.value.run {
                when (view.id) {
                    btnGoogleLogin.id -> {
//                        googleSignIn()
                        startMainActivity()
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
//        initView()
        initData()
        val params = HashMap<String, String>()
        params["user_index"] = "10003"
        loginViewModel.getUserInfo(applicationContext, params)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initView() {

    }

    private fun initData() {
        preferenceController.getPreference(PrefKey.KEY_AUTO_LOGIN)?.let {
            if (it == "true") {
                startAutoLogin()
            }
        }
        binding.value.apply {
            lifecycleOwner = this@LoginActivity
            callback = loginCallBack
        }
        setObserver()
    }

    private fun setObserver() {
        loginViewModel.apply {
            loginResultData.observe(this@LoginActivity, {
//                PreferenceController.getInstance(applicationContext).setPreference(PrefKey.KEY_USER_INDEX, it.userIndex)
//                PreferenceController.getInstance(applicationContext).setPreference(PrefKey.KEY_USER_TOKEN, it.token)
//                PreferenceController.getInstance(applicationContext).setPreference(PrefKey.KEY_AUTO_LOGIN, true)
                StockLog.d(TAG, "email = ${it.email}")
                StockLog.d(TAG, "name = ${it.name}")
                StockLog.d(TAG, "userIndex = ${it.userIndex}")
                StockLog.d(TAG, "login_type = ${it.login_type}")

            })

            autoLoginStatus.observe(this@LoginActivity, {
                when (it) {
                    ServerRespCode.OK -> {

                    }

                    ServerRespCode.BadRequest -> {

                    }
                }
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
                    snsLoginSuccess(SNSLoginRequest(personEmail, personName, StockPortfolioConfig.SIGN_TYPE_GOOGLE))
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
                    StockLog.d(TAG, "response.rawResponse : $response.rawResponse")
                    StockLog.d(TAG, "accessToken : ${AccessToken.getCurrentAccessToken().token}")
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

    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private fun snsLoginSuccess(snsLoginRequest: SNSLoginRequest) {
        loginViewModel.postUserInfo(applicationContext, snsLoginRequest)
    }

    private fun startAutoLogin() {
        //TODO 재 로그인요청
    }
}