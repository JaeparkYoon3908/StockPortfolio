package com.yjpapp.stockportfolio.function.mystock

import android.os.Bundle
import android.view.*
import androidx.compose.material.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yjpapp.data.localdb.room.mystock.MyStockEntity
import com.yjpapp.data.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.dialog.CommonOneBtnDialog
import com.yjpapp.stockportfolio.extension.repeatOnStarted
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockPurchaseInputDialog
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockPurchaseInputDialogData
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockSellInputDialog
import com.yjpapp.stockportfolio.util.StockUtils
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

/**
 * 1.5 신규버전 업데이트 기능 (나의 주식)
 * 디자인 패턴 : MVVM
 * @author Yoon Jae-park
 * @since 2022.02
 */
@ExperimentalMaterialApi
@AndroidEntryPoint
class MyStockFragment : Fragment() {
    private val TAG = MyStockFragment::class.java.simpleName
    private val myStockViewModel: MyStockViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setHasOptionsMenu(true)
            setContent {
                MyStockScreen(
                    viewModel = myStockViewModel,
                )
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            repeatOnStarted {
                myStockViewModel.uiState.collect { event -> handleEvent(event) }
            }
        }
        navController = Navigation.findNavController(view)
    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_my_stock, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_MyStockFragment_Refresh -> {
                if (myStockViewModel.myStockInfoList.isEmpty()) {
                    Toasty.warning(
                        requireContext(),
                        getString(R.string.MyStockFragment_Msg_Current_Price_Refresh_Warning)
                    ).show()
                    return false
                }
                if (!myStockViewModel.isCurrentPriceRefreshing) {
                    myStockViewModel.refreshAllPrices()
                    myStockViewModel.isCurrentPriceRefreshing = true
                }
            }
            R.id.menu_MyStockFragment_Add -> {
                if (myStockViewModel.myStockInfoList.size >= 10) {
                    Toasty.info(
                        requireContext(),
                        getString(R.string.MyStockFragment_Notice_Max_List),
                        Toasty.LENGTH_LONG
                    ).show()
                    return false
                }
                showPurchaseInputDialog(null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleEvent(event: MyStockViewModel.Event) {
        when (event) {
            is MyStockViewModel.Event.ShowInfoToastMessage -> {
                Toasty.info(requireContext(), event.msg, Toasty.LENGTH_LONG).show()
            }
            is MyStockViewModel.Event.ShowErrorToastMessage -> {
                Toasty.error(requireContext(), event.msg, Toasty.LENGTH_LONG).show()
            }
            is MyStockViewModel.Event.SuccessIncomeNoteAdd -> {
                lifecycleScope.launch {
                    myStockViewModel.deleteMyStock(
                        myStockEntity = event.data
                    )
                }
            }
            is MyStockViewModel.Event.RefreshCurrentPriceDone -> {
                if (event.isSuccess)
                    Toasty.success(requireContext(), getString(R.string.MyStockFragment_Msg_Current_Price_Refresh_Success)).show()
                else
                    Toasty.error(requireContext(), getString(R.string.MyStockFragment_Msg_Current_Price_Refresh_Error)).show()
                myStockViewModel.isCurrentPriceRefreshing = false
            }
            is MyStockViewModel.Event.ResponseServerError -> {
                showOneBtnDialog(event.msg)
            }
            else -> {}
        }
    }

    private fun showPurchaseInputDialog(
        dialogData: MyStockPurchaseInputDialogData?
    ) {
        MyStockPurchaseInputDialog(
            mContext = requireContext(),
            dialogData = dialogData,
            callBack = object : MyStockPurchaseInputDialog.CallBack {
                override fun onInputDialogCompleteClicked(
                    dialog: MyStockPurchaseInputDialog,
                    userInputDialogData: MyStockPurchaseInputDialogData
                ) {
                    lifecycleScope.launch {
                        val currentPriceData = myStockViewModel.getCurrentPrice(userInputDialogData.subjectName.code)
                        currentPriceData.run {
                            if (currentPrice.isEmpty() || yesterdayPrice.isEmpty()) {
                                showOneBtnDialog(getString(R.string.Error_Msg_Network_Connect_Exception))
                                return@launch
                            }
                        }
                        val currentPrice = currentPriceData.currentPrice
                        val currentPriceNumber = StockUtils.getNumDeletedComma(currentPrice).toInt()
                        val purchasePriceNumber = StockUtils.getNumDeletedComma(userInputDialogData.purchasePrice).toInt()
                        val purchaseCountNumber = userInputDialogData.purchaseCount.toInt()
                        val gainPrice = (currentPriceNumber - purchasePriceNumber) * purchaseCountNumber
                        val myStockEntity = MyStockEntity(
                            id = dialogData?.id?: 0,
                            subjectName = userInputDialogData.subjectName.text,
                            subjectCode = userInputDialogData.subjectName.code,
                            gainPrice = StockUtils.getNumInsertComma(gainPrice.toString()),
                            purchaseDate = userInputDialogData.purchaseDate,
                            purchasePrice = userInputDialogData.purchasePrice,
                            purchaseCount = userInputDialogData.purchaseCount.toInt(),
                            currentPrice = currentPrice,
                            dayToDayPrice = currentPriceData.dayToDayPrice,
                            dayToDayPercent = currentPriceData.dayToDayPercent,
                            yesterdayPrice = currentPriceData.yesterdayPrice
                        )
                        if (dialogData == null) {
                            val isAddSuccess = myStockViewModel.addMyStock(myStockEntity)
                            if (isAddSuccess) {
                                dialog.dismiss()
                            }
                        }
                        if (dialogData != null) {
                            val isUpdateSuccess = myStockViewModel.updateMyStock(myStockEntity)
                            if (isUpdateSuccess) {
                                dialog.dismiss()
                            }
                        }
                    }
                }
            }).show(childFragmentManager, "MyStockInputDialog")
    }

    private fun showSellInputDialog(myStockEntity: MyStockEntity) {
        MyStockSellInputDialog(
            mContext = requireContext(),
            dialogData = MyStockSellInputDialog.MyStockSellInputDialogData(
                myStockEntity = myStockEntity
            ),
            callBack = object : MyStockSellInputDialog.CallBack {
                override fun onInputDialogCompleteClicked(
                    dialog: MyStockSellInputDialog,
                    userInputDialogData: MyStockSellInputDialog.MyStockSellInputDialogData
                ) {
                    myStockViewModel.requestAddIncomeNote(
                        reqIncomeNoteInfo = ReqIncomeNoteInfo(
                            subjectName = myStockEntity.subjectName,
                            sellDate = userInputDialogData.sellDate,
                            purchasePrice = StockUtils.getNumDeletedComma(myStockEntity.purchasePrice).toDouble(),
                            sellPrice = StockUtils.getNumDeletedComma(userInputDialogData.sellPrice).toDouble(),
                            sellCount = userInputDialogData.sellCount.toInt()
                        ),
                        myStockEntity = myStockEntity
                    )
                    dialog.dismiss()
                }
            }
        ).show(requireActivity().supportFragmentManager, TAG)
    }

    private fun showOneBtnDialog(msg: String) {
        if (myStockViewModel.isDialogShowing) {
            return
        }
        val fragmentDialog = CommonOneBtnDialog(
            requireContext(),
            CommonOneBtnDialog.CommonOneBtnData(
                noticeText = msg,
                btnText = getString(R.string.Common_Ok),
                btnListener = { _: View, dialog ->
                    dialog.dismiss()
                    myStockViewModel.isDialogShowing = false
                }
            )
        )
        fragmentDialog.show(requireActivity().supportFragmentManager, TAG)
        myStockViewModel.isDialogShowing = true
    }
}