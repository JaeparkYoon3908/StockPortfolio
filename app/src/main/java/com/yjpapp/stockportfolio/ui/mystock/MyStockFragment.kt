package com.yjpapp.stockportfolio.ui.mystock

import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVVMFragment
import com.yjpapp.stockportfolio.databinding.FragmentMyStockBinding

class MyStockFragment: BaseMVVMFragment<FragmentMyStockBinding>() {
    private val mySockViewModel = MyStockViewModel()
    override fun getLayoutId(): Int {
        return R.layout.fragment_my_stock
    }

    override fun setViewModel() {
        mDataBinding.viewModel = mySockViewModel
    }
}