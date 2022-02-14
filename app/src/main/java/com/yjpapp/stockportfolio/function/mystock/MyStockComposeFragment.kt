package com.yjpapp.stockportfolio.function.mystock

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.dialog.CommonDatePickerDialog
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockInputDialog
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class MyStockComposeFragment : Fragment() {
    private val myStockViewModel: MyStockViewModel by viewModels()
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            myStockViewModel.onViewCreated()
            setHasOptionsMenu(true)
            setContent {
                Column {
                    TotalPriceComposable()
                    StockListComposable()
                }
            }
        }
    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_my_stock, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_MyStockFragment_Add -> {
                myStockViewModel.apply {
                    inputDialogSubjectName = ""
                    inputDialogPurchaseDate = ""
                    inputDialogPurchasePrice.value = ""
                    inputDialogPurchaseCount = ""
                }
//                showInputDialog(true, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Preview
    @Composable
    private fun SimpleComposable() {
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

    @Preview
    @Composable
    private fun TotalPriceComposable() {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.common_15dp),
                end = dimensionResource(id = R.dimen.common_15dp)
            )
        ) {
            Row (
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ) {
                Text(
                    text = stringResource(id = R.string.MyStockFragment_Total_Purchase_Price),
                    color = colorResource(id = R.color.color_222222),
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)

                )
                Text(
                    text = "5000000000",
                    color = colorResource(id = R.color.color_222222),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row (
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))

            ) {
                Text(
                    text = stringResource(id = R.string.MyStockFragment_Total_Evaluation_Amount),
                    color = colorResource(id = R.color.color_222222),
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = "5000000000",
                    color = colorResource(id = R.color.color_222222),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row (
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ) {
                Text(
                    text = stringResource(id = R.string.Common_gains_losses),
                    color = colorResource(id = R.color.color_222222),
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = "5000000000",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row (
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ) {
                Text(
                    text = stringResource(id = R.string.Common_GainPercent),
                    color = colorResource(id = R.color.color_222222),
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = "5000000000",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }
            Divider(
                color = colorResource(id = R.color.color_line_1a000000),
                thickness = 1.dp,
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            )
        }
    }

    private fun showInputDialog(isInsertMode: Boolean, id: Int) {
        MyStockInputDialog(mContext, object : MyStockInputDialog.CallBack {
            override fun onInputDialogCompleteClicked(
                dialog: MyStockInputDialog,
                myStockInputDialogData: MyStockInputDialog.MyStockInputDialogData
            ) {
                if (!myStockViewModel.saveMyStock(mContext, isInsertMode, id, myStockInputDialogData)) {
                    Toasty.error(mContext, mContext.getString(R.string.Error_Msg_Normal), Toasty.LENGTH_SHORT).show()
                    return
                }
                dialog.dismiss()
            }
        }).show()
    }

    @Preview
    @Composable
    private fun StockListComposable() {
        LazyColumn {
            items(5000) {
                KotlinWorldCard(order = it)
            }
        }
    }
    @Composable
    private fun StockListItem() {

    }
    @Composable
    private fun KotlinWorldCard(order: Int) {
        Card(
            Modifier
                .padding(12.dp)
                .border(width = 4.dp, color = Color.Black)
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("Kotlin World $order")
            }
        }
    }
}