package com.yjpapp.stockportfolio.ui.income_note

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.database.model.IncomeNoteInfo
import com.yjpapp.stockportfolio.ui.MainActivity
import com.yjpapp.stockportfolio.ui.dialog.EditIncomeNoteDialog
import com.yjpapp.stockportfolio.ui.dialog.IncomeNoteFilterDialog
import com.yjpapp.stockportfolio.ui.memo.MemoListFragment
import com.yjpapp.stockportfolio.util.ChoSungSearchQueryUtil
import com.yjpapp.stockportfolio.util.Utils
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.dialog_edit_income_note.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class IncomeNoteFragment : Fragment(),
    IncomeNoteListAdapter.MainActivityCallBack, IncomeNoteFilterDialog.MainFilterClicked {
    private lateinit var mContext: Context
    private lateinit var mRootView: View
    private lateinit var callback: OnBackPressedCallback

    //layout 변수
    private lateinit var textView_TotalRealizationGainsLossesData: TextView
    private lateinit var textView_TotalRealizationGainsLossesPercent: TextView
    private lateinit var linear_MainActivity_Filter: LinearLayout
    private lateinit var textView_MainActivity_Filter: TextView
    private lateinit var textView_MainActivity_Edit: TextView
    private lateinit var recyclerView_MainActivity: RecyclerView
    private lateinit var searchView_IncomeNoteFragment: SearchView

    private var incomeNoteListAdapter: IncomeNoteListAdapter? = null
    private var allIncomeNoteList: ArrayList<IncomeNoteInfo?>? = null
    private var insertMode: Boolean = false
    private var editSelectPosition = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        //Fragment BackPress Event Call
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (incomeNoteListAdapter?.isEditMode()!!) {
                    incomeNoteListAdapter?.setEditMode(false)
                    incomeNoteListAdapter?.notifyDataSetChanged()
                    runAddButtonControl()
                } else {
                    Utils.runBackPressAppCloseEvent(mContext, activity as Activity)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mRootView = inflater.inflate(R.layout.fragment_income_note, container, false)
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initLayout()
    }

    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun addClicked() {
        EditIncomeNoteDialog(mContext).apply {
            if (!isShowing) {
                show()
                txt_complete.setOnClickListener {
                    runDialogCompleteClick(this)
                }
                txt_cancel.setOnClickListener {
                    dismiss()
                }
            }
        }
    }

    override fun onEditClicked(position: Int) {
        insertMode = false
        incomeNoteListAdapter?.setEditMode(false)
        incomeNoteListAdapter?.notifyDataSetChanged()
        editSelectPosition = position
        runAddButtonControl()
        EditIncomeNoteDialog(mContext).apply {
            if (!isShowing) {
                show()
                et_subject_name.setText(allIncomeNoteList!![position]?.subjectName)
                et_purchase_date.setText(allIncomeNoteList!![position]?.purchaseDate)
                et_sell_date.setText(allIncomeNoteList!![position]?.sellDate)
                et_purchase_price.setText(allIncomeNoteList!![position]?.purchasePrice)
                et_sell_price.setText(allIncomeNoteList!![position]?.sellPrice)
                et_sell_count.setText(allIncomeNoteList!![position]?.sellCount.toString())
                txt_complete.setOnClickListener {
                    runDialogCompleteClick(this)
                }
                txt_cancel.setOnClickListener {
                    runDialogCancelClick(this)
                }
            }
        }
    }

    override fun onDeleteClicked(position: Int) {
        var dataList = incomeNoteListAdapter?.getDataInfoList()!!
        val id: Int = dataList[position]?.id!!
        (activity as MainActivity).databaseController.deleteData(id, Databases.TABLE_INCOME_NOTE)

        dataList = (activity as MainActivity).databaseController.getAllIncomeNoteList()
        incomeNoteListAdapter?.setDataInfoList(dataList)
//        incomeNoteListAdapter?.setEditMode(false)
        incomeNoteListAdapter?.notifyItemRemoved(position)
        incomeNoteListAdapter?.notifyItemRangeRemoved(position, incomeNoteListAdapter?.itemCount!!)
        runAddButtonControl()
        bindTotalGainData()
    }

    override fun onItemLongClicked() {
        runAddButtonControl()
    }

    override fun allSelect() {
        allDataFiltering()
    }

    override fun gainSelect() {
        gainDataFiltering()
    }

    override fun lossSelect() {
        lossDataFiltering()
    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_income_note, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_IncomeNoteActivity_Add -> {
                insertMode = true
                addClicked()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initData() {
        allIncomeNoteList = (activity as MainActivity).databaseController.getAllIncomeNoteList()
    }


    private fun initLayout() {
        setHasOptionsMenu(true)

        textView_TotalRealizationGainsLossesData =
            mRootView.findViewById(R.id.txt_TotalRealizationGainsLossesData)
        textView_TotalRealizationGainsLossesPercent =
            mRootView.findViewById(R.id.txt_TotalRealizationGainsLossesPercent)
        linear_MainActivity_Filter = mRootView.findViewById(R.id.lin_IncomeNoteFragment_Filter)
        textView_MainActivity_Filter = mRootView.findViewById(R.id.txt_IncomeNoteFragment_Filter)
        textView_MainActivity_Edit = mRootView.findViewById(R.id.txt_IncomeNoteFragment_Edit)
        recyclerView_MainActivity = mRootView.findViewById(R.id.recyclerview_IncomeNoteFragment)
        searchView_IncomeNoteFragment = mRootView.findViewById(R.id.sv_IncomeNoteFragment)
        searchView_IncomeNoteFragment.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                startSearch(newText)
                return false
            }

        })

        linear_MainActivity_Filter.setOnClickListener(onClickListener)
        textView_MainActivity_Edit.setOnClickListener(onClickListener)

        textView_TotalRealizationGainsLossesData.isSelected = true
        textView_TotalRealizationGainsLossesPercent.isSelected = true

        bindTotalGainData()
        initRecyclerView()
    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when (view?.id) {
            R.id.lin_MainActivity_BottomMenu_Memo -> {
                val intent = Intent(mContext, MemoListFragment::class.java)
                startActivity(intent)
            }

            R.id.txt_IncomeNoteFragment_Edit -> {
                activity?.window?.attributes?.windowAnimations = R.style.AnimationPopupStyle
                if (allIncomeNoteList?.size!! > 0) {
                    incomeNoteListAdapter?.setEditMode(!incomeNoteListAdapter?.isEditMode()!!)
                    incomeNoteListAdapter?.notifyDataSetChanged()
                    runAddButtonControl()
                }

            }
            R.id.lin_IncomeNoteFragment_Filter -> {
                val mainFilterDialog = IncomeNoteFilterDialog(this)
                mainFilterDialog.show(childFragmentManager, tag)
            }
        }

    }

    private fun bindTotalGainData() {
        allIncomeNoteList = (activity as MainActivity).databaseController.getAllIncomeNoteList()
        var totalGainNumber: Double = 0.0
        var totalGainPercent: Double = 0.0
        for (i in allIncomeNoteList?.indices!!) {
            totalGainNumber += Utils.getNumDeletedComma(allIncomeNoteList!![i]!!.realPainLossesAmount!!)
                .toDouble()
            totalGainPercent += Utils.getNumDeletedPercent(allIncomeNoteList!![i]!!.gainPercent!!)
                .toDouble()
        }
        totalGainPercent /= allIncomeNoteList!!.size
        textView_TotalRealizationGainsLossesData.text =
            NumberFormat.getCurrencyInstance(Locale.KOREA).format(totalGainNumber)
        if (totalGainNumber >= 0) {
            textView_TotalRealizationGainsLossesData.setTextColor(mContext.getColor(R.color.color_e52b4e))
            textView_TotalRealizationGainsLossesPercent.setTextColor(mContext.getColor(R.color.color_e52b4e))
        } else {
            textView_TotalRealizationGainsLossesData.setTextColor(mContext.getColor(R.color.color_4876c7))
            textView_TotalRealizationGainsLossesPercent.setTextColor(mContext.getColor(R.color.color_4876c7))
        }
        textView_TotalRealizationGainsLossesPercent.text =
            Utils.getRoundsPercentNumber(totalGainPercent)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
//        if (allIncomeNoteList?.size != 0) {
//            layoutManager.scrollToPosition(allIncomeNoteList?.size!! - 1)
//        }
        recyclerView_MainActivity.layoutManager = layoutManager

        incomeNoteListAdapter = IncomeNoteListAdapter(allIncomeNoteList, this)
        recyclerView_MainActivity.adapter = incomeNoteListAdapter
        recyclerView_MainActivity.itemAnimator = FadeInAnimator()

    }

    private fun runDialogCancelClick(editIncomeNoteDialog: EditIncomeNoteDialog) {
        editIncomeNoteDialog.run {
            dismiss()
        }
    }

    private fun runDialogCompleteClick(editIncomeNoteDialog: EditIncomeNoteDialog) {

        editIncomeNoteDialog.run {
            //예외처리 (값을 모두 입력하지 않았을 때)
            if (et_subject_name.text.isEmpty() ||
                et_purchase_date.text.isEmpty() ||
                et_sell_date.text.isEmpty() ||
                et_purchase_price.text.isEmpty() ||
                et_sell_price.text.isEmpty() ||
                et_sell_count.text.isEmpty()
            ) {

                Toast.makeText(mContext, "값을 모두 입력해야합니다.", Toast.LENGTH_LONG).show()
                return
            }

            //매매한 회사이름
            val subjectName = et_subject_name.text.toString()
            //매수일
            val purchaseDate = et_purchase_date.text.toString()
            //매도일
            val sellDate = et_sell_date.text.toString()
            //매수금액
            val purchasePrice = et_purchase_price.text.toString()
            val purchasePriceNumber = Utils.getNumDeletedComma(purchasePrice)
            //매도금액
            val sellPrice = et_sell_price.text.toString()
            val sellPriceNumber = Utils.getNumDeletedComma(sellPrice)
            //매도수량
            val sellCount = et_sell_count.text.toString().toInt()
            //수익
            val realPainLossesAmountNumber =
                ((sellPriceNumber.toDouble() - purchasePriceNumber.toDouble()) * sellCount)
            val realPainLossesAmount = DecimalFormat("###,###").format(realPainLossesAmountNumber)
            //수익률
            val gainPercentNumber = Utils.calculateGainPercent(purchasePrice, sellPrice)
            val gainPercent = Utils.getRoundsPercentNumber(gainPercentNumber)

            //날짜오류 예외처리
            if (Utils.getNumDeletedDot(purchaseDate).toInt() > Utils.getNumDeletedDot(sellDate)
                    .toInt()
            ) {
                Toast.makeText(mContext, "매도한 날짜가 매수한 날짜보다 앞서있습니다.", Toast.LENGTH_LONG)
                    .show()
                return
            }

            val dataInfo = IncomeNoteInfo(0, subjectName, realPainLossesAmount, purchaseDate,
                sellDate, gainPercent, purchasePrice, sellPrice, sellCount)

            if (insertMode) {
                (activity as MainActivity).databaseController.insertIncomeNoteData(dataInfo)
            } else {
                dataInfo.id = allIncomeNoteList!![editSelectPosition]!!.id
                (activity as MainActivity).databaseController.updateIncomeNoteData(dataInfo)
            }
            dismiss()
        }
        val newDataInfo = (activity as MainActivity).databaseController.getAllIncomeNoteList()
        incomeNoteListAdapter?.setDataInfoList(newDataInfo)
        if (insertMode) {
            incomeNoteListAdapter?.notifyItemInserted(incomeNoteListAdapter?.itemCount!! - 1)
        } else {
            incomeNoteListAdapter?.notifyDataSetChanged()
        }
        recyclerView_MainActivity.scrollToPosition(newDataInfo.size - 1)
        bindTotalGainData()
    }

    private fun allDataFiltering() {
        allIncomeNoteList = (activity as MainActivity).databaseController.getAllIncomeNoteList()
        incomeNoteListAdapter?.setDataInfoList(allIncomeNoteList!!)
        incomeNoteListAdapter?.notifyDataSetChanged()
        textView_MainActivity_Filter.text = getString(R.string.IncomeNoteFilterDialog_All)
    }

    private fun gainDataFiltering() {
        val gainDataList = (activity as MainActivity).databaseController.getGainIncomeNoteList()
        incomeNoteListAdapter?.setDataInfoList(gainDataList!!)
        incomeNoteListAdapter?.notifyDataSetChanged()
        textView_MainActivity_Filter.text = getString(R.string.IncomeNoteFilterDialog_Gain)
    }

    private fun lossDataFiltering() {
        val lossDataList = (activity as MainActivity).databaseController.getLossIncomeNoteList()
        incomeNoteListAdapter?.setDataInfoList(lossDataList!!)
        incomeNoteListAdapter?.notifyDataSetChanged()
        textView_MainActivity_Filter.text = getString(R.string.IncomeNoteFilterDialog_Loss)
    }

    private fun runAddButtonControl() {
        menu?.getItem(menu?.size()!! - 1)?.isVisible = !incomeNoteListAdapter?.isEditMode()!!
    }

    //TODO 검색기능 추가.
    private fun startSearch(newText: String?) {
        (activity as MainActivity).logcat(newText!!) //로그캣 오는거 확인 ㄱ=>ㄱㄴ=>ㄱㄴㄷ....
        (activity as MainActivity).logcat(ChoSungSearchQueryUtil.makeQuery(newText))
    }
}