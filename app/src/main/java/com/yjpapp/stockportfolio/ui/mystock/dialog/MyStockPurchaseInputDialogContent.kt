package com.yjpapp.stockportfolio.ui.mystock.dialog

import android.app.Activity
import android.content.Intent
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.data.model.response.StockPriceInfo
import com.yjpapp.stockportfolio.extension.getSerializableExtraData
import com.yjpapp.stockportfolio.ui.common.StockConfig
import com.yjpapp.stockportfolio.ui.common.dialog.CommonDatePickerDialog
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_666666
import com.yjpapp.stockportfolio.ui.common.theme.Color_E52B4E
import com.yjpapp.stockportfolio.ui.common.theme.Color_F1F1F1
import com.yjpapp.stockportfolio.ui.common.theme.Color_FFFFFF
import com.yjpapp.stockportfolio.ui.mystock.search.StockSearchActivity
import com.yjpapp.stockportfolio.util.StockUtils
import es.dmoral.toasty.Toasty

data class MyStockPurchaseInputDialogData(
    var id: Int = -1,
    var stockPriceInfo: StockPriceInfo = StockPriceInfo(),
    var purchaseDate: String = "",
    var purchasePrice: String = "",
    var purchaseCount: String = "",
)

@Composable
fun MyStockPurchaseInputDialogContent(
    dialogData: MyStockPurchaseInputDialogData = MyStockPurchaseInputDialogData(),
    onDismissRequest: (data: MyStockPurchaseInputDialogData, isComplete: Boolean) -> Unit
) {
    val context = LocalContext.current
    var rememberStockPriceInfo by remember { mutableStateOf(dialogData.stockPriceInfo) }
    var purchaseDateText by remember { mutableStateOf(dialogData.purchaseDate) }
    var purchasePriceText by remember { mutableStateOf(TextFieldValue(text = dialogData.purchasePrice)) }
    var purchaseCountText by remember { mutableStateOf(dialogData.purchaseCount) }
    val inputPurchaseInputDialogData = MyStockPurchaseInputDialogData()
    val stockSearchActivityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val stockPriceInfo = result.data?.getSerializableExtraData("stockPriceInfo", StockPriceInfo::class.java)
                if (stockPriceInfo is StockPriceInfo) {
                    rememberStockPriceInfo = stockPriceInfo
                }
            }
        }
    )
    Dialog(onDismissRequest = { onDismissRequest(dialogData, false) }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
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
                        modifier = Modifier.clickable {
                            val intent = Intent(context, StockSearchActivity::class.java)
                            stockSearchActivityResultLauncher.launch(intent)
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.IncomeNoteFragment_SubjectName),
                            fontSize = 16.sp,
                            color = Color(0xff666666)
                        )
                        BasicTextField(
                            modifier = Modifier.padding(start = 10.dp),
                            value = rememberStockPriceInfo.itmsNm,
                            onValueChange = { rememberStockPriceInfo.itmsNm = it },
                            singleLine = true,
                            readOnly = true,
                            enabled = false,
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
                                    Box {
                                        if (rememberStockPriceInfo.itmsNm.isEmpty()) {
                                            Text(
                                                text = stringResource(id = R.string.EditIncomeNoteDialog_SubjectName_Hint),
                                                style = TextStyle(fontSize = 14.sp, color = Color_666666)
                                            )
                                        }
                                        innerTextField()
                                    }
                                    Spacer(modifier = Modifier.height(5.dp))
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.clickable {
                            var year = ""
                            var month = ""
                            var day = ""
                            if (purchaseDateText != "") {
                                val split = purchaseDateText.split("-")
                                year = split[0]
                                month = split[1]
                                day = split[2]
                            }
                            //매수 날짜 선택 다이얼로그 show
                            CommonDatePickerDialog(context, year, month, day).apply {
                                setListener { _: DatePicker?, year, month, dayOfMonth ->
                                    val todaySplit = StockUtils.getTodayYYYY_MM_DD().split(".")
                                    if (year == todaySplit[0].toInt() && month > todaySplit[1].toInt()) {
                                        Toasty.error(
                                            mContext,
                                            "선택하신 월이 현재 보다 큽니다.",
                                            Toasty.LENGTH_LONG
                                        ).show()
                                        return@setListener
                                    }
                                    if (year == todaySplit[0].toInt() && month == todaySplit[1].toInt() && dayOfMonth > todaySplit[2].toInt()) {
                                        Toasty.error(
                                            mContext,
                                            "선택하신 일이 현재 보다 큽니다.",
                                            Toasty.LENGTH_LONG
                                        ).show()
                                        return@setListener
                                    }
                                    val purchaseYear = year.toString()
                                    val purchaseMonth = if (month < 10) {
                                        "0$month"
                                    } else {
                                        month.toString()
                                    }
                                    val purchaseDay = if (dayOfMonth < 10) {
                                        "0$dayOfMonth"
                                    } else {
                                        dayOfMonth.toString()
                                    }
                                    purchaseDateText = "$purchaseYear-$purchaseMonth-$purchaseDay"
                                    dismiss()
                                }
                                show()
                            }
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.MyStockFragment_Purchase_Date),
                            fontSize = 16.sp,
                            color = Color(0xff666666)
                        )
                        BasicTextField(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            value = purchaseDateText,
                            onValueChange = { purchaseDateText = it },
                            singleLine = true,
                            readOnly = true,
                            enabled = false,
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
                                    Box {
                                        if (purchaseDateText.isEmpty()) {
                                            Text(
                                                text = stringResource(id = R.string.EditIncomeNoteDialog_Purchase_Date_Hint),
                                                style = TextStyle(fontSize = 14.sp, color = Color_666666)
                                            )
                                        }
                                        innerTextField()
                                    }
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
                            color = Color_666666
                        )
                        BasicTextField(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            value = purchasePriceText,
                            textStyle = TextStyle(fontSize = 14.sp, color = Color_222222),
                            onValueChange = {
                                purchasePriceText = if (it.text.isNotEmpty()) {
                                    val commaInsertedText = StockUtils.getNumInsertComma(it.text)
                                    TextFieldValue(text = commaInsertedText, selection = TextRange(commaInsertedText.length))
                                } else {
                                    TextFieldValue(text = it.text)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                                    Box {
                                        if (purchasePriceText.text.isEmpty()) {
                                            Text(
                                                text = stringResource(id = R.string.MyStockInputDialog_Purchase_Price_Hint),
                                                style = TextStyle(fontSize = 14.sp, color = Color_666666)
                                            )
                                        }
                                        Row {
                                            if (purchasePriceText.text.isNotEmpty()) {
                                                Text(
                                                    text = StockConfig.koreaMoneySymbol,
                                                    style = TextStyle(fontSize = 14.sp, color = Color_222222)
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
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
                            value = purchaseCountText,
                            onValueChange = { purchaseCountText = it },
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
                                    Box {
                                        if (purchaseCountText.isEmpty()) {
                                            Text(
                                                text = stringResource(id = R.string.MyStockInputDialog_Purchase_Count_Hint),
                                                style = TextStyle(fontSize = 14.sp, color = Color_666666)
                                            )
                                        }
                                        innerTextField()
                                    }
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
                        onClick = { onDismissRequest(MyStockPurchaseInputDialogData(), false) }) {
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
                        onClick = {
                            if (purchaseCountText.isEmpty() ||
                                purchaseDateText.isEmpty() ||
                                purchasePriceText.text.isEmpty() ||
                                rememberStockPriceInfo.itmsNm.isEmpty()
                            ) {
                                Toasty.error(
                                    context,
                                    context.getString(R.string.MyStockInputDialog_Error_Message),
                                    Toasty.LENGTH_SHORT
                                ).show()
                                return@TextButton
                            }
                            if (purchaseCountText.toInt() == 0) {
                                Toasty.error(
                                    context,
                                    context.getString(R.string.MyStockInputDialog_Error_Message_Purchase_Count),
                                    Toasty.LENGTH_SHORT
                                ).show()
                                return@TextButton
                            }
                            onDismissRequest(
                                MyStockPurchaseInputDialogData(
                                    stockPriceInfo = rememberStockPriceInfo,
                                    purchaseDate = purchaseDateText,
                                    purchasePrice = purchasePriceText.text,
                                    purchaseCount = purchaseCountText
                                ),
                                true
                            )
                        }
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
    MyStockPurchaseInputDialogContent(onDismissRequest = { data, isComplete -> })
}
