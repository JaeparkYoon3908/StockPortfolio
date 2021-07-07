package com.yjpapp.stockportfolio.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.yjpapp.stockportfolio.database.preference.PreferenceController
import com.yjpapp.stockportfolio.database.sqlte.DatabaseController

/**
 * @author Yoon Jae-park
 * @since 2020.12
 */
abstract class BaseMVPActivity<VB: ViewBinding>: BaseActivity() {
    private lateinit var baseInteractor: BaseInteractor
    lateinit var binding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseInteractor = BaseInteractor.getInstance(mContext)
        binding = getViewBinding()
        setContentView(binding.root)
        initData()
        initLayout()
    }

    abstract fun initData()
    abstract fun initLayout()

//    fun logcat(msg: String){
//        if(BuildConfig.LOG_CAT) Log.d(javaClass.simpleName, msg)
//    }

    abstract fun getViewBinding(): VB
}