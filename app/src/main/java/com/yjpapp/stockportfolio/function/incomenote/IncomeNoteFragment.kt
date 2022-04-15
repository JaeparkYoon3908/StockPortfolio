package com.yjpapp.stockportfolio.function.incomenote

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.common.dialog.CommonTwoBtnDialog
import com.yjpapp.stockportfolio.common.widget.CommonLoadingView
import com.yjpapp.stockportfolio.databinding.FragmentIncomeNoteBinding
import com.yjpapp.stockportfolio.extension.OnSingleClickListener
import com.yjpapp.stockportfolio.extension.repeatOnStarted
import com.yjpapp.stockportfolio.function.incomenote.dialog.IncomeNoteDatePickerDialog
import com.yjpapp.stockportfolio.function.incomenote.dialog.IncomeNoteInputDialog
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteListInfo
import com.yjpapp.stockportfolio.common.dialog.CommonDialogManager
import com.yjpapp.stockportfolio.common.dialog.CommonOneBtnDialog
import com.yjpapp.stockportfolio.util.StockUtils
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch
import java.math.BigDecimal

/**
 * 수익노트 화면
 * 디자인 패턴 : MVVM
 * @author Yoon Jae-park
 * @since 2020.08
 */
@Suppress("IMPLICIT_CAST_TO_ANY")
@AndroidEntryPoint
class IncomeNoteFragment: Fragment() {
    private var _binding: FragmentIncomeNoteBinding? = null
    private val binding get() = _binding!!
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
                    viewModel.runBackPressAppCloseEvent(it)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        binding.ivLoading.setLoadingImageColor(CommonLoadingView.LoadingColorType.BLACK)
        initRecyclerView()
    }

    private val onClickListener = OnSingleClickListener { view: View? ->
        when (view?.id) {
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
                    requestGetIncomeNote()
                    requestTotalGain()
                    setDateText()
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerviewIncomeNoteFragment.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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
                purchasePrice = StockUtils.getNumInsertComma(BigDecimal(it.purchasePrice).toString()),
                sellPrice = StockUtils.getNumInsertComma(BigDecimal(it.sellPrice).toString()),
                sellCount = it.sellCount.toString(),
            )
        }
        IncomeNoteInputDialog(requireContext(), inputDialogCallBack, data).run {
            if (!isShowing) {
                show()
            }
        }
    }

    private fun initData() {
        viewModel.apply {
            page = 1
            requestGetIncomeNote()
            requestTotalGain()
            setDateText()
        }
        //event handler
        lifecycleScope.launch {
            repeatOnStarted {
                viewModel.uiState.collect { event -> handleEvent(event) }
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
                    viewModel.requestDeleteIncomeNote(respIncomeNoteListInfo.id, position)
                    return
                }
                CommonTwoBtnDialog(
                    requireContext(),
                    CommonTwoBtnDialog.CommonTwoBtnData(
                        noticeText = "삭제하시겠습니까?",
                        leftBtnText = requireContext().getString(R.string.Common_Cancel),
                        rightBtnText = requireContext().getString(R.string.Common_Ok),
                        leftBtnListener = { view: View, dialog: CommonTwoBtnDialog ->
                            dialog.dismiss()
                        },
                        rightBtnListener = { view: View, dialog: CommonTwoBtnDialog ->
                            if (incomeNoteListAdapter.itemCount > position) {
                                viewModel.requestDeleteIncomeNote(
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
                    requestGetIncomeNote()
                    requestTotalGain()
                    setDateText()
                }
            }
        }
    }

    private val inputDialogCallBack = object : IncomeNoteInputDialog.CallBack {
        override fun onInputDialogCompleteClicked(reqIncomeNoteInfo: ReqIncomeNoteInfo) {
            reqIncomeNoteInfo.id = viewModel.incomeNoteId
            if (viewModel.editMode) {
                viewModel.requestModifyIncomeNote(reqIncomeNoteInfo)
            } else {
                viewModel.requestAddIncomeNote(reqIncomeNoteInfo)
            }
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (viewModel.hasNext) {
                incomeNoteListAdapter.isLoadingAnimationStart = true
                incomeNoteListAdapter.notifyDataSetChanged()
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()
                if (lastVisible >= totalItemCount - 1) {
                    viewModel.page++
                    viewModel.requestGetIncomeNote()
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
    private fun handleEvent(event: IncomeNoteViewModel.Event) {
        when (event) {
            is IncomeNoteViewModel.Event.SendTotalGainData -> {
                val totalGainNumber = event.data.total_price
                val totalGainPercent = event.data.total_percent
                binding.apply {
                    val totalRealizationGainsLossesNumber =
                        StockUtils.getNumInsertComma(BigDecimal(totalGainNumber).toString())
                    txtTotalRealizationGainsLossesData.text =
                        "${StockConfig.koreaMoneySymbol}$totalRealizationGainsLossesNumber"
                    if (totalGainPercent >= 0) {
                        txtTotalRealizationGainsLossesData.setTextColor(requireContext().getColor(R.color.color_e52b4e))
                        txtTotalRealizationGainsLossesPercent.setTextColor(
                            requireContext().getColor(
                                R.color.color_e52b4e
                            )
                        )
                    } else {
                        txtTotalRealizationGainsLossesData.setTextColor(requireContext().getColor(R.color.color_4876c7))
                        txtTotalRealizationGainsLossesPercent.setTextColor(
                            requireContext().getColor(
                                R.color.color_4876c7
                            )
                        )
                    }
                    txtTotalRealizationGainsLossesPercent.text =
                        StockUtils.getRoundsPercentNumber(totalGainPercent)
                }
            }
            is IncomeNoteViewModel.Event.IncomeNoteDeleteSuccess -> {
                val toastMsg = requireContext().getString(R.string.Common_Notice_Delete_Ok)
                Toasty.normal(requireContext(), toastMsg).show()
                incomeNoteListAdapter.incomeNoteListInfo.removeAt(event.position)
                incomeNoteListAdapter.notifyItemRemoved(event.position)
                incomeNoteListAdapter.notifyDataSetChanged()
                viewModel.requestTotalGain()
            }
            is IncomeNoteViewModel.Event.IncomeNoteAddSuccess -> {
                val toastMsg = requireContext().getString(R.string.Common_Notice_Add_Ok)
                Toasty.info(requireContext(), toastMsg).show()
                incomeNoteListAdapter.incomeNoteListInfo.add(0, event.data)
//                incomeNoteListAdapter.notifyItemInserted(incomeNoteListAdapter.itemCount - 1)
                incomeNoteListAdapter.notifyDataSetChanged()
                viewModel.requestTotalGain()
            }
            is IncomeNoteViewModel.Event.IncomeNoteModifySuccess -> {
                val toastMsg = requireContext().getString(R.string.Common_Notice_Modify_Ok)
                Toasty.normal(requireContext(), toastMsg).show()
                val beforeModifyIncomeNote =
                    incomeNoteListAdapter.incomeNoteListInfo.find { it.id == event.data.id }
                val index = incomeNoteListAdapter.incomeNoteListInfo.indexOf(beforeModifyIncomeNote)
                incomeNoteListAdapter.incomeNoteListInfo[index] = event.data
                incomeNoteListAdapter.notifyDataSetChanged()
                viewModel.requestTotalGain()
            }
            is IncomeNoteViewModel.Event.FetchUIncomeNotes -> {
                event.data.forEach {
                    incomeNoteListAdapter.incomeNoteListInfo.add(it)
                }
                incomeNoteListAdapter.isLoadingAnimationStart = false
                incomeNoteListAdapter.notifyDataSetChanged()
            }
            is IncomeNoteViewModel.Event.ResponseServerError -> {
                if (viewModel.isDialogShowing) {
                    return
                }
                val dialogFragment = CommonOneBtnDialog(
                    requireContext(),
                    CommonOneBtnDialog.CommonOneBtnData(
                        noticeText = event.msg,
                        btnText = getString(R.string.Common_Ok),
                        btnListener = { _: View, dialog: CommonOneBtnDialog ->
                            dialog.dismiss()
                            viewModel.isDialogShowing = false
                        }
                    )
                )
                dialogFragment.show(requireActivity().supportFragmentManager, TAG)
                viewModel.isDialogShowing = true
            }
            is IncomeNoteViewModel.Event.StartAndStopLoadingAnimation -> {
                if (event.isAnimationStart) {
                    binding.ivLoading.visibility = View.VISIBLE
                } else {
                    binding.ivLoading.visibility = View.GONE
                }
            }
        }
    }
}