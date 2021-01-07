package com.yjpapp.stockportfolio.ui.presenter

import android.content.Context
import android.widget.Toast
import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.ui.dialog.IncomeNoteInputDialog
import com.yjpapp.stockportfolio.ui.interactor.IncomeNoteInteractor
import com.yjpapp.stockportfolio.ui.view.IncomeNoteView
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.dialog_edit_income_note.*
import java.text.DecimalFormat

class IncomeNotePresenter(val mContext: Context, private val incomeNoteView: IncomeNoteView) {
    private val incomeNoteInteractor = IncomeNoteInteractor.getInstance(mContext)

    fun onAllFilterClicked(){
        incomeNoteView.showAllData(incomeNoteInteractor.getAllIncomeNoteInfoList())
    }

    fun onGainFilterClicked(){
        incomeNoteView.showGainData(incomeNoteInteractor.getGainIncomeNoteInfoList())
    }

    fun onLossFilterClicked(){
        incomeNoteView.showLossData(incomeNoteInteractor.getLossIncomeNoteInfoList())
    }

    fun onAddButtonClicked(){
        incomeNoteView.showInputDialog(false, -1, null)
    }

    fun onEditButtonClicked(position: Int){
        val id = incomeNoteInteractor.getAllIncomeNoteInfoList()[position]!!.id
        val incomeNoteInfo = IncomeNoteInteractor.getInstance(mContext).getIncomeNoteInfo(position)
        incomeNoteView.showInputDialog(true, id, incomeNoteInfo!!)
    }

    fun onDeleteButtonClicked(id: Int){
        incomeNoteInteractor.deleteIncomeNoteInfo(id)
        incomeNoteView.deleteIncomeNoteData()
    }

    fun onInputDialogCompleteClicked(incomeNoteInputDialog: IncomeNoteInputDialog, editMode: Boolean, id: Int){
        incomeNoteInputDialog.run {
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

            val dataInfo = IncomeNoteInfo(id, subjectName, realPainLossesAmount, purchaseDate,
                    sellDate, gainPercent, purchasePrice, sellPrice, sellCount)
            if(editMode){
                IncomeNoteInteractor.getInstance(mContext).updateIncomeNoteInfo(dataInfo)
                incomeNoteView.updateIncomeNoteData(IncomeNoteInteractor.getInstance(mContext).getAllIncomeNoteInfoList())
            }else{
                IncomeNoteInteractor.getInstance(mContext).insertIncomeNoteInfo(dataInfo)
                incomeNoteView.addIncomeNoteData(IncomeNoteInteractor.getInstance(mContext).getAllIncomeNoteInfoList())
            }
            dismiss()
        }
    }
    fun getAllIncomeNoteList(): ArrayList<IncomeNoteInfo?>{
        return incomeNoteInteractor.getAllIncomeNoteInfoList()
    }
}