package com.yjpapp.stockportfolio.function.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.rewarded.RewardedAd
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVVMFragment
import com.yjpapp.stockportfolio.databinding.FragmentMyBinding
import org.koin.android.ext.android.inject

class MyFragment : BaseMVVMFragment<FragmentMyBinding>() {

    private val myViewModel: MyViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initData()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my
    }

    private fun initLayout() {

    }

    private fun initData() {
        mDataBinding.apply {
            viewModel = myViewModel
            callBack = this@MyFragment.callBack
            lifecycleOwner = this@MyFragment
        }
    }

    interface CallBack {
        fun onClick(view: View)
        fun onSwitchClick(view: View)
    }

    private val callBack = object : CallBack {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.btn_logout -> {
                    //TODO 로그아웃
                }
                R.id.btn_member_off -> {
                    //TODO 회원 탈퇴
                }
            }
        }

        override fun onSwitchClick(view: View) {
            when (view.id) {

            }
        }

    }
}