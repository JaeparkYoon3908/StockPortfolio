package com.yjpapp.stockportfolio.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.databinding.FragmentIncomeNoteBinding
import com.yjpapp.stockportfolio.ui.adapter.IncomeNoteListAdapter
import com.yjpapp.stockportfolio.ui.dialog.IncomeNoteFilterDialog
import com.yjpapp.stockportfolio.ui.dialog.IncomeNoteInputDialog
import com.yjpapp.stockportfolio.ui.presenter.IncomeNotePresenter
import com.yjpapp.stockportfolio.ui.view.IncomeNoteView
import com.yjpapp.stockportfolio.util.Utils
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.dialog_input_income_note.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * 수익노트 화면
 *
 * @author Yun Jae-park
 * @since 2020.08
 */
class IncomeNoteFragment : Fragment(),IncomeNoteView {
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

//        mRootView = inflater.inflate(R.layout.fragment_income_note, container, false)
        _viewBinding = FragmentIncomeNoteBinding.inflate(inflater, container, false)

        return viewBinding.root
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
            R.id.menu_IncomeNote_Edit -> {
//                activity?.window?.attributes?.windowAnimations = R.style.AnimationPopupStyle
                incomeNotePresenter.onMenuEditButtonClick()
            }
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
                    startSearch(newText)
                    return false
                }
            })
            linIncomeNoteFragmentFilter.setOnClickListener(onClickListener)
            txtTotalRealizationGainsLosses.isSelected = true
            txtTotalRealizationGainsLossesData.isSelected = true
            txtTotalRealizationGainsLossesPercent.isSelected = true
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
        viewBinding.recyclerviewIncomeNoteFragment.layoutManager = layoutManager

        viewBinding.recyclerviewIncomeNoteFragment.itemAnimator = FadeInAnimator()

    }

    //TODO 검색기능 추가.
    private fun startSearch(newText: String?) {
        incomeNotePresenter.onStartSearch(newText)
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

    override fun showInputDialog(editMode: Boolean, incomeNoteInfo: IncomeNoteInfo?) {
        IncomeNoteInputDialog(mContext, incomeNotePresenter).apply {
            if(editMode){
                if (!isShowing) {
                    show()
                    et_subject_name.setText(incomeNoteInfo?.subjectName)
                    et_purchase_date.setText(incomeNoteInfo?.purchaseDate)
                    et_sell_date.setText(incomeNoteInfo?.sellDate)
                    et_purchase_price.setText(incomeNoteInfo?.purchasePrice)
                    et_sell_price.setText(incomeNoteInfo?.sellPrice)
                    et_sell_count.setText(incomeNoteInfo?.sellCount.toString())
                }
            }else{
                if (!isShowing) {
                    show()
                }
            }
        }
    }

    override fun scrollTopPosition(topPosition: Int) {
        viewBinding.recyclerviewIncomeNoteFragment.scrollToPosition(topPosition)
    }

    override fun setAdapter(incomeNoteListAdapter: IncomeNoteListAdapter) {
        viewBinding.recyclerviewIncomeNoteFragment.adapter = incomeNoteListAdapter
    }
}