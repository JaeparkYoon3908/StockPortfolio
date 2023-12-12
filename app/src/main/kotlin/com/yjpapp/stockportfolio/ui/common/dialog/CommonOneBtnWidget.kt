package com.yjpapp.stockportfolio.ui.common.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.yjpapp.stockportfolio.ui.common.theme.Color_E52B4E
import com.yjpapp.stockportfolio.ui.common.theme.Color_FFFFFF

@Composable
fun CommonOneBtnWidget(
    noticeText: String = "",
    btnText: String = stringResource(id = R.string.Common_Complete),
    onClickListener: () -> Unit
) {
    Dialog(onDismissRequest = {  }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = noticeText)
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(color = Color_E52B4E),
                    onClick = {

                    }
                ) {
                    Text(
                        text = btnText,
                        color = Color_FFFFFF,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreView() {
    CommonOneBtnWidget{}
}