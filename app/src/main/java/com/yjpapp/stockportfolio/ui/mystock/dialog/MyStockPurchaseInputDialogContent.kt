package com.yjpapp.stockportfolio.ui.mystock.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yjpapp.data.model.SubjectName
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.theme.Color_222222
import com.yjpapp.stockportfolio.common.theme.Color_E52B4E
import com.yjpapp.stockportfolio.common.theme.Color_F1F1F1
import com.yjpapp.stockportfolio.common.theme.Color_FFFFFF

data class MyStockPurchaseInputDialogData(
    var id: Int = -1,
    var subjectName: SubjectName = SubjectName(),
    var purchaseDate: String = "",
    var purchasePrice: String = "",
    var purchaseCount: String = "",
)

@Composable
fun MyStockPurchaseInputDialogContent(
    dialogData: MyStockPurchaseInputDialogData = MyStockPurchaseInputDialogData(),
    onDismissRequest: (data: MyStockPurchaseInputDialogData?) -> Unit
) {
    val subjectText = remember { mutableStateOf(dialogData.subjectName.text) }
    val purchaseDateText = remember { mutableStateOf(dialogData.purchaseDate) }
    val purchasePriceText = remember { mutableStateOf(dialogData.purchasePrice) }
    val purchaseCountText = remember { mutableStateOf(dialogData.purchaseCount) }
    val inputPurchaseInputDialogData = MyStockPurchaseInputDialogData()
    Dialog(onDismissRequest = { onDismissRequest(dialogData) }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
//                .padding(PaddingValues(start = 30.dp, end = 30.dp)),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .padding(PaddingValues(start = 20.dp, end = 20.dp))
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.IncomeNoteFragment_SubjectName),
                            fontSize = 16.sp,
                            color = Color(0xff666666)
                        )
                        BasicTextField(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            value = subjectText.value,
                            onValueChange = { subjectText.value = it },
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .drawWithContent {
                                            drawContent()
                                            drawLine(
                                                color = Color(0x1a000000),
                                                start = Offset(
                                                    x = 0f,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                end = Offset(
                                                    x = size.width,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                strokeWidth = 1.dp.toPx(),
                                            )
                                        },
                                ) {
                                    innerTextField()
                                    Spacer(modifier = Modifier.height(5.dp))
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.MyStockFragment_Purchase_Date),
                            fontSize = 16.sp,
                            color = Color(0xff666666)
                        )
                        BasicTextField(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            value = purchaseDateText.value,
                            onValueChange = { purchaseDateText.value = it },
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .drawWithContent {
                                            drawContent()
                                            drawLine(
                                                color = Color(0x1a000000),
                                                start = Offset(
                                                    x = 0f,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                end = Offset(
                                                    x = size.width,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                strokeWidth = 1.dp.toPx(),
                                            )
                                        },
                                ) {
                                    innerTextField()
                                    Spacer(modifier = Modifier.height(5.dp))
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.MyStockFragment_Purchase_Average),
                            fontSize = 16.sp,
                            color = Color(0xff666666)
                        )
                        BasicTextField(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            value = purchasePriceText.value,
                            onValueChange = { purchasePriceText.value = it },
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .drawWithContent {
                                            drawContent()
                                            drawLine(
                                                color = Color(0x1a000000),
                                                start = Offset(
                                                    x = 0f,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                end = Offset(
                                                    x = size.width,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                strokeWidth = 1.dp.toPx(),
                                            )
                                        },
                                ) {
                                    innerTextField()
                                    Spacer(modifier = Modifier.height(5.dp))
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.MyStockFragment_Holding_Quantity),
                            fontSize = 16.sp,
                            color = Color(0xff666666)
                        )
                        BasicTextField(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            value = purchaseCountText.value,
                            onValueChange = { purchaseCountText.value = it },
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .drawWithContent {
                                            drawContent()
                                            drawLine(
                                                color = Color(0x1a000000),
                                                start = Offset(
                                                    x = 0f,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                end = Offset(
                                                    x = size.width,
                                                    y = size.height - 1.dp.toPx(),
                                                ),
                                                strokeWidth = 1.dp.toPx(),
                                            )
                                        },
                                ) {
                                    innerTextField()
                                    Spacer(modifier = Modifier.height(5.dp))
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(50.dp)
                            .background(color = Color_F1F1F1),
                        onClick = { onDismissRequest(null) }) {
                        Text(
                            text = stringResource(id = R.string.Common_Cancel),
                            color = Color_222222,
                            fontSize = 15.sp
                        )
                    }
                    TextButton(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(50.dp)
                            .background(color = Color_E52B4E),
                        onClick = { onDismissRequest(dialogData) }
                    ) {
                        Text(
                            text = stringResource(id = R.string.Common_Complete),
                            color = Color_FFFFFF,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
@Preview
@Composable
private fun Preview() {
    MyStockPurchaseInputDialogContent(onDismissRequest = {})
}
