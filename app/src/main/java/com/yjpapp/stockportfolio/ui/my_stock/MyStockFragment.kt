package com.yjpapp.stockportfolio.ui.my_stock

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yjpapp.stockportfolio.R

class MyStockFragment: Fragment() {
    private lateinit var mContext: Context
    private lateinit var mRootView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        mRootView = inflater.inflate(R.layout.fragment_my_stock, container, false)
        initLayout()
        return mRootView
    }

    private fun initLayout(){

    }
}