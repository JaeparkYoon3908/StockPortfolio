package com.yjpapp.stockportfolio.ui.smsauth

import android.os.Bundle
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVVMActivity
import com.yjpapp.stockportfolio.databinding.ActivitySmsAuthBinding
import kotlinx.android.synthetic.main.activity_sms_auth.*
import kotlinx.android.synthetic.main.dialog_input_income_note.*
import kotlinx.android.synthetic.main.item_my_stock_list.*

class SMSAuthActivity: BaseMVVMActivity()  {
    private val binding by binding<ActivitySmsAuthBinding>(R.layout.activity_sms_auth)
    private val smsAuthViewModel = SMSAuthViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@SMSAuthActivity  // **중요** binding에 LifeCycleOwner을 지정해줘야 LiveData가 실시간으로 변화
            viewModel = smsAuthViewModel //viewModel을 지정해야 onTextChanged가 작동됨.
        }
        setMyPhoneNum()
    }

    private fun setMyPhoneNum(){
//        et_phone_num.setText(Utils.getMyPhoneNum(this))
        et_phone_num.setText("01048533908")
        smsAuthViewModel.phoneNum.value = et_phone_num.text.toString()
    }

}