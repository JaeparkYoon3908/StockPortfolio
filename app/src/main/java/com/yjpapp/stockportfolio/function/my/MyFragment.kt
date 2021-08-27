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
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my
    }

    override fun setViewModel() {
        mDataBinding.viewModel = myViewModel
    }
}