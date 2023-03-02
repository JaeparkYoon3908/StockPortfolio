package com.yjpapp.stockportfolio.function.mystock

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.theme.Color_222222
import com.yjpapp.stockportfolio.common.theme.Color_4876C7
import com.yjpapp.stockportfolio.common.theme.Color_CD4632
import com.yjpapp.stockportfolio.common.theme.Color_Line_1A000000
import com.yjpapp.stockportfolio.util.StockUtils
import kotlinx.coroutines.launch

/**
 * MyStock UI Compose 분리
 * @author Yoon Jae-park
 * @since 2023.02
 */
@Composable
@ExperimentalMaterialApi
fun MyStockScreen(
    viewModel: MyStockViewModel = hiltViewModel()
) {
    Column {
        TotalPriceComposable(
            myStockViewModel = viewModel
        )
        StockListComposable(
            myStockViewModel = viewModel
        )
    }
}

@Composable
private fun TotalPriceComposable(
    myStockViewModel: MyStockViewModel,
) {
    val totalPurchasePrice by myStockViewModel.totalPurchasePrice.collectAsState()
    val totalEvaluationAmount by myStockViewModel.totalEvaluationAmount.collectAsState()
    val totalGainPrice by myStockViewModel.totalGainPrice.collectAsState()
    val totalGainPricePercent by myStockViewModel.totalGainPricePercent.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.common_15dp),
                end = dimensionResource(id = R.dimen.common_15dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.common_10dp))
        ) {
            Text(
                text = stringResource(R.string.MyStockFragment_Total_Purchase_Price),
                color = Color_222222,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier
                    .weight(0.30f)
            )
            Text(
                text = StockUtils.getPriceNum(totalPurchasePrice),
                color = Color_222222,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier
                    .weight(0.70f)
            )
        }

        Row(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.common_10dp))

        ) {
            Text(
                text = stringResource(R.string.MyStockFragment_Total_Evaluation_Amount),
                color = Color_222222,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier
                    .weight(0.30f)
            )
            Text(
                text = StockUtils.getPriceNum(totalEvaluationAmount),
                color = Color_222222,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier
                    .weight(0.70f)
            )
        }

        Row(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.common_10dp))
        ) {
            Text(
                text = stringResource(R.string.Common_gains_losses),
                color = Color_222222,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier
                    .weight(0.30f)
            )
            Text(
                text = StockUtils.getPriceNum(totalGainPrice),
                color = when {
                    StockUtils.getNumDeletedComma(totalGainPrice).toDouble() > 0 -> Color_CD4632
                    StockUtils.getNumDeletedComma(totalGainPrice).toDouble() < 0 -> Color_4876C7
                    else -> Color_CD4632
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier
                    .weight(0.70f)
            )
        }

        Row(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.common_10dp))
        ) {
            Text(
                text = stringResource(R.string.Common_GainPercent),
                color = Color_222222,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier
                    .weight(0.30f)
            )
            Text(
                text = totalGainPricePercent,
                color = when {
                    StockUtils.getNumDeletedComma(totalGainPrice).toDouble() > 0 -> Color_CD4632
                    StockUtils.getNumDeletedComma(totalGainPrice).toDouble() < 0 -> Color_4876C7
                    else -> Color_CD4632
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier
                    .weight(0.70f)
            )
        }
        Divider(
            color = Color_Line_1A000000,
            thickness = 1.dp,
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.common_10dp))
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "UnsafeRepeatOnLifecycleDetector")
@ExperimentalMaterialApi
@Composable
private fun StockListComposable(
    myStockViewModel: MyStockViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold { paddingValues ->
        val listState = rememberLazyListState()
        LazyColumn(
            reverseLayout = true,
            state = listState,
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            items(
                count = myStockViewModel.myStockInfoList.size
            ) {
                MyStockListItemWidget(
                    myStockViewModel = myStockViewModel,
                    myStockEntity = myStockViewModel.myStockInfoList[it],
                )
            }
        }
        coroutineScope.launch {
            myStockViewModel.scrollIndex.collect { position ->
                listState.scrollToItem(position)
            }
        }
    }
}
