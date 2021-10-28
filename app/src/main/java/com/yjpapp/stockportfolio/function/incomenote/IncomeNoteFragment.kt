package com.yjpapp.stockportfolio.function.incomenote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.constance.StockPortfolioConfig
import com.yjpapp.stockportfolio.databinding.FragmentIncomeNoteBinding
import com.yjpapp.stockportfolio.function.memo.MemoListFragment
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteInfo
import com.yjpapp.stockportfolio.dialog.CommonDatePickerDialog
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.math.BigDecimal
import java.util.*

/**
 * 수익노트 화면
 * 디자인 패턴 : MVP
 * @author Yoon Jae-park
 * @since 2020.08
 */
class IncomeNoteFragment : Fragment() {
    private val TAG = IncomeNoteFragment::class.java.simpleName
    private lateinit var mContext: Context
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var incomeNoteListAdapter = IncomeNoteListAdapter(arrayListOf(), null)

    private var _binding: FragmentIncomeNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: IncomeNoteViewModel by inject()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        //Fragment BackPress Event Call
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.let {
                    Utils.runBackPressAppCloseEvent(mContext, it)
                }
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
                viewModel.editMode = false
                viewModel.incomeNoteId = -1
                showInputDialog(viewModel.editMode, null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initLayout() {
        setHasOptionsMenu(true)
        incomeNoteListAdapter.callBack = adapterCallBack
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
                    datePickerDialogCallBack,
                    viewModel.initStartYYYYMMDD,
                    viewModel.initEndYYYYMMDD
                ).apply {
                    show(this@IncomeNoteFragment.childFragmentManager, TAG)
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerviewIncomeNoteFragment.apply {
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            adapter = incomeNoteListAdapter
            addOnScrollListener(onScrollListener)
            addOnItemTouchListener(object: RecyclerView.OnItemTouchListener{
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    when(e.actionMasked){
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_DOWN -> {
                            incomeNoteListAdapter.closeSwipeLayout()
                        }
                    }
                    return false
                }
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }
    }

    fun showAddButton() {
        menu?.getItem(0)?.isVisible = true
    }

    fun hideAddButton() {
        menu?.getItem(0)?.isVisible = false
    }

    fun showInputDialog(editMode: Boolean, respIncomeNoteInfo: RespIncomeNoteInfo.IncomeNoteList?) {
        IncomeNoteInputDialog(inputDialogCallBack, mContext).apply {
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
//                        Toast.makeText(requireContext(), "날짜 : $year/$month/$dayOfMonth", Toast.LENGTH_LONG).show()
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

    fun scrollPosition(position: Int) {
        binding.recyclerviewIncomeNoteFragment.scrollToPosition(position)
    }

    fun scrollTopPosition() {
        binding.recyclerviewIncomeNoteFragment.smoothScrollBy(0, 0)
    }

    private fun initData() {
        val toDayYYYYMM = Utils.getTodayYYMMDD()
        viewModel.apply {
            initStartYYYYMMDD = listOf(toDayYYYYMM[0], "01", "01")
            initEndYYYYMMDD = listOf(toDayYYYYMM[0], "12", "01")

            requestGetIncomeNote(mContext, 1)
            requestTotalGain(mContext)
        }
        subScribeUI(this@IncomeNoteFragment)
    }
    private fun subScribeUI(owner: LifecycleOwner) {
        viewModel.apply {
            totalGainIncomeNoteData.observe(owner, { data ->
                val totalGainNumber = data.total_price
                val totalGainPercent = data.total_percent
                binding.apply {
                    val totalRealizationGainsLossesNumber = Utils.getNumInsertComma(BigDecimal(totalGainNumber).toString())
                    txtTotalRealizationGainsLossesData.text = "${StockPortfolioConfig.moneySymbol}$totalRealizationGainsLossesNumber"
                    if (totalGainNumber >= 0) {
                        txtTotalRealizationGainsLossesData.setTextColor(mContext.getColor(R.color.color_e52b4e))
                        txtTotalRealizationGainsLossesPercent.setTextColor(mContext.getColor(R.color.color_e52b4e))
                    } else {
                        txtTotalRealizationGainsLossesData.setTextColor(mContext.getColor(R.color.color_4876c7))
                        txtTotalRealizationGainsLossesPercent.setTextColor(mContext.getColor(R.color.color_4876c7))
                    }
                    txtTotalRealizationGainsLossesPercent.text = "$totalGainPercent%"
                }
            })
            incomeNoteListLiveData.observe(owner, { data ->
                data.forEach {
                    incomeNoteListAdapter.incomeNoteList.add(it)
                }
                incomeNoteListAdapter.notifyDataSetChanged()
                viewModel.run {
                    val startDate = makeDateString(initStartYYYYMMDD)
                    val endDate = makeDateString(initEndYYYYMMDD)
                    binding.txtFilterDate.text = "$startDate ~ $endDate"
                }
            })
            incomeNoteModifySuccess.observe(owner, { data ->
                Toasty.normal(mContext, "수정완료").show()
                viewModel.requestTotalGain(mContext)
            })
            incomeNoteDeletedPosition.observe(owner, { data ->
                Toasty.normal(mContext, "삭제완료").show()
                incomeNoteListAdapter.incomeNoteList.removeAt(data)
                incomeNoteListAdapter.notifyItemRemoved(data)
                incomeNoteListAdapter.notifyItemRangeRemoved(data, incomeNoteListAdapter.itemCount)
            })
            incomeNoteAddSuccess.observe(owner, { data ->
                Toasty.info(mContext, "추가완료").show()
            })
        }
    }

    private val adapterCallBack = object : IncomeNoteListAdapter.CallBack {
        override fun onEditButtonClick(respIncomeNoteList: RespIncomeNoteInfo.IncomeNoteList?) {
            respIncomeNoteList?.let {
                viewModel.editMode = true
                showAddButton()
                viewModel.incomeNoteId = it.id //        val incomeNoteInfo = incomeNoteInteractor.getIncomeNoteInfo(position)
                showInputDialog(viewModel.editMode, respIncomeNoteList)
            }
        }

        override fun onDeleteOkClick(id: Int, position: Int) {
            viewModel.requestDeleteIncomeNote(mContext, id, position)
        }
    }

    private val datePickerDialogCallBack = object : IncomeNoteDatePickerDialog.CallBack {
        override fun requestIncomeNoteList(startDateList: List<String>, endDateList: List<String>) {
            lifecycleScope.launch {
                viewModel.initStartYYYYMMDD = startDateList
                viewModel.initEndYYYYMMDD = endDateList
                //TODO 리스트 클리어
                viewModel.requestGetIncomeNote(mContext, 1)
                viewModel.requestTotalGain(mContext)
            }
        }
    }
    private val inputDialogCallBack = object : IncomeNoteInputDialog.CallBack {
        override fun onInputDialogCompleteClicked(reqIncomeNoteInfo: ReqIncomeNoteInfo) {
            reqIncomeNoteInfo.id = viewModel.incomeNoteId
            if (viewModel.editMode) {
                viewModel.requestModifyIncomeNote(mContext, reqIncomeNoteInfo)
            } else {
                viewModel.requestAddIncomeNote(mContext, reqIncomeNoteInfo)
            }
        }
    }
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()
            if (lastVisible >= totalItemCount - 1) {
                viewModel.page++
                viewModel.requestGetIncomeNote(mContext, viewModel.page)
            }
        }
    }
}