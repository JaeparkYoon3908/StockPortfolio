package com.yjpapp.stockportfolio.ui.mystock

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVVMFragment
import com.yjpapp.stockportfolio.database.room.MyStockEntity
import com.yjpapp.stockportfolio.databinding.FragmentMyStockBinding
import com.yjpapp.stockportfolio.ui.widget.MonthYearPickerDialog
import es.dmoral.toasty.Toasty
import org.koin.android.ext.android.inject

/**
 * 나의 주식 화면
 *
 * @author Yoon Jae-park
 * @since 2021.04
 */
class MyStockFragment : BaseMVVMFragment<FragmentMyStockBinding>() {
    private val mySockViewModel: MyStockViewModel by inject()
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
        mySockViewModel.onViewCreated()
        setHasOptionsMenu(true)

        val tempMyStockList = mutableListOf<MyStockEntity>()
        myStockAdapter = MyStockAdapter(tempMyStockList)
        mySockViewModel.myStockInfoList.value?.let {
            myStockAdapter = MyStockAdapter(it)
        }
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        mDataBinding.apply {
            recyclerviewMyStockFragment.layoutManager = layoutManager
            recyclerviewMyStockFragment.adapter = myStockAdapter
        }
        setObserver()
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
                showInputDialog()
            }
            R.id.menu_MyStockFragment_Edit -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setObserver() {
        mySockViewModel.run {
            myStockInfoList.observe(this@MyStockFragment, Observer {
                myStockAdapter.setMyStockList(it)
                mDataBinding.recyclerviewMyStockFragment.scrollToPosition(it.size - 1)
            })
            showErrorToast.observe(this@MyStockFragment, Observer {
                it.getContentIfNotHandled()?.let {
                    Toasty.error(mContext, R.string.MyStockInputDialog_Error_Message, Toast.LENGTH_SHORT).show()
                }
            })
            showDBSaveErrorToast.observe(this@MyStockFragment, Observer {
                it.getContentIfNotHandled()?.let{
                    Toasty.error(mContext, R.string.MyStockInputDialog_Error_Message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showInputDialog(){
        MyStockInputDialog.getInstance(mContext).apply {
            mySockViewModel.inputDialogNavigator = this
            binding.viewModel = mySockViewModel
            binding.etSellDate.setOnClickListener {
                var year = ""
                var month = ""
                if (binding.etSellDate.text.toString() != "") {
                    val split = binding.etSellDate.text.toString().split(".")
                    year = split[0]
                    month = split[1]
                }
                //매수 날짜 선택 다이얼로그 show
                MonthYearPickerDialog(year, month).apply {
                    setListener { view, year, month, dayOfMonth ->
//                        Toast.makeText(
//                            requireContext(),
//                            "Set date: $year/$month/$dayOfMonth",
//                            Toast.LENGTH_LONG
//                        ).show()
                        uiHandler.sendEmptyMessage(MyStockInputDialog.MSG.SELL_DATE_DATA_INPUT)
                        purchaseYear = year.toString()
                        purchaseMonth = if (month < 10) {
                            "0$month"
                        } else {
                            month.toString()
                        }
                        mySockViewModel.inputDialogPurchaseDate = "$purchaseYear.$purchaseMonth"
                    }
                    show(this@MyStockFragment.childFragmentManager, "MonthYearPickerDialog")
                }
            }
            binding.txtCancel.setOnClickListener { dismiss() }
            binding.txtComplete.setOnClickListener {
                if(mySockViewModel.saveMyStock()){
                    dismiss()
                }
            }

            show()
        }
    }
}