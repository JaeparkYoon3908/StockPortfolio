package com.yjpapp.stockportfolio.function.mystock

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.FrameMetricsAggregator.ANIMATION_DURATION
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockInputDialog
import dagger.hilt.android.AndroidEntryPoint
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe
import es.dmoral.toasty.Toasty
import kotlin.math.roundToInt

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
//            myStockViewModel.onViewCreated()
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
                    inputDialogPurchasePrice = ""
                    inputDialogPurchaseCount = ""
                }
//                showInputDialog(true, 0)
            }
        }
        return super.onOptionsItemSelected(item)
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

    /**
     * Compose 영역
     */
    @Preview
    @Composable
    private fun SimpleComposable() {
        Column {
            Text(
                text = "Hello world.",
                modifier = Modifier.clickable {
                    Toasty.normal(requireContext(), "Test!!").show()
                }
            )

            Text(
                text = "Alan walker",
                modifier = Modifier.clickable {
                    Toasty.normal(requireContext(), "Test!!").show()
                }
            )

            Text(
                text = "All for down.",
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

    @Preview
    @Composable
    private fun StockListComposable() {
        LazyColumn {
            items(10) {
                StockListItem(order = it)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun StockListItem(order: Int) {
        var isOpenRevealSwipe = false
        val maxRevealDp = 120.dp
        RevealSwipe(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            maxRevealDp = maxRevealDp,
            backgroundCardEndColor = colorResource(id = R.color.color_background_fbfbfb),
            animateBackgroundCardColor = false,
            directions = setOf(
//        RevealDirection.StartToEnd,
                RevealDirection.EndToStart
            ),
//            hiddenContentStart = {
//                Icon(
//                    modifier = Modifier.padding(horizontal = 25.dp),
//                    imageVector = Icons.Outlined.Star,
//                    contentDescription = null,
//                    tint = Color.White
//                )
//            },
            hiddenContentEnd = {
                Column(
                    modifier = Modifier
                        .width(maxRevealDp)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "편집",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.color_ffffff),
                        modifier = Modifier
                            .clickable {  }
                            .fillMaxWidth()
                            .background(color = colorResource(id = R.color.color_80000000))
                    )

                    Text(
                        text = "매도",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.color_ffffff),
                        modifier = Modifier
                            .clickable {  }
                            .fillMaxWidth()
                            .background(color = colorResource(id = R.color.color_4876c7))

                    )

                    Text(
                        text = "삭제",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.color_ffffff),
                        modifier = Modifier
                            .clickable {  }
                            .fillMaxWidth()
                            .background(color = colorResource(id = R.color.color_cd4632))
                    )
                }
            }
        ) {
            Card( modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
                elevation = 0.dp,
                backgroundColor = colorResource(id = R.color.color_background_fbfbfb)
            ){
                Column(
                    modifier = Modifier
//                    .padding(bottom = 10.dp)
                        .wrapContentHeight()
//                    .fillMaxWidth()
//                    .background(color = colorResource(id = R.color.color_background_fbfbfb))
                ) {

                    Row(Modifier
                        .padding(start = 15.dp, end = 15.dp)
                    ) {
                        Text(
                            text = "회사 이름",
                            fontSize = 16.sp,
                            maxLines = 1,
                            color = colorResource(id = R.color.color_222222),
                            modifier = Modifier
                                .weight(0.55f)
                                .padding(top = 10.dp)
                        )
                        Row(
                            modifier = Modifier
                                .weight(0.45f)
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.MyStockFragment_Gain),
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                            )
                            //수익
                            Text(
                                text = "700",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_666666),
                                modifier = Modifier
                                    .padding(start = 5.dp)
                            )
                            //수익 퍼센트
                            Text(
                                text = "(0.93)",
                                fontSize = 12.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                                modifier = Modifier
                                    .padding(start = 5.dp)
                            )
                        }
                    }

                    Divider(
                        Modifier
                            .padding(10.dp)
                    )
                    Row(
                        Modifier
                            .padding(start = 15.dp, end = 15.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(0.5f)
                        ) {
                            Text(
                                text = "매수일",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_666666)
                            )

                            Text(
                                text = "2021-03-29",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .weight(0.5f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "평단가",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_666666),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )

                            Text(
                                text = "2,500",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 15.dp, top = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Text(
                                text = "매수수량",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_666666)
                            )

                            Text(
                                text = "270",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "현재가",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_666666),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )

                            Text(
                                text = "2,600",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}