package com.yjpapp.stockportfolio.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yjpapp.data.model.MyStockData
import com.yjpapp.data.model.response.StockPriceData
import com.yjpapp.stockportfolio.model.DialogType
import com.yjpapp.stockportfolio.ui.main.mystock.dialog.MyStockPurchaseInputDialogContent
import com.yjpapp.stockportfolio.ui.main.mystock.dialog.MyStockPurchaseInputDialogData
import com.yjpapp.stockportfolio.util.StockUtils
import de.charlex.compose.reset
import kotlinx.coroutines.launch

@Composable
fun MainDialogWidget(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val mainDialogType by viewModel.dialogUiState.collectAsStateWithLifecycle(initialValue = DialogType.None)
    when (mainDialogType) {
        is DialogType.InsertPurchaseInput -> {
            MyStockPurchaseInputDialogContent { dialogData, isComplete ->
                if (isComplete) {
                    viewModel.addMyStock(
                        MyStockData(
                            subjectName = dialogData.stockPriceInfo.itmsNm,
                            subjectCode = dialogData.stockPriceInfo.isinCd,
                            purchaseDate = dialogData.purchaseDate,
                            purchasePrice = dialogData.purchasePrice,
                            purchaseCount = dialogData.purchaseCount.toIntOrNull()?: 0,
                            currentPrice = StockUtils.getNumInsertComma(dialogData.stockPriceInfo.clpr),
                            dayToDayPrice = dialogData.stockPriceInfo.vs,
                            dayToDayPercent = dialogData.stockPriceInfo.fltRt,
                            basDt = dialogData.stockPriceInfo.basDt,
                        )
                    )
                }
            }
        }
        is DialogType.UpdatePurchaseInput -> {
            val myStockData = (mainDialogType as DialogType.UpdatePurchaseInput).myStockData

            MyStockPurchaseInputDialogContent(
                dialogData = MyStockPurchaseInputDialogData(
                    id = myStockData.id,
                    stockPriceInfo = StockPriceData(
                        itmsNm = myStockData.subjectName,
                        srtnCd = myStockData.subjectCode
                    ),
                    purchaseDate = myStockData.purchaseDate,
                    purchasePrice = myStockData.purchasePrice,
                    purchaseCount = myStockData.purchaseCount.toString()
                )
            ) { dialogData, isComplete ->
                if (isComplete) {
                    viewModel.updateMyStock(
                        MyStockData(
                            id = myStockData.id,
                            subjectName = dialogData.stockPriceInfo.itmsNm,
                            subjectCode = dialogData.stockPriceInfo.isinCd,
                            purchaseDate = dialogData.purchaseDate,
                            purchasePrice = dialogData.purchasePrice,
                            purchaseCount = dialogData.purchaseCount.toIntOrNull()?: 0,
                            currentPrice = myStockData.currentPrice,
                            dayToDayPrice = myStockData.dayToDayPrice,
                            dayToDayPercent = myStockData.dayToDayPercent,
                            basDt = myStockData.basDt
                        )
                    )
                }
            }
        }
        DialogType.None -> Unit
        else -> {

        }
    }

}