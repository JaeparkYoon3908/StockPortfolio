package com.yjpapp.stockportfolio.function.mystock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import es.dmoral.toasty.Toasty
import com.yjpapp.stockportfolio.R

class MyStockComposeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SimpleComposable()
            }
        }
    }

    @Preview
    @Composable
    fun SimpleComposable() {
        Column {
            Text(text = "Hello world.",
                modifier = Modifier.clickable {
                    Toasty.normal(requireContext(), "Test!!").show()
                }
            )

            Text(text = "Alan walker",
                modifier = Modifier.clickable {
                    Toasty.normal(requireContext(), "Test!!").show()
                }
            )

            Text(text = "All for down.",
                modifier = Modifier.clickable {
                    Toasty.normal(requireContext(), "Test!!").show()
                }
            )

            val count = remember { mutableStateOf(0) }
            Text(
                text = count.value.toString(),
                fontSize = 25.sp,
                color = colorResource(id = R.color.color_4876c7),
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp)
                    .width(400.dp)
                    .height(100.dp)
                    .clickable { count.value += 1 }
            )
        }
    }
}