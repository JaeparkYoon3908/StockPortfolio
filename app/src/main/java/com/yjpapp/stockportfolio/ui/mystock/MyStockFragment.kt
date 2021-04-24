package com.yjpapp.stockportfolio.ui.mystock

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ethanhua.skeleton.Skeleton
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
class MyStockFragment : BaseMVVMFragment<FragmentMyStockBinding>(), MyStockAdapter.AdapterCallBack {
    private val myStockViewModel: MyStockViewModel by inject()
    private lateinit var myStockAdapter: MyStockAdapter
    override fun getLayoutId(): Int {
        return R.layout.fragment_my_stock
    }

    override fun setViewModel() {
        mDataBinding.viewModel = myStockViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myStockViewModel.onViewCreated()
        setHasOptionsMenu(true)

        val tempMyStockList = mutableListOf<MyStockEntity>()
        myStockAdapter = MyStockAdapter(tempMyStockList)
        myStockViewModel.myStockInfoList.value?.let {
            myStockAdapter = MyStockAdapter(it)
        }
        myStockAdapter.setCallBack(this)
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        mDataBinding.apply {
            recyclerviewMyStockFragment.layoutManager = layoutManager
            recyclerviewMyStockFragment.adapter = myStockAdapter
        }
        setObserver()
        startSkeletonAnimation()
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
                myStockViewModel.apply {
                    inputDialogSubjectName = ""
                    inputDialogPurchaseDate = ""
                    inputDialogPurchasePrice = ""
                    inputDialogPurchaseCount = ""
                }
                showInputDialog()
            }
            R.id.menu_MyStockFragment_Edit -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setObserver() {
        myStockViewModel.run {
            myStockInfoList.observe(this@MyStockFragment, Observer {
                myStockAdapter.setMyStockList(it)
                mDataBinding.recyclerviewMyStockFragment.scrollToPosition(it.size - 1)
            })
            showErrorToast.observe(this@MyStockFragment, Observer {
                it.getContentIfNotHandled()?.let {
                    Toasty.error(
                        mContext,
                        R.string.MyStockInputDialog_Error_Message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            showDBSaveErrorToast.observe(this@MyStockFragment, Observer {
                it.getContentIfNotHandled()?.let {
                    Toasty.error(
                        mContext,
                        R.string.MyStockInputDialog_Error_Message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun showInputDialog(){
        MyStockInputDialog.getInstance(mContext).apply {
            myStockViewModel.inputDialogController = this
            binding.viewModel = myStockViewModel
            binding.etPurchaseDate.setOnClickListener {
                var year = ""
                var month = ""
                if (binding.etPurchaseDate.text.toString() != "") {
                    val split = binding.etPurchaseDate.text.toString().split(".")
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
                        myStockViewModel.inputDialogPurchaseDate = "$purchaseYear.$purchaseMonth"
                    }
                    show(this@MyStockFragment.childFragmentManager, "MonthYearPickerDialog")
                }
            }
            binding.txtCancel.setOnClickListener { dismiss() }
            binding.txtComplete.setOnClickListener {
                if(myStockViewModel.saveMyStock()){
                    dismiss()
                    startSkeletonAnimation()
                }
            }
            show()
        }
    }

    override fun onEditClick(myStockEntity: MyStockEntity?) {
        myStockViewModel.apply {
            myStockEntity?.let {
                inputDialogSubjectName = it.subjectName
                inputDialogPurchaseDate = it.purchaseDate
                inputDialogPurchasePrice = it.purchasePrice
                inputDialogPurchaseCount = it.purchaseCount
            }
        }
        showInputDialog()
    }

    override fun onSellClick(myStockEntity: MyStockEntity?) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClick(myStockEntity: MyStockEntity?) {
        myStockEntity?.let {
            myStockAdapter.setMyStockList(myStockViewModel.deleteMyStock(it))
            startSkeletonAnimation()
        }
    }

    private fun startSkeletonAnimation(){
        val skeletonScreen = Skeleton.bind(mDataBinding.recyclerviewMyStockFragment)
            .adapter(myStockAdapter)
            .shimmer(true)
            .angle(20)
            .color(R.color.color_dddddd)
            .frozen(false)
            .duration(1000)
            .count(myStockAdapter.itemCount)
            .load(R.layout.skeleton_row_layout)
            .show() //default count is 10
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            skeletonScreen.hide()
        },1200)
    }
}