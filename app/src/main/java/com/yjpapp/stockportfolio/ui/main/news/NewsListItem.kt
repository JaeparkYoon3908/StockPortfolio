package com.yjpapp.stockportfolio.ui.main.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yjpapp.stockportfolio.data.model.NewsData
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222

@Composable
fun NewsListItem(
    data: NewsData,
    onItemClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .clickable { onItemClick() },
        shape = RoundedCornerShape(10.dp),

    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = data.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color_222222
            )
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = data.description,
                maxLines = 3,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Normal,
                color = Color_222222
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NewsListItem(data = NewsData())
}