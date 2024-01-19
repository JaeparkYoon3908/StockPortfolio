package com.yjpapp.stockportfolio.ui.main.mystock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_4876C7
import com.yjpapp.stockportfolio.ui.common.theme.Color_CD4632
import com.yjpapp.stockportfolio.ui.main.MainViewModel
import com.yjpapp.stockportfolio.util.StockUtils

@Composable
internal fun TotalPriceWidget(
    myStockViewModel: MainViewModel,
) {
    val myStockUiState by myStockViewModel.myStockUiState.collectAsState()

    Card(
        modifier = Modifier.padding(20.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White
    ) {
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
                    .padding(top = dimensionResource(id = R.dimen.common_20dp))
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
                    text = StockUtils.getPriceNum(myStockUiState.totalPurchasePrice),
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
                    text = StockUtils.getPriceNum(myStockUiState.totalEvaluationAmount),
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
                    text = StockUtils.getPriceNum(myStockUiState.totalGainPrice),
                    color = when {
                        StockUtils.getNumDeletedComma(myStockUiState.totalGainPrice).toDouble() > 0 -> Color_CD4632
                        StockUtils.getNumDeletedComma(myStockUiState.totalGainPrice).toDouble() < 0 -> Color_4876C7
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
                    .padding(
                        top = dimensionResource(id = R.dimen.common_10dp),
                        bottom = dimensionResource(id = R.dimen.common_20dp)
                    )
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
                    text = myStockUiState.totalGainPricePercent,
                    color = when {
                        StockUtils.getNumDeletedComma(myStockUiState.totalGainPrice).toDouble() > 0 -> Color_CD4632
                        StockUtils.getNumDeletedComma(myStockUiState.totalGainPrice).toDouble() < 0 -> Color_4876C7
                        else -> Color_CD4632
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }
        }
    }
}
