package com.yjpapp.stockportfolio.common.theme

import androidx.compose.ui.graphics.Color

fun getColor(colorString: String): Color {
    return Color(android.graphics.Color.parseColor("#$colorString"))
}

val Black = Color(0xff000000)
val White = Color(0xffffffff)
val BackgroundFBFBFBFB = Color(0xfffbfbfb)
val Color80000000 = Color(0x80000000)