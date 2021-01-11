package com.yjpapp.stockportfolio.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.ui.MainActivity
import com.yjpapp.stockportfolio.ui.adapter.IncomeNoteListAdapter
import com.yjpapp.stockportfolio.ui.dialog.IncomeNoteFilterDialog
import com.yjpapp.stockportfolio.ui.dialog.IncomeNoteInputDialog
import com.yjpapp.stockportfolio.ui.presenter.IncomeNotePresenter
import com.yjpapp.stockportfolio.ui.view.IncomeNoteView
import com.yjpapp.stockportfolio.util.ChoSungSearchQueryUtil
import com.yjpapp.stockportfolio.util.Utils
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.dialog_edit_income_note.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class IncomeNoteFragment : Fragment(),IncomeNoteView {
    private lateinit var incomeNotePresenter: IncomeNotePresenter
    private lateinit var mContext: Context
    private lateinit var mRootView: View
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    //layout 변수
    private lateinit var textView_TotalRealizationGainsLossesData: TextView
    private lateinit var textView_TotalRealizationGainsLossesPercent: TextView
    private lateinit var linear_Filter: LinearLayout
    private lateinit var textView_Filter: TextView
    private lateinit var textView_Edit: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var searchView: SearchView

//    private var incomeNoteListAdapter: IncomeNoteListAdapter? = null

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
        mRootView = inflater.inflate(R.layout.fragment_income_note, container, false)
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLayout()
    }

    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
        incomeNotePresenter.onResume()
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
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
            R.id.menu_IncomeNote_Edit -> {
//                activity?.window?.attributes?.windowAnimations = R.style.AnimationPopupStyle
                incomeNotePresenter.onMenuEditButtonClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun initData() {
////        allIncomeNoteList = (activity as MainActivity).databaseController.getAllIncomeNoteList()
//        incomeNotePresenter = IncomeNotePresenter(mContext, this)
//    }

    private fun initLayout() {
        setHasOptionsMenu(true)
        incomeNotePresenter = IncomeNotePresenter(mContext, this)
        textView_TotalRealizationGainsLossesData =
            mRootView.findViewById(R.id.txt_TotalRealizationGainsLossesData)
        textView_TotalRealizationGainsLossesPercent =
            mRootView.findViewById(R.id.txt_TotalRealizationGainsLossesPercent)
        linear_Filter = mRootView.findViewById(R.id.lin_IncomeNoteFragment_Filter)
        textView_Filter = mRootView.findViewById(R.id.txt_IncomeNoteFragment_Filter)
//        textView_Edit = mRootView.findViewById(R.id.txt_IncomeNoteFragment_Edit)
        recyclerView = mRootView.findViewById(R.id.recyclerview_IncomeNoteFragment)
        searchView = mRootView.findViewById(R.id.sv_IncomeNoteFragment)
        searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                startSearch(newText)
                return false
            }
        })

        linear_Filter.setOnClickListener(onClickListener)
//        textView_Edit.setOnClickListener(onClickListener)

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
        recyclerView.layoutManager = layoutManager

        recyclerView.itemAnimator = FadeInAnimator()

    }

    //TODO 검색기능 추가.
    private fun startSearch(newText: String?) {
        (activity as MainActivity).logcat(newText!!) //로그캣 오는거 확인 ㄱ=>ㄱㄴ=>ㄱㄴㄷ....
        (activity as MainActivity).logcat(ChoSungSearchQueryUtil.makeQuery(newText))
    }

    override fun bindTotalGainData() {
        val allIncomeNoteList = incomeNotePresenter.getAllIncomeNoteList()
        var totalGainNumber = 0.0
        var totalGainPercent = 0.0
        val gainPercentList = ArrayList<Double>()

        for (i in allIncomeNoteList.indices) {
            totalGainNumber += Utils.getNumDeletedComma(allIncomeNoteList[i]!!.realPainLossesAmount!!)
                    .toDouble()
            totalGainPercent += Utils.getNumDeletedPercent(allIncomeNoteList[i]!!.gainPercent!!)
                    .toDouble()
            gainPercentList.add(Utils.getNumDeletedPercent(allIncomeNoteList[i]!!.gainPercent!!).toDouble())
        }
        totalGainPercent = Utils.calculateTotalGainPercent(gainPercentList)
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

    override fun changeFilterText(text: String) {
        textView_Filter.text = text
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

    override fun showInputDialog(editMode: Boolean, incomeNoteInfo: IncomeNoteInfo?) {
        if(editMode){
            IncomeNoteInputDialog(mContext, incomeNotePresenter).apply {
                if (!isShowing) {
                    show()
                    et_subject_name.setText(incomeNoteInfo?.subjectName)
                    et_purchase_date.setText(incomeNoteInfo?.purchaseDate)
                    et_sell_date.setText(incomeNoteInfo?.sellDate)
                    et_purchase_price.setText(incomeNoteInfo?.purchasePrice)
                    et_sell_price.setText(incomeNoteInfo?.sellPrice)
                    et_sell_count.setText(incomeNoteInfo?.sellCount.toString())
                }
            }
        }else{
            IncomeNoteInputDialog(mContext, incomeNotePresenter).apply {
                if (!isShowing) {
                    show()
                }
            }
        }
    }

    override fun scrollTopPosition(topPosition: Int) {
        recyclerView.scrollToPosition(topPosition)
    }

    override fun setAdapter(incomeNoteListAdapter: IncomeNoteListAdapter) {
        recyclerView.adapter = incomeNoteListAdapter
    }

}