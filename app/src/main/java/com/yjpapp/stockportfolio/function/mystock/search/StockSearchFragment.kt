package com.yjpapp.stockportfolio.function.mystock.search

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseFragment
import com.yjpapp.stockportfolio.databinding.FragmentStockSearchBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @link 주식 추가 다이얼로그 -> 회사명 입력 할 때 띄워지는 Fragment
 *
 * @author Yoon Jae-park
 * @since 2021.11
 */
@AndroidEntryPoint
class StockSearchFragment : BaseFragment<FragmentStockSearchBinding>(R.layout.fragment_stock_search) {
    private val TAG = StockSearchFragment::class.java.simpleName
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            adapter = StockSearchAdapter()
        }
    }
}