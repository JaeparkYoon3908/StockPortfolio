package com.yjpapp.stockportfolio.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import org.koin.android.ext.android.inject

abstract class BaseActivity<T : ViewDataBinding>(
    private val layoutId: Int
) : AppCompatActivity() {
    private var _binding: T? = null
    val binding: T get() = _binding!!
    val preferenceController: PreferenceController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutId)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}