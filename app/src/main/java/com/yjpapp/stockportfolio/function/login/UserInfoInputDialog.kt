package com.yjpapp.stockportfolio.function.login

import android.app.AlertDialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CustomDialogUserInfoInputBinding
import com.yjpapp.stockportfolio.util.DisplayUtils
import com.yjpapp.stockportfolio.util.PatternUtils
import es.dmoral.toasty.Toasty

class UserInfoInputDialog(
    val mContext: Context,
    val data: Data,
    val confirmBtnListener: (view: View, dialog: UserInfoInputDialog, sendData: SendData) -> Unit
) : AlertDialog(mContext) {
    data class Data(
        var isNameExist: Boolean = false,
        var isEmailExist: Boolean = false,
        var loginType: String = ""
    )
    data class SendData (
        var name: String = "",
        var email: String = ""
    )

    private var _binding: CustomDialogUserInfoInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.custom_dialog_user_info_input,
            null,
            false
        )
        setContentView(binding.root)

        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        setCancelable(false)
        //setWidthHeight()
        initView()
        initData()
    }

    private fun initView() {
        binding.btnConfirm.setOnClickListener { view ->
            //이메일만 없는경우
            if (data.isNameExist && !data.isEmailExist) {
                binding.etEmail.text?.let { etEmailText ->
                    if (etEmailText.isEmpty()) {
                        Toasty.error(mContext, "이메일을 입력해주세요.").show()
                    }
                    else if (!PatternUtils.isEmailForm(etEmailText.toString(), data.loginType)){
                        Toasty.error(mContext, "이메일을 형식에 맞게 입력해주세요.").show()
                    }
                    else {
                        confirmBtnListener(
                            view,
                            this@UserInfoInputDialog,
                            SendData (
                                email = etEmailText.toString()
                            )
                        )
                    }
                }
            }
            //이름만 없는경우
            else if (!data.isNameExist && data.isEmailExist) {
                binding.etName.text?.let { etNameText ->
                    if (etNameText.isNotEmpty()) {
                        confirmBtnListener(
                            view,
                            this@UserInfoInputDialog,
                            SendData (
                                name = etNameText.toString(),
                            )
                        )
                    }
                    else {
                        Toasty.error(mContext, "이름을 입력해주세요.").show()
                    }
                }
            }
            //이메일, 이름 둘 다 없는경우
            else if (!data.isNameExist && !data.isEmailExist) {
                binding.etEmail.text?.let { etEmailText ->
                    binding.etName.text?.let { etNameText ->
                        if (etEmailText.isEmpty() && etNameText.isEmpty()) {
                            Toasty.error(mContext, "이름, 이메일을 입력해주세요.").show()
                        }
                        else if (etNameText.isEmpty()) {
                            Toasty.error(mContext, "이름을 입력해주세요.").show()
                        }
                        else if (etEmailText.isEmpty()) {
                            Toasty.error(mContext, "이메일을 입력해주세요.").show()
                        }
                        else if (!PatternUtils.isEmailForm(etEmailText.toString(), data.loginType)) {
                            Toasty.error(mContext, "이메일을 형식에 맞게 입력해주세요.").show()
                        } else {
                            confirmBtnListener(
                                view,
                                this@UserInfoInputDialog,
                                SendData (
                                    name = etNameText.toString(),
                                    email = etEmailText.toString()
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initData() {
        binding.data = data
    }

    private fun setWidthHeight() {
        val windowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mContext.display
        } else {
            windowManager.defaultDisplay
        }
        val size = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics
        } else {
            display?.getSize(size)
        }
        val params: ViewGroup.LayoutParams? = window?.attributes
        val deviceWidth = size.x
        //사이즈 설정
        params?.width = (deviceWidth * 0.8).toInt()
        params?.height = DisplayUtils.dpToPx(350)
        window?.attributes = params as WindowManager.LayoutParams
    }
}