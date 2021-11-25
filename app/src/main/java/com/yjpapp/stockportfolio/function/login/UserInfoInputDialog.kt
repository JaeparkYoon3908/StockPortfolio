package com.yjpapp.stockportfolio.function.login

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.DialogUserInfoInputBinding

class UserInfoInputDialog(
    val mContext: Context,
    val data: Data
) : AlertDialog(mContext) {
    data class Data(
        var isNameExist: Boolean = false,
        var isEmailExist: Boolean = false
    )
    private var _binding: DialogUserInfoInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.dialog_user_info_input,
            null,
            false
        )
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        initView()
        initData()
    }

    private fun initView() {

    }

    private fun initData() {
        binding.data = data
    }
}