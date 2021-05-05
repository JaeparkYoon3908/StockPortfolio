package com.yjpapp.stockportfolio.ui.mystock

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.Skeleton
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVVMFragment
import com.yjpapp.stockportfolio.database.room.MyStockEntity
import com.yjpapp.stockportfolio.databinding.FragmentMyStockBinding
import com.yjpapp.stockportfolio.ui.widget.MonthYearPickerDialog
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import org.koin.android.ext.android.inject
import java.text.NumberFormat
import java.util.*


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
        initLayout()
        setObserver()
//        startSkeletonAnimation()
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
                    inputDialogPurchasePrice.value = ""
                    inputDialogPurchaseCount = ""
                }
                showInputDialog(true, 0)
            }
//            R.id.menu_MyStockFragment_Edit -> {
//
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initLayout() {
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
            recyclerviewMyStockFragment.itemAnimator = FadeInAnimator()
            recyclerviewMyStockFragment.addOnItemTouchListener(object :
                RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    when (e.actionMasked) {
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_DOWN -> {
                            myStockAdapter.closeSwipeLayout()
                        }
                    }
                    return false
                }
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })

        }
    }

    private fun setObserver() {
        myStockViewModel.run {
            myStockInfoList.observe(this@MyStockFragment, Observer {
                myStockAdapter.setMyStockList(it)
                when (notifyHandler) {
                    NOTIFY_HANDLER_INSERT -> {
                        myStockAdapter.notifyItemInserted(it.size - 1)
                        myStockAdapter.notifyItemRangeInserted(it.size - 1, it.size)
                        mDataBinding.recyclerviewMyStockFragment.scrollToPosition(it.size - 1)
                    }
                    NOTIFY_HANDLER_UPDATE -> {
                        myStockAdapter.notifyDataSetChanged()
                    }
                    NOTIFY_HANDLER_DELETE -> {
                        myStockAdapter.notifyItemRemoved(deletePosition)
                        myStockAdapter.notifyItemRangeRemoved(deletePosition, it.size)
                    }
                }
                var mTotalPurchasePrice = 0.0
                it.forEach { myStockEntity ->
                    mTotalPurchasePrice += Utils.getNumDeletedComma(myStockEntity.purchasePrice).toDouble()
                }
                totalPurchasePrice.value = NumberFormat.getCurrencyInstance(Locale.KOREA).format(mTotalPurchasePrice)
            })
            showErrorToast.observe(this@MyStockFragment, Observer {
                it.getContentIfNotHandled()?.let {
                    Toasty.error(mContext, R.string.MyStockInputDialog_Error_Message, Toast.LENGTH_SHORT).show()
                }
            })
            showDBSaveErrorToast.observe(this@MyStockFragment, Observer {
                it.getContentIfNotHandled()?.let {
                    Toasty.error(mContext, R.string.MyStockInputDialog_Error_Message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showInputDialog(isInsertMode: Boolean, id: Int) {
        MyStockInputDialog.getInstance(mContext).apply {
            myStockViewModel.inputDialogController = this
            binding.apply {
                viewModel = myStockViewModel
                etSubjectName.setOnClickListener {
//                    val intent = Intent(mContext, StockSearchActivity::class.java)
//                    startActivityForResult(intent, 10000)
                }
                etPurchaseDate.setOnClickListener {
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
                            myStockViewModel.inputDialogPurchaseDate =
                                "$purchaseYear.$purchaseMonth"
                        }
                        show(this@MyStockFragment.childFragmentManager, "MonthYearPickerDialog")
                    }
                }
                txtCancel.setOnClickListener { dismiss() }
                txtComplete.setOnClickListener {
                    if (myStockViewModel.saveMyStock(isInsertMode, id)) {
                        dismiss()
                    }
                }

                //observer
                myStockViewModel.inputDialogPurchasePrice.observe(this@MyStockFragment, Observer {
                    etPurchasePrice.setText(it)
                    etPurchasePrice.setSelection(it.length)
                })

            }
            show()
        }
    }

    override fun onEditClick(myStockEntity: MyStockEntity?) {
        myStockViewModel.apply {
            if (myStockEntity == null) {
                Toasty.error(mContext, "값을 불러올 수 없습니다.")
            } else {
                inputDialogSubjectName = myStockEntity.subjectName
                inputDialogPurchaseDate = myStockEntity.purchaseDate
                inputDialogPurchasePrice.value = myStockEntity.purchasePrice
                inputDialogPurchaseCount = myStockEntity.purchaseCount
                showInputDialog(false, myStockEntity.id)
            }
        }
    }

    override fun onSellClick(myStockEntity: MyStockEntity?) {
        //TODO 수익 노트 연동.
    }

    var deletePosition = 0
    override fun onDeleteClick(myStockEntity: MyStockEntity?, position: Int) {
        deletePosition = position
        myStockEntity?.let {
            myStockViewModel.deleteMyStock(it)
//            myStockAdapter.setMyStockList(myStockViewModel.deleteMyStock(it))
//            startSkeletonAnimation()
        }
    }

    fun onRecyclerViewClicked() {

    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            return super.onDoubleTap(e)
        }
    }

    private fun startSkeletonAnimation() {
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
        }, 1200)
    }
}