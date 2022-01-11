package com.yjpapp.stockportfolio.function

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.databinding.ActivitySplashBinding
import com.yjpapp.stockportfolio.function.login.LoginActivity
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import java.util.*


/**
 * 앱 실행 시 오프닝 화면
 *
 * @author Yoon Jae-park
 * @since 2020.10
 */

class SplashActivity: BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val permissionList = arrayOf<String>(
        Manifest.permission.READ_PHONE_STATE
//            Manifest.permission.READ_SMS,
//            Manifest.permission.READ_PHONE_NUMBERS,
//            Manifest.permission.READ_PHONE_STATE,
    )

    private val PERMISSION_REQUEST_CODE = 0 // 임의 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        startMainActivity()
    }

    private fun initData() {
        preferenceController.apply {
            //마이 설정값이 없을 때 초기화
            if (!isExists(PrefKey.KEY_SETTING_AUTO_LOGIN)) {
                setPreference(PrefKey.KEY_SETTING_AUTO_LOGIN, true)
            }
            //나의 주식
            if (!isExists(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)) {
                setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH, true)
            }
            if (!isExists(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)) {
                setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD, true)
            }
            if (!isExists(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)) {
                setPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK, true)
            }
            //수익 노트
            if (!isExists(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)) {
                setPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK, true)
            }
            //메모
            if (!isExists(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)) {
                setPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK, true)
            }
            if (!isExists(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK)) {
                setPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK, true)
            }
        }
    }

    private fun initLayout() {}

    private fun startMainActivity(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(applicationContext, LoginActivity::class.java)
            finish()
            startActivity(intent)

        }, 1500)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // requestPermission의 두번째 매개변수는 배열이므로 아이템이 여러개 있을 수 있기 때문에 결과를 배열로 받는다.
            //해당 권한이 거절된 경우.
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "앱을 시작하려면 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            } else {
                //권한이 허용된 경우 다음 코드 진행
                startMainActivity()
            }
        }
    }

    /**
     * 퍼미션 부분.
     */
    private fun checkPermissions(vararg permissions: String): Boolean {
        var result: Int
        val permissionsNeeded: MutableList<String> = ArrayList()
        permissions.forEach {
            result = ContextCompat.checkSelfPermission(this, it)
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(it)
            }
        }
        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
            return false
        }
        return true
    }

    private fun checkPermission(): Boolean {
        permissionList.forEach {
            if(checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, permissionList, PERMISSION_REQUEST_CODE)
    }

}