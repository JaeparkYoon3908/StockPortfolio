package com.yjpapp.stockportfolio.function.incomenote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.constance.StockPortfolioConfig
import com.yjpapp.stockportfolio.databinding.FragmentIncomeNoteBinding
import com.yjpapp.stockportfolio.function.memo.MemoListFragment
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteInfo
import com.yjpapp.stockportfolio.dialog.CommonDatePickerDialog
import com.yjpapp.stockportfolio.util.Utils
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

/**
 * 수익노트 화면
 * 디자인 패턴 : MVP
 * @author Yoon Jae-park
 * @since 2020.08
 */
class IncomeNoteFragment : Fragment(), IncomeNoteView {
    private val TAG = IncomeNoteFragment::class.java.simpleName
    private lateinit var incomeNotePresenter: IncomeNotePresenter
    private lateinit var mContext: Context
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var layoutManager: LinearLayoutManager

    private var _binding: FragmentIncomeNoteBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        //Fragment BackPress Event Call
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                incomeNotePresenter.onBackPressedClick(activity!!)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_income_note, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initData()
    }

    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_income_note, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_IncomeNoteFragment_Add -> {
                incomeNotePresenter.onAddButtonClicked()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initLayout() {
        setHasOptionsMenu(true)
        incomeNotePresenter = IncomeNotePresenter(mContext, this)

        binding.apply {
            btnDate.setOnClickListener(onClickListener)
        }

        initRecyclerView()
    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when (view?.id) {
            R.id.lin_MainActivity_BottomMenu_Memo -> {
                val intent = Intent(mContext, MemoListFragment::class.java)
                startActivity(intent)
            }

            R.id.btn_date -> {
                IncomeNoteDatePickerDialog(
                    incomeNotePresenter,
                    incomeNotePresenter.initStartYYYYMMDD,
                    incomeNotePresenter.initEndYYYYMMDD
                ).apply {
                    show(this@IncomeNoteFragment.childFragmentManager, TAG)
                }
            }
        }
    }

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
//        layoutManager.reverseLayout = true
//        layoutManager.stackFromEnd = true

        binding.apply {
            recyclerviewIncomeNoteFragment.layoutManager = layoutManager
            recyclerviewIncomeNoteFragment.itemAnimator = FadeInAnimator()
            recyclerviewIncomeNoteFragment.addOnItemTouchListener(object: RecyclerView.OnItemTouchListener{
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    when(e.actionMasked){
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_DOWN -> {
                            incomeNotePresenter.closeSwipeLayout()
                        }
                    }
                    return false
                }
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }
    }

    override fun bindTotalGainData() {
        var totalGainNumber = 0.0
        var totalGainPercent = ""
        val allIncomeNoteInfoList = incomeNotePresenter.getAllIncomeNoteList()
        if (allIncomeNoteInfoList.isNotEmpty()) {
            allIncomeNoteInfoList[0]?.let {
                totalGainNumber = it.totalPrice
                totalGainPercent = it.totalPercent
            }
        }

        binding.apply {

            txtTotalRealizationGainsLossesData.text = StockPortfolioConfig.moneySymbol + BigDecimal(totalGainNumber)
            if(totalGainNumber >= 0){
                txtTotalRealizationGainsLossesData.setTextColor(mContext.getColor(R.color.color_e52b4e))
                txtTotalRealizationGainsLossesPercent.setTextColor(mContext.getColor(R.color.color_e52b4e))
            }else{
                txtTotalRealizationGainsLossesData.setTextColor(mContext.getColor(R.color.color_4876c7))
                txtTotalRealizationGainsLossesPercent.setTextColor(mContext.getColor(R.color.color_4876c7))
            }
            //퍼센티지 붙이기
//            txtTotalRealizationGainsLossesPercent.text =
//                    Utils.getRoundsPercentNumber(totalGainPercent)
            txtTotalRealizationGainsLossesPercent.text = totalGainPercent
        }

    }

    override fun showAddButton() {
        menu?.getItem(0)?.isVisible = true
    }

    override fun hideAddButton() {
        menu?.getItem(0)?.isVisible = false
    }

    override fun showFilterDialog() {
        val mainFilterDialog = IncomeNoteFilterDialog(incomeNotePresenter)
        mainFilterDialog.show(childFragmentManager, tag)
    }

    override fun showInputDialog(editMode: Boolean, respIncomeNoteInfo: RespIncomeNoteInfo.IncomeNoteList?) {
        IncomeNoteInputDialog(mContext, incomeNotePresenter).apply {
            if(editMode){
                if (!isShowing) {
                    show()
                    respIncomeNoteInfo?.let {
                        etSubjectName.setText(it.subjectName)
                        etSellDate.setText(it.sellDate)
                        etPurchasePrice.setText(Utils.getNumInsertComma(BigDecimal(it.purchasePrice).toString()))
                        etSellPrice.setText(Utils.getNumInsertComma(BigDecimal(it.sellPrice).toString()))
                        etSellCount.setText(it.sellCount.toString())
                    }
                }
            }else{
                if (!isShowing) {
                    show()
                }
            }

            etSellDate.setOnClickListener {
                var year = ""
                var month = ""
                var day = ""
                if(etSellDate.text.toString() != "") {
                    val split = etSellDate.text.toString().split("-")
                    year = split[0]
                    month = split[1]
                    day = split[2]
                }

                CommonDatePickerDialog(year, month, day).apply {
                    setListener { view, year, month, dayOfMonth ->
                        Toast.makeText(requireContext(), "Set date: $year/$month/$dayOfMonth", Toast.LENGTH_LONG).show()
                        uiHandler.sendEmptyMessage(IncomeNoteInputDialog.MSG.PURCHASE_DATE_DATA_INPUT)
                        purchaseYear = year.toString()
                        purchaseMonth = if(month<10){
                            "0$month"
                        }else{
                            month.toString()
                        }
                        purchaseDay = dayOfMonth.toString()
                    }
                    show(this@IncomeNoteFragment.childFragmentManager, "MonthYearPickerDialog")
                }
            }
        }
    }

    override fun scrollPosition(position: Int) {
        binding.recyclerviewIncomeNoteFragment.scrollToPosition(position)
    }

    override fun scrollTopPosition() {
        binding.recyclerviewIncomeNoteFragment.smoothScrollBy(0, 0)
    }

    override fun setAdapter(incomeNoteListAdapter: IncomeNoteListAdapter?) {
        binding.recyclerviewIncomeNoteFragment.adapter = incomeNoteListAdapter
    }

    override fun showToast(toast: Toast) {
        toast.show()
    }

    override fun initFilterDateText(startDate: String, endDate: String) {
        binding.txtFilterDate.text = "$startDate ~ $endDate"
    }

    private fun initData() {
        val toDayYYYYMM = Utils.getTodayYYMMDD()
        val startDate = "${toDayYYYYMM[0]}-01-01"
        val endDate = "${toDayYYYYMM[0]}-12-01"
        lifecycleScope.launch {
            incomeNotePresenter.requestIncomeNoteList(mContext, startDate, endDate)
        }
    }
}