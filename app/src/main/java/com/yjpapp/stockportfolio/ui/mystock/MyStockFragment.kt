package com.yjpapp.stockportfolio.ui.mystock

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVVMFragment
import com.yjpapp.stockportfolio.database.sqlte.data.MyStockInfo
import com.yjpapp.stockportfolio.databinding.FragmentMyStockBinding
import org.koin.android.ext.android.inject

/**
 * 나의 주식 화면
 *
 * @author Yoon Jae-park
 * @since 2021.04
 */
class MyStockFragment : BaseMVVMFragment<FragmentMyStockBinding>() {
    private val mySockViewModel: MyStockViewModel by inject()
    private lateinit var myStockInputDialog: MyStockInputDialog
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
        setHasOptionsMenu(true)
        myStockInputDialog = MyStockInputDialog.getInstance(mContext, mySockViewModel)
        myStockAdapter = MyStockAdapter(mySockViewModel)
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mDataBinding.apply {
            recyclerviewMyStockFragment.layoutManager = layoutManager
            recyclerviewMyStockFragment.adapter = myStockAdapter
        }
        val myStockInfo = MyStockInfo(0,
                "가나다라마바사아자차카파타하",
                "500,000",
                "2021.04.16",
                "15%",
                "485,000",
                "500,000",
                "10")
        val myStockInfo2 = MyStockInfo(0,
                "카카오",
                "500,000",
                "2021.02.11",
                "5%",
                "600,000",
                "600,000",
                "51")
        val arrayList = mutableListOf<MyStockInfo>()
        arrayList.add(myStockInfo)
        mySockViewModel.myStockInfoList.value = arrayList
        setObserver()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            arrayList.add(myStockInfo2)
            mySockViewModel.myStockInfoList.postValue(arrayList)
        }, 3000)
    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_my_stock, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_MyStockFragment_Add -> {
                mySockViewModel.onAddButtonClick(myStockInputDialog, this@MyStockFragment.childFragmentManager)
            }
            R.id.menu_MyStockFragment_Edit -> {
                mySockViewModel.onEditButtonClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setObserver() {
        mySockViewModel.myStockInfoList.observe(this, Observer {
            myStockAdapter.notifyDataSetChanged()
        })

        mySockViewModel.showCompleteToast.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(mContext, "값을 모두 입력했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}