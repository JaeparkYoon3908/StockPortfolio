package com.yjpapp.stockportfolio.function.mystock.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yjpapp.stockportfolio.R

@Composable
fun MyStockPurchaseInputDialogContent(
    onDismissRequest: (data: MyStockPurchaseInputDialog.MyStockPurchaseInputDialogData) -> Unit
) {
    val data = MyStockPurchaseInputDialog.MyStockPurchaseInputDialogData()
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
                    TextField(value = data.subjectName, onValueChange = )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}