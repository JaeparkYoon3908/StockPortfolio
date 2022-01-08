package com.yjpapp.stockportfolio.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding

/**
 * @author Yoon Jae-park
 * @since 2020.12
 */
abstract class BaseMVPActivity<VB: ViewBinding>: BaseActivity() {
    lateinit var binding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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