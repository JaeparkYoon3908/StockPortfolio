package com.yjpapp.stockportfolio.function.incomenote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.localdb.sqlte.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.databinding.FragmentIncomeNoteBinding
import com.yjpapp.stockportfolio.function.memo.MemoListFragment
import com.yjpapp.stockportfolio.model.IncomeNoteModel
import com.yjpapp.stockportfolio.util.StockLog
import com.yjpapp.stockportfolio.widget.MonthYearPickerDialog
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * 수익노트 화면
 * 디자인 패턴 : MVP
 * @author Yoon Jae-park
 * @since 2020.08
 */
class IncomeNoteFragment : Fragment(), IncomeNoteView {
    private lateinit var incomeNotePresenter: IncomeNotePresenter
    private lateinit var mContext: Context

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var layoutManager: LinearLayoutManager

    private var _viewBinding: FragmentIncomeNoteBinding? = null
    private val viewBinding get() = _viewBinding!!

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

        _viewBinding = FragmentIncomeNoteBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        subScribeUI()
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
        _viewBinding = null
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
//            R.id.menu_IncomeNote_Edit -> {
//                activity?.window?.attributes?.windowAnimations = R.style.AnimationPopupStyle
//                incomeNotePresenter.onMenuEditButtonClick()
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initLayout() {
        setHasOptionsMenu(true)
        incomeNotePresenter = IncomeNotePresenter(mContext, this)

        viewBinding.apply {
            svIncomeNoteFragment.setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    incomeNotePresenter.onStartSearch(newText)
                    return false
                }
            })
            linIncomeNoteFragmentFilter.setOnClickListener(onClickListener)
            txtTotalRealizationGainsLosses.isSelected = true
            txtTotalRealizationGainsLossesData.isSelected = true
            txtTotalRealizationGainsLossesPercent.isSelected = true

//            toggle.setOnCheckedChangeListener { buttonView, isChecked ->
//                //종목명 -> 기간별
//                if(isChecked){
//                    txtDate.visibility = View.GONE
//                    svIncomeNoteFragment.visibility = View.VISIBLE
//                }
//                //기간별 -> 종목명
//                else{
//                    Log.d("YJP", "!isChecked")
//                    txtDate.visibility = View.VISIBLE
//                    svIncomeNoteFragment.visibility = View.GONE
//                }
//            }
        }

        bindTotalGainData()
        initRecyclerView()
    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when (view?.id) {
            R.id.lin_MainActivity_BottomMenu_Memo -> {
                val intent = Intent(mContext, MemoListFragment::class.java)
                startActivity(intent)
            }

//            R.id.txt_IncomeNoteFragment_Edit -> {
//                activity?.window?.attributes?.windowAnimations = R.style.AnimationPopupStyle
//                val allIncomeNoteList = incomeNotePresenter.getAllIncomeNoteList()
//                if (allIncomeNoteList.size > 0) {
//                    incomeNoteListAdapter?.setEditMode(!incomeNoteListAdapter?.isEditMode()!!)
//                    incomeNoteListAdapter?.notifyDataSetChanged()
//                    if(incomeNoteListAdapter?.isEditMode()!!){
//                        hideAddButton()
//                    }else{
//                        showAddButton()
//                    }
//                }
//
//            }
            R.id.lin_IncomeNoteFragment_Filter -> {
                showFilterDialog()
//                val intent = Intent(mContext, SMSAuthActivity::class.java)
//                startActivity(intent)
            }
        }
    }

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
//        if (allIncomeNoteList?.size != 0) {
//            layoutManager.scrollToPosition(allIncomeNoteList?.size!! - 1)
//        }
        viewBinding.apply {
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
        val allIncomeNoteList = incomeNotePresenter.getAllIncomeNoteList()
        var totalGainNumber = 0.0
        var totalGainPercent = 0.0
        val gainPercentList = ArrayList<Double>()

        if(allIncomeNoteList.size!=0){
            for (i in allIncomeNoteList.indices) {
                totalGainNumber += Utils.getNumDeletedComma(allIncomeNoteList[i]!!.realPainLossesAmount!!).toDouble()
                totalGainPercent += Utils.getNumDeletedPercent(allIncomeNoteList[i]!!.gainPercent!!).toDouble()
                gainPercentList.add(Utils.getNumDeletedPercent(allIncomeNoteList[i]!!.gainPercent!!).toDouble())
            }
            totalGainPercent = Utils.calculateTotalGainPercent(allIncomeNoteList)
        }

        viewBinding.apply {

            txtTotalRealizationGainsLossesData.text =
                    NumberFormat.getCurrencyInstance(Locale.KOREA).format(totalGainNumber)
            if(totalGainNumber >= 0){
                txtTotalRealizationGainsLossesData.setTextColor(mContext.getColor(R.color.color_e52b4e))
                txtTotalRealizationGainsLossesPercent.setTextColor(mContext.getColor(R.color.color_e52b4e))
            }else{
                txtTotalRealizationGainsLossesData.setTextColor(mContext.getColor(R.color.color_4876c7))
                txtTotalRealizationGainsLossesPercent.setTextColor(mContext.getColor(R.color.color_4876c7))
            }
            txtTotalRealizationGainsLossesPercent.text =
                    Utils.getRoundsPercentNumber(totalGainPercent)
        }

    }

    override fun changeFilterText(text: String) {
        viewBinding.txtIncomeNoteFragmentFilter.text = text
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

    override fun showInputDialog(editMode: Boolean, incomeNoteInfo: IncomeNoteModel.IncomeNoteList?) {
        IncomeNoteInputDialog(mContext, incomeNotePresenter).apply {
            if(editMode){
                if (!isShowing) {
                    show()
                    etSubjectName.setText(incomeNoteInfo?.subjectName)
//                        etSellDate.setText(incomeNoteInfo?.purchaseDate)
                    etSellDate.setText(incomeNoteInfo?.sellDate)
                    etPurchasePrice.setText(incomeNoteInfo?.purchasePrice)
                    etSellPrice.setText(incomeNoteInfo?.sellPrice)
                    etSellCount.setText(incomeNoteInfo?.sellCount.toString())
                }
            }else{
                if (!isShowing) {
                    show()
                }
            }

            etSellDate.setOnClickListener{
                var year = ""
                var month = ""
                if(etSellDate.text.toString() != ""){
                    val split = etSellDate.text.toString().split(".")
                    year = split[0]
                    month = split[1]
                }

                MonthYearPickerDialog(year, month).apply {
                    setListener { view, year, month, dayOfMonth ->
                        Toast.makeText(requireContext(), "Set date: $year/$month/$dayOfMonth", Toast.LENGTH_LONG).show()
                        uiHandler.sendEmptyMessage(IncomeNoteInputDialog.MSG.PURCHASE_DATE_DATA_INPUT)
                        purchaseYear = year.toString()
                        purchaseMonth = if(month<10){
                            "0$month"
                        }else{
                            month.toString()
                        }
                    }
                    show(this@IncomeNoteFragment.childFragmentManager, "MonthYearPickerDialog")
                }
            }
        }
    }

    override fun scrollTopPosition(topPosition: Int) {
        viewBinding.recyclerviewIncomeNoteFragment.scrollToPosition(topPosition)
    }

    override fun setAdapter(incomeNoteListAdapter: IncomeNoteListAdapter?) {
        viewBinding.recyclerviewIncomeNoteFragment.adapter = incomeNoteListAdapter
    }

    override fun showToast(toast: Toast) {
        toast.show()
    }

    private fun subScribeUI(){
        lifecycleScope.launch {
            incomeNotePresenter.getIncomeNoteList(mContext).collectLatest {
                StockLog.d("YJP", "서버 연동 데이터 : ${it}" )
                incomeNotePresenter.submitData(it)
            }
        }
    }
}