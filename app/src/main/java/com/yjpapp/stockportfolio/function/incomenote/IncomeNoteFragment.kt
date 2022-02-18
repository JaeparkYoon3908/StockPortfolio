package com.yjpapp.stockportfolio.function.incomenote

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseFragment
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.databinding.FragmentIncomeNoteBinding
import com.yjpapp.stockportfolio.common.dialog.CommonTwoBtnDialog
import com.yjpapp.stockportfolio.extension.OnSingleClickListener
import com.yjpapp.stockportfolio.extension.repeatOnStarted
import com.yjpapp.stockportfolio.function.incomenote.dialog.IncomeNoteDatePickerDialog
import com.yjpapp.stockportfolio.function.incomenote.dialog.IncomeNoteInputDialog
import com.yjpapp.stockportfolio.function.memo.MemoListFragment
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteListInfo
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import com.yjpapp.stockportfolio.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal

/**
 * 수익노트 화면
 * 디자인 패턴 : MVP
 * @author Yoon Jae-park
 * @since 2020.08
 */
@AndroidEntryPoint
class IncomeNoteFragment : BaseFragment<FragmentIncomeNoteBinding>(R.layout.fragment_income_note) {
    private val TAG = IncomeNoteFragment::class.java.simpleName
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var incomeNoteListAdapter =
        IncomeNoteListAdapter(arrayListOf(), null).apply { setHasStableIds(true) }

    private val viewModel: IncomeNoteViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //Fragment BackPress Event Call
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.let {
                    viewModel.runBackPressAppCloseEvent(mContext, it)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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
        binding.unbind()
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
                showInputDialog(null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initLayout() {
        setHasOptionsMenu(true)
        incomeNoteListAdapter.callBack = adapterCallBack
        binding.apply {
            btnDate.setOnClickListener(onClickListener)
            btnSearchAll.setOnClickListener(onClickListener)
        }

        initRecyclerView()
    }

    private val onClickListener = OnSingleClickListener { view: View? ->
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

            R.id.btn_search_all -> {
                incomeNoteListAdapter.incomeNoteListInfo = arrayListOf()
                viewModel.apply {
                    initStartYYYYMMDD = listOf()
                    initEndYYYYMMDD = listOf()
                    page = 1
                    requestGetIncomeNote(requireContext())
                    requestTotalGain(requireContext())
                    setDateText()
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerviewIncomeNoteFragment.apply {
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            adapter = incomeNoteListAdapter
            addOnScrollListener(onScrollListener)
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    when (e.actionMasked) {
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

    fun showInputDialog(respIncomeNoteInfoInfo: RespIncomeNoteListInfo.IncomeNoteInfo?) {
        var data = IncomeNoteInputDialog.IncomeNoteInputDialogData()
        respIncomeNoteInfoInfo?.let {
            data = IncomeNoteInputDialog.IncomeNoteInputDialogData(
                subjectName = it.subjectName,
                sellDate = it.sellDate,
                purchasePrice = Utils.getNumInsertComma(BigDecimal(it.purchasePrice).toString()),
                sellPrice = Utils.getNumInsertComma(BigDecimal(it.sellPrice).toString()),
                sellCount = it.sellCount.toString(),
            )
        }
        IncomeNoteInputDialog(mContext, inputDialogCallBack, data).run {
            if (!isShowing) {
                show()
            }
        }
    }

    private fun initData() {
        viewModel.apply {
            page = 1
            requestGetIncomeNote(mContext)
            requestTotalGain(mContext)
            setDateText()
        }
        //event handler
        lifecycleScope.launch {
            repeatOnStarted {
                viewModel.eventFlow.collect { event -> handleEvent(event) }
            }
        }
    }

    private val adapterCallBack = object : IncomeNoteListAdapter.CallBack {
        override fun onEditButtonClick(respIncomeNoteListInfo: RespIncomeNoteListInfo.IncomeNoteInfo?) {
            respIncomeNoteListInfo?.let {
                viewModel.editMode = true
                viewModel.incomeNoteId = it.id
                showInputDialog(it)
            }
        }

        override fun onDeleteButtonClick(
            respIncomeNoteListInfo: RespIncomeNoteListInfo.IncomeNoteInfo?,
            position: Int
        ) {
            if (respIncomeNoteListInfo != null) {
                if (viewModel.isShowDeleteCheck() == StockConfig.FALSE) {
                    viewModel.requestDeleteIncomeNote(mContext, respIncomeNoteListInfo.id, position)
                    return
                }
                CommonTwoBtnDialog(
                    mContext,
                    CommonTwoBtnDialog.CommonTwoBtnData(
                        noticeText = "삭제하시겠습니까?",
                        leftBtnText = mContext.getString(R.string.Common_Cancel),
                        rightBtnText = mContext.getString(R.string.Common_Ok),
                        leftBtnListener = { view: View, dialog: CommonTwoBtnDialog ->
                            dialog.dismiss()
                        },
                        rightBtnListener = { view: View, dialog: CommonTwoBtnDialog ->
                            if (incomeNoteListAdapter.itemCount > position) {
                                viewModel.requestDeleteIncomeNote(
                                    mContext,
                                    respIncomeNoteListInfo.id,
                                    position
                                )
                                dialog.dismiss()
                            }
                        }
                    )
                ).show()
            }
        }
    }

    private val datePickerDialogCallBack = object : IncomeNoteDatePickerDialog.CallBack {
        @SuppressLint("NotifyDataSetChanged")
        override fun requestIncomeNoteList(startDateList: List<String>, endDateList: List<String>) {
            binding.btnDate
            lifecycleScope.launch {
                incomeNoteListAdapter.incomeNoteListInfo = arrayListOf()
                incomeNoteListAdapter.notifyDataSetChanged()
                viewModel.apply {
                    initStartYYYYMMDD = startDateList
                    initEndYYYYMMDD = endDateList
                    page = 1
                    requestGetIncomeNote(mContext)
                    requestTotalGain(mContext)
                    setDateText()
                }
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
            if (viewModel.hasNext) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()
                if (lastVisible >= totalItemCount - 1) {
                    viewModel.page++
                    viewModel.requestGetIncomeNote(mContext)
                }
            }
        }
    }

    private fun setDateText() {
        viewModel.run {
            if (initEndYYYYMMDD.isNotEmpty() && initEndYYYYMMDD.isNotEmpty()) {
                val startDate = makeDateString(initStartYYYYMMDD)
                val endDate = makeDateString(initEndYYYYMMDD)
                binding.txtFilterDate.text = "$startDate ~ $endDate"
            } else {
                binding.txtFilterDate.text = getString(R.string.Common_All)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleEvent(event: IncomeNoteViewModel.Event) = when (event) {
        is IncomeNoteViewModel.Event.SendTotalGainData -> {
            val totalGainNumber = event.data.total_price
            val totalGainPercent = event.data.total_percent
            binding.let {
                val totalRealizationGainsLossesNumber =
                    Utils.getNumInsertComma(BigDecimal(totalGainNumber).toString())
                it.txtTotalRealizationGainsLossesData.text =
                    "${StockConfig.moneySymbol}$totalRealizationGainsLossesNumber"
                if (totalGainPercent >= 0) {
                    it.txtTotalRealizationGainsLossesData.setTextColor(mContext.getColor(R.color.color_e52b4e))
                    it.txtTotalRealizationGainsLossesPercent.setTextColor(mContext.getColor(R.color.color_e52b4e))
                } else {
                    it.txtTotalRealizationGainsLossesData.setTextColor(mContext.getColor(R.color.color_4876c7))
                    it.txtTotalRealizationGainsLossesPercent.setTextColor(mContext.getColor(R.color.color_4876c7))
                }
                it.txtTotalRealizationGainsLossesPercent.text =
                    Utils.getRoundsPercentNumber(totalGainPercent)
            }
        }
        is IncomeNoteViewModel.Event.IncomeNoteDeleteSuccess -> {
            val toastMsg = mContext.getString(R.string.Common_Notice_Delete_Ok)
            Toasty.normal(mContext, toastMsg).show()
            incomeNoteListAdapter.incomeNoteListInfo.removeAt(event.position)
            incomeNoteListAdapter.notifyItemRemoved(event.position)
            incomeNoteListAdapter.notifyDataSetChanged()
            viewModel.requestTotalGain(mContext)
        }
        is IncomeNoteViewModel.Event.IncomeNoteAddSuccess -> {
            val toastMsg = mContext.getString(R.string.Common_Notice_Add_Ok)
            Toasty.info(mContext, toastMsg).show()
            incomeNoteListAdapter.incomeNoteListInfo.add(0, event.data)
//                incomeNoteListAdapter.notifyItemInserted(incomeNoteListAdapter.itemCount - 1)
            incomeNoteListAdapter.notifyDataSetChanged()
            viewModel.requestTotalGain(mContext)
        }
        is IncomeNoteViewModel.Event.IncomeNoteModifySuccess -> {
            val toastMsg = mContext.getString(R.string.Common_Notice_Modify_Ok)
            Toasty.normal(mContext, toastMsg).show()
            viewModel.requestTotalGain(mContext)
            if (event.data.id != -1) {
                val beforeModifyIncomeNote =
                    incomeNoteListAdapter.incomeNoteListInfo.find { it.id == event.data.id }
                val index = incomeNoteListAdapter.incomeNoteListInfo.indexOf(beforeModifyIncomeNote)
                incomeNoteListAdapter.incomeNoteListInfo[index] = event.data
                incomeNoteListAdapter.notifyDataSetChanged()
                viewModel.requestTotalGain(mContext)
            } else {
                ResponseAlertManger.showErrorAlert(
                    mContext,
                    getString(R.string.Error_Msg_Normal)
                )
            }
        }
        is IncomeNoteViewModel.Event.FetchUIncomeNotes -> {
            event.data.forEach {
                incomeNoteListAdapter.incomeNoteListInfo.add(it)
            }
            incomeNoteListAdapter.notifyDataSetChanged()
        }
    }
}