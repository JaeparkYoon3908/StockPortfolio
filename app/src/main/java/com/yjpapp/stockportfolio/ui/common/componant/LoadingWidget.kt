package com.yjpapp.stockportfolio.ui.common.componant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_E52B4E

@Composable
fun LoadingWidget(
    modifier: Modifier = Modifier,
    title: String? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 480.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color_E52B4E
            )
            title?.apply {
                Text(
                    text = this,
                    color = Color_222222,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}