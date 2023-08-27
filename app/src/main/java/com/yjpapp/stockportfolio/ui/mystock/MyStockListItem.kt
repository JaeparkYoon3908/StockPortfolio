package com.yjpapp.stockportfolio.ui.mystock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yjpapp.stockportfolio.data.model.SubjectName
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.data.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.data.model.response.StockPriceInfo
import com.yjpapp.stockportfolio.ui.common.StockConfig
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_4876C7
import com.yjpapp.stockportfolio.ui.common.theme.Color_666666
import com.yjpapp.stockportfolio.ui.common.theme.Color_80000000
import com.yjpapp.stockportfolio.ui.common.theme.Color_CD4632
import com.yjpapp.stockportfolio.ui.common.theme.Color_FBFBFB
import com.yjpapp.stockportfolio.ui.common.theme.Color_FFFFFF
import com.yjpapp.stockportfolio.ui.mystock.dialog.MyStockPurchaseInputDialogContent
import com.yjpapp.stockportfolio.ui.mystock.dialog.MyStockPurchaseInputDialogData
import com.yjpapp.stockportfolio.util.StockUtils
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe
import de.charlex.compose.rememberRevealState
import de.charlex.compose.reset
import kotlinx.coroutines.launch

/**
 * MyStockList Item Compose UI
 * @author Yoon Jae-park
 * @since 2022.02
 */
@ExperimentalMaterialApi
@Composable
fun MyStockListItemWidget(
    myStockViewModel: MyStockViewModel,
    myStockEntity: MyStockEntity
) {
    val revealSwipeState = rememberRevealState()
    val coroutineScope = rememberCoroutineScope()
    val maxRevealDp = 110.dp
    var showMyStockPurchaseInputDialog by remember { mutableStateOf(false) }
    if (showMyStockPurchaseInputDialog) {
        MyStockPurchaseInputDialogContent(
            dialogData = MyStockPurchaseInputDialogData(
                id = myStockEntity.id,
                stockPriceInfo = StockPriceInfo(
                    itmsNm = myStockEntity.subjectName,
                    srtnCd = myStockEntity.subjectCode
                ),
                purchaseDate = myStockEntity.purchaseDate,
                purchasePrice = myStockEntity.purchasePrice,
                purchaseCount = myStockEntity.purchaseCount.toString()
            )
        ) { dialogData, isComplete ->
            showMyStockPurchaseInputDialog = false
            if (isComplete) {
                //TODO 완료 했을 때 데이터 처리
            }
        }
    }
    RevealSwipe(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        maxRevealDp = maxRevealDp,
        backgroundCardEndColor = Color_FBFBFB,
        animateBackgroundCardColor = false,
        state = revealSwipeState,
        directions = setOf(
//        RevealDirection.StartToEnd,
            RevealDirection.EndToStart
        ),

        hiddenContentEnd = {
            Column(
                modifier = Modifier
                    .width(maxRevealDp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable { showMyStockPurchaseInputDialog = true }
                        .fillMaxWidth()
                        .weight(0.333f)
                        .background(color = Color_80000000)
                ) {
                    Text(
                        text = stringResource(R.string.Common_Edit),
                        fontSize = 16.sp,
                        color = Color_FFFFFF
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                myStockViewModel.deleteMyStock(
                                    myStockEntity = myStockEntity
                                )
                            }
                            coroutineScope.launch {
                                revealSwipeState.reset()
                            }
                        }
                        .fillMaxWidth()
                        .weight(0.333f)
                        .background(color = Color_CD4632)
                ) {
                    Text(
                        text = stringResource(R.string.Common_Delete),
                        fontSize = 16.sp,
                        color = Color_FFFFFF
                    )
                }
            }
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
            elevation = 0.dp,
            backgroundColor = Color_FBFBFB
        ) {
            val purchasePriceNumber =
                StockUtils.getNumDeletedComma(myStockEntity.purchasePrice).toDouble()
            val currentPriceNumber =
                StockUtils.getNumDeletedComma(myStockEntity.currentPrice).toDouble()
            val gainPriceNumber = StockUtils.getNumDeletedComma(myStockEntity.gainPrice).toDouble()
            Column(
                modifier = Modifier
//                    .padding(bottom = 10.dp)
                    .wrapContentHeight()
            ) {

                Row(
                    Modifier
                        .padding(start = 15.dp, end = 15.dp)
                ) {
                    Text(
                        text = myStockEntity.subjectName,
                        fontSize = 16.sp,
                        maxLines = 1,
                        color = Color_222222,
                        modifier = Modifier
                            .weight(0.55f)
                            .padding(top = 10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .weight(0.45f)
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.MyStockFragment_Gain),
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color_222222,
                        )
                        //수익
                        Text(
                            text = StockUtils.getPriceNum(myStockEntity.gainPrice),
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = when {
                                gainPriceNumber > 0 -> Color_CD4632
                                gainPriceNumber < 0 -> Color_4876C7
                                else -> Color_222222
                            },
                            modifier = Modifier
                                .padding(start = 5.dp)
                        )

                        //수익 퍼센트
                        val allPurchasePriceNum =
                            (purchasePriceNumber * myStockEntity.purchaseCount)
                        val allCurrentPriceNum = (currentPriceNumber * myStockEntity.purchaseCount)
                        val gainPercentNum =
                            StockUtils.calculateGainPercent(allPurchasePriceNum, allCurrentPriceNum)
                        Text(
                            text = "(${StockUtils.getRoundsPercentNumber(gainPercentNum)})",
                            fontSize = 12.sp,
                            maxLines = 1,
                            color = when {
                                gainPriceNumber > 0 -> Color_CD4632
                                gainPriceNumber < 0 -> Color_4876C7
                                else -> Color_222222
                            },
                            modifier = Modifier
                                .padding(start = 5.dp)
                        )
                    }
                }

                Divider(
                    Modifier
                        .padding(10.dp)
                )
                Row(
                    Modifier
                        .padding(start = 15.dp, end = 15.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .weight(0.5f)
                    ) {
                        //매수일
                        Text(
                            text = stringResource(R.string.MyStockFragment_Purchase_Date),
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color_666666
                        )

                        Text(
                            text = myStockEntity.purchaseDate,
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color_222222,
                            modifier = Modifier
                                .padding(start = 10.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .weight(0.5f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        //평균단가
                        Text(
                            text = stringResource(R.string.MyStockFragment_Purchase_Average),
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color_666666,
                            modifier = Modifier
                                .padding(start = 10.dp)
                        )

                        Text(
                            text = StockUtils.getPriceNum(myStockEntity.purchasePrice),
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color_222222,
                            modifier = Modifier
                                .padding(start = 10.dp)
                        )
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.weight(0.35f)
                    ) {
                        Text(
                            text = stringResource(R.string.MyStockFragment_Holding_Quantity),
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = colorResource(id = R.color.color_666666)
                        )

                        Text(
                            text = myStockEntity.purchaseCount.toString(),
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = colorResource(id = R.color.color_222222),
                            modifier = Modifier
                                .padding(start = 10.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(0.65f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.MyStockFragment_Current_Price),
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color_666666,
                            modifier = Modifier
                                .padding(start = 10.dp)
                        )

                        Text(
                            text = "${StockConfig.koreaMoneySymbol}${myStockEntity.currentPrice}",
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color_222222,
                            modifier = Modifier
                                .padding(start = 10.dp)
                        )

                        Text(
                            text = myStockEntity.dayToDayPrice,
                            fontSize = 11.sp,
                            maxLines = 1,
                            color = when {
                                myStockEntity.dayToDayPrice.toInt() > 0 -> Color_CD4632
                                myStockEntity.dayToDayPrice.toInt() == 0 -> Color_222222
                                else -> Color_4876C7
                            },
                            modifier = Modifier
                                .padding(start = 5.dp)

                        )

                        Text(
                            text = "(${myStockEntity.dayToDayPercent}%)",
                            fontSize = 11.sp,
                            color = when {
                                myStockEntity.dayToDayPrice.toInt() > 0 -> Color_CD4632
                                myStockEntity.dayToDayPrice.toInt() == 0 -> Color_222222
                                else -> Color_4876C7
                            },
                        )
                    }
                }
            }
        }
    }
}