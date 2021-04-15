package com.yjpapp.stockportfolio.ui.mystock

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVVMFragment
import com.yjpapp.stockportfolio.databinding.FragmentMyStockBinding

class MyStockFragment: BaseMVVMFragment<FragmentMyStockBinding>() {
    private val mySockViewModel = MyStockViewModel()
    private lateinit var myStockAdapter: MyStockAdapter
    override fun getLayoutId(): Int {
        return R.layout.fragment_my_stock
    }

    override fun setViewModel() {
        mDataBinding.viewModel = mySockViewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myStockAdapter = MyStockAdapter(mySockViewModel)
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mDataBinding.apply {
            recyclerviewMyStockFragment.layoutManager = layoutManager
            recyclerviewMyStockFragment.adapter = myStockAdapter
        }
        mySockViewModel.currentPrice.value = "50,000"

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            mySockViewModel.currentPrice.postValue("200,000")
        },5000)
        setObserver()
    }
    private fun setObserver(){
        mySockViewModel.currentPrice.observe(this, Observer {
            myStockAdapter.notifyDataSetChanged()
        })
    }
}