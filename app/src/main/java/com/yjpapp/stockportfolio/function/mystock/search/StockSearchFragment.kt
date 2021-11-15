package com.yjpapp.stockportfolio.function.mystock.search

import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVVMFragment
import com.yjpapp.stockportfolio.databinding.FragmentStockSearchBinding

/**
 * @link 주식 추가 다이얼로그 -> 회사명 입력 할 때 띄워지는 Fragment
 *
 * @author Yoon Jae-park
 * @since 2021.11
 */
class StockSearchFragment : BaseMVVMFragment<FragmentStockSearchBinding>() {
    /**
     * @return layout resource id
     */
    override fun getLayoutId(): Int {
        return R.layout.fragment_stock_search
    }
}