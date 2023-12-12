package com.yjpapp.stockportfolio.ui.common.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_E52B4E
import com.yjpapp.stockportfolio.ui.common.theme.Color_F1F1F1
import com.yjpapp.stockportfolio.ui.common.theme.Color_FFFFFF

@Composable
fun CommonTwoBtnWidget(
    noticeText: String = "",
    leftBtnText: String = stringResource(id = R.string.Common_Cancel),
    rightBtnText: String = stringResource(id = R.string.Common_Complete),
    onLeftBtnClickListener: () -> Unit,
    onRightBtnClickListener: () -> Unit,
) {
    Dialog(onDismissRequest = {  }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column {
                Text(text = noticeText)
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(50.dp)
                            .background(color = Color_F1F1F1),
                        onClick = {  }) {
                        androidx.compose.material.Text(
                            text = leftBtnText,
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

                        }
                    ) {
                        Text(
                            text = rightBtnText,
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
    CommonTwoBtnWidget(onLeftBtnClickListener = {  }) {}
}