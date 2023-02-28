package com.yjpapp.stockportfolio.function.mystock.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yjpapp.data.model.SubjectName
import com.yjpapp.stockportfolio.R

data class MyStockPurchaseInputDialogData(
    var id: Int = -1,
    var subjectName: SubjectName = SubjectName(),
    var purchaseDate: String = "",
    var purchasePrice: String = "",
    var purchaseCount: String = "",
)

@Composable
fun MyStockPurchaseInputDialogContent(
    onDismissRequest: (data: MyStockPurchaseInputDialogData) -> Unit
) {
    val data = MyStockPurchaseInputDialogData()
    val subjectText = remember { mutableStateOf(data.subjectName.text) }
    val purchaseDateText = remember { mutableStateOf(data.purchaseDate) }
    val purchasePriceText = remember { mutableStateOf(data.purchasePrice) }
    val purchaseCountText = remember { mutableStateOf(data.purchaseCount) }
    Dialog(onDismissRequest = { onDismissRequest(data) }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
//                .padding(PaddingValues(start = 30.dp, end = 30.dp)),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(PaddingValues(start = 20.dp, end = 20.dp))
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.IncomeNoteFragment_SubjectName),
                        fontSize = 16.sp,
                        color = Color(0xff666666)
                    )
                    TextField(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        value = subjectText.value,
                        onValueChange = { subjectText.value = it },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.MyStockFragment_Purchase_Date),
                        fontSize = 16.sp,
                        color = Color(0xff666666)
                    )
                    TextField(
                        value = purchaseDateText.value,
                        onValueChange = { purchaseDateText.value = it }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.MyStockFragment_Purchase_Average),
                        fontSize = 16.sp,
                        color = Color(0xff666666)
                    )
                    TextField(
                        value = purchasePriceText.value,
                        onValueChange = { purchasePriceText.value = it }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.MyStockFragment_Holding_Quantity),
                        fontSize = 16.sp,
                        color = Color(0xff666666)
                    )
                    TextField(
                        value = purchaseCountText.value,
                        onValueChange = { purchaseCountText.value = it }
                    )
                }
            }
        }

    }
}