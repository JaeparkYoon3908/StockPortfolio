package com.yjpapp.stockportfolio.function.mystock

import android.os.Bundle
import android.view.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import es.dmoral.toasty.Toasty
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.dialog.CommonDatePickerDialog
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockInputDialog
import org.koin.android.ext.android.inject

class MyStockComposeFragment : Fragment() {
    private val myStockViewModel: MyStockViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            myStockViewModel.onViewCreated()
            setHasOptionsMenu(true)
            setContent {
                TotalPriceComposable()
//                SimpleComposable()
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
                showInputDialog(true, 0)
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
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row (
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))

            ){
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
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row (
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ){
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
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row (
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ){
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
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }
        }
    }

    private fun showInputDialog(isInsertMode: Boolean, id: Int) {
        MyStockInputDialog.getInstance(requireContext()).apply {
            myStockViewModel.inputDialogController = this
            binding.apply {
                viewModel = myStockViewModel
                etSubjectName.setOnClickListener {
//                    val intent = Intent(mContext, StockSearchActivity::class.java)
//                    startActivityForResult(intent, 10000)
                }
                etPurchaseDate.setOnClickListener {
                    var year = ""
                    var month = ""
                    if (binding.etPurchaseDate.text.toString() != "") {
                        val split = binding.etPurchaseDate.text.toString().split(".")
                        year = split[0]
                        month = split[1]
                    }
                    //매수 날짜 선택 다이얼로그 show
                    CommonDatePickerDialog(requireContext(), year, month).apply {
                        setListener { view, year, month, dayOfMonth ->
                            uiHandler.sendEmptyMessage(MyStockInputDialog.MSG.SELL_DATE_DATA_INPUT)
                            purchaseYear = year.toString()
                            purchaseMonth = if (month < 10) {
                                "0$month"
                            } else {
                                month.toString()
                            }
                            purchaseDay = if (dayOfMonth < 10) {
                                "0$dayOfMonth"
                            } else {
                                dayOfMonth.toString()
                            }
                            myStockViewModel.inputDialogPurchaseDate =
                                "$purchaseYear.$purchaseMonth"
                        }
                        show()
                    }
                }
                txtCancel.setOnClickListener { dismiss() }
                txtComplete.setOnClickListener {
                    if (myStockViewModel.saveMyStock(isInsertMode, id)) {
                        dismiss()
                    }
                }

                //observer
                myStockViewModel.inputDialogPurchasePrice.observe(this@MyStockComposeFragment, {
                    etPurchasePrice.setText(it)
                    etPurchasePrice.setSelection(it.length)
                })

            }
            show()
        }
    }
}