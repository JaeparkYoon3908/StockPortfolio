package com.yjpapp.stockportfolio.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.yjpapp.stockportfolio.database.sqlte.DatabaseController
import com.yjpapp.stockportfolio.preference.PreferenceController

/**
 * @author Yoon Jae-park
 * @since 2020.12
 */
abstract class BaseMVPActivity<VB: ViewBinding>: AppCompatActivity() {
    private lateinit var baseInteractor: BaseInteractor
    lateinit var mContext: Context
    lateinit var databaseController: DatabaseController
    lateinit var preferenceController: PreferenceController
    lateinit var binding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        databaseController = DatabaseController.getInstance(mContext)
        preferenceController = PreferenceController.getInstance(mContext)
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