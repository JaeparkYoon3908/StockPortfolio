package com.yjpapp.stockportfolio.function.mystock

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.common.dialog.CommonTwoBtnDialog
import com.yjpapp.stockportfolio.common.theme.*
import com.yjpapp.stockportfolio.extension.repeatOnStarted
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockPurchaseInputDialog
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockSellInputDialog
import com.yjpapp.stockportfolio.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.model.SubjectName
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import com.yjpapp.stockportfolio.util.StockUtils
import dagger.hilt.android.AndroidEntryPoint
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe
import de.charlex.compose.rememberRevealState
import de.charlex.compose.reset
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 1.5 신규버전 업데이트 기능 (나의 주식)
 * @author Yoon Jae-park
 * @since 2022.02
 */
@AndroidEntryPoint
class MyStockFragment : Fragment() {
    private val TAG = MyStockFragment::class.java.simpleName
    private val myStockViewModel: MyStockViewModel by viewModels()
    private lateinit var mContext: Context
    private lateinit var navController: NavController

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
            setHasOptionsMenu(true)
            setContent {
                Column {
                    TotalPriceComposable()
                    StockListComposable()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            repeatOnStarted {
                myStockViewModel.uiState.collect { event -> handleEvent(event) }
            }
        }
        navController = Navigation.findNavController(view)
    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_my_stock, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_MyStockFragment_Refresh -> {
                if (myStockViewModel.myStockInfoList.isEmpty()) {
                    Toasty.warning(
                        requireContext(),
                        getString(R.string.MyStockFragment_Msg_Current_Price_Refresh_Warning)
                    ).show()
                    return false
                }

                if (!myStockViewModel.isCurrentPriceRefreshing) {
                    myStockViewModel.refreshAllPrices()
                    myStockViewModel.isCurrentPriceRefreshing = true
                }
            }
            R.id.menu_MyStockFragment_Add -> {
                if (myStockViewModel.myStockInfoList.size >= 10) {
                    Toasty.info(
                        mContext,
                        getString(
                            R.string.MyStockFragment_Notice_Max_List
                        ),
                        Toasty.LENGTH_LONG
                    ).show()
                    return false
                }
                showPurchaseInputDialog(null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleEvent(event: MyStockViewModel.Event) {
        when (event) {
            is MyStockViewModel.Event.InitUIState -> {}
            is MyStockViewModel.Event.ShowInfoToastMessage -> {
                Toasty.info(mContext, event.msg, Toasty.LENGTH_LONG).show()
            }
            is MyStockViewModel.Event.ShowErrorToastMessage -> {
                Toasty.error(mContext, event.msg, Toasty.LENGTH_LONG).show()
            }
            is MyStockViewModel.Event.SuccessIncomeNoteAdd -> {
                myStockViewModel.deleteMyStock(
                    context = requireContext(),
                    myStockEntity = event.data
                )
            }
            is MyStockViewModel.Event.RefreshCurrentPriceDone -> {
                if (event.isSuccess)
                    Toasty.success(requireContext(), getString(R.string.MyStockFragment_Msg_Current_Price_Refresh_Success)).show()
                else
                    Toasty.error(requireContext(), getString(R.string.MyStockFragment_Msg_Current_Price_Refresh_Error)).show()
                myStockViewModel.isCurrentPriceRefreshing = false
            }
            is MyStockViewModel.Event.ResponseServerError -> {
                Toasty.error(requireContext(), event.msg, Toasty.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    private fun showPurchaseInputDialog(
        dialogData: MyStockPurchaseInputDialog.MyStockPurchaseInputDialogData?
    ) {
        MyStockPurchaseInputDialog(
            mContext = mContext,
            dialogData = dialogData,
            callBack = object : MyStockPurchaseInputDialog.CallBack {
                override fun onInputDialogCompleteClicked(
                    dialog: MyStockPurchaseInputDialog,
                    userInputDialogData: MyStockPurchaseInputDialog.MyStockPurchaseInputDialogData
                ) {
                    lifecycleScope.launch {
                        val currentPriceData = myStockViewModel.getCurrentPrice(userInputDialogData.subjectName.code)
                        currentPriceData.run {
                            if (currentPrice.isEmpty() || yesterdayPrice.isEmpty()) {
                                ResponseAlertManger.showErrorAlert(mContext, getString(R.string.Error_Msg_Network_Connect_Exception))
                                return@launch
                            }
                        }
                        val currentPrice = currentPriceData.currentPrice
                        val currentPriceNumber = StockUtils.getNumDeletedComma(currentPrice).toInt()
                        val purchasePriceNumber = StockUtils.getNumDeletedComma(userInputDialogData.purchasePrice).toInt()
                        val purchaseCountNumber = userInputDialogData.purchaseCount.toInt()
                        val gainPrice = (currentPriceNumber - purchasePriceNumber) * purchaseCountNumber
                        val myStockEntity = MyStockEntity(
                            id = dialogData?.id?: 0,
                            subjectName = userInputDialogData.subjectName.text,
                            subjectCode = userInputDialogData.subjectName.code,
                            gainPrice = StockUtils.getNumInsertComma(gainPrice.toString()),
                            purchaseDate = userInputDialogData.purchaseDate,
                            purchasePrice = userInputDialogData.purchasePrice,
                            purchaseCount = userInputDialogData.purchaseCount.toInt(),
                            currentPrice = currentPrice,
                            dayToDayPrice = currentPriceData.dayToDayPrice,
                            dayToDayPercent = currentPriceData.dayToDayPercent,
                            yesterdayPrice = currentPriceData.yesterdayPrice
                        )
                        if (dialogData == null) {
                            val isAddSuccess = myStockViewModel.addMyStock(mContext, myStockEntity)
                            if (isAddSuccess) {
                                dialog.dismiss()
                            }
                        }
                        if (dialogData != null) {
                            val isUpdateSuccess = myStockViewModel.updateMyStock(mContext, myStockEntity)
                            if (isUpdateSuccess) {
                                dialog.dismiss()
                            }
                        }
                    }
                }
            }).show(childFragmentManager, "MyStockInputDialog")
    }

    private fun showSellInputDialog(myStockEntity: MyStockEntity) {
        MyStockSellInputDialog(
            mContext = requireContext(),
            dialogData = MyStockSellInputDialog.MyStockSellInputDialogData(
                myStockEntity = myStockEntity
            ),
            callBack = object : MyStockSellInputDialog.CallBack {
                override fun onInputDialogCompleteClicked(
                    dialog: MyStockSellInputDialog,
                    userInputDialogData: MyStockSellInputDialog.MyStockSellInputDialogData
                ) {
                    myStockViewModel.requestAddIncomeNote(
                        context = requireContext(),
                        reqIncomeNoteInfo = ReqIncomeNoteInfo(
                            subjectName = myStockEntity.subjectName,
                            sellDate = userInputDialogData.sellDate,
                            purchasePrice = StockUtils.getNumDeletedComma(myStockEntity.purchasePrice).toDouble(),
                            sellPrice = StockUtils.getNumDeletedComma(userInputDialogData.sellPrice).toDouble(),
                            sellCount = userInputDialogData.sellCount.toInt()
                        ),
                        myStockEntity = myStockEntity
                    )
                    dialog.dismiss()
                }

            }
        ).show(requireActivity().supportFragmentManager, TAG)
    }
    /**
     * Compose 영역
     */
    @Preview
    @Composable
    private fun TotalPriceComposable() {
        val totalPurchasePrice by myStockViewModel.totalPurchasePrice.collectAsState()
        val totalEvaluationAmount by myStockViewModel.totalEvaluationAmount.collectAsState()
        val totalGainPrice by myStockViewModel.totalGainPrice.collectAsState()
        val totalGainPricePercent by myStockViewModel.totalGainPricePercent.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.common_15dp),
                    end = dimensionResource(id = R.dimen.common_15dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ) {
                Text(
                    text = getString(R.string.MyStockFragment_Total_Purchase_Price),
                    color = Color_222222,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = StockUtils.getPriceNum(totalPurchasePrice),
                    color = Color_222222,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))

            ) {
                Text(
                    text = getString(R.string.MyStockFragment_Total_Evaluation_Amount),
                    color = Color_222222,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = StockUtils.getPriceNum(totalEvaluationAmount),
                    color = Color_222222,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ) {
                Text(
                    text = getString(R.string.Common_gains_losses),
                    color = Color_222222,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = StockUtils.getPriceNum(totalGainPrice),
                    color = when {
                        StockUtils.getNumDeletedComma(totalGainPrice).toDouble() > 0 -> Color_CD4632
                        StockUtils.getNumDeletedComma(totalGainPrice).toDouble() < 0 -> Color_4876C7
                        else -> Color_CD4632
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ) {
                Text(
                    text = getString(R.string.Common_GainPercent),
                    color = Color_222222,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = totalGainPricePercent,
                    color = when {
                        StockUtils.getNumDeletedComma(totalGainPrice).toDouble() > 0 -> Color_CD4632
                        StockUtils.getNumDeletedComma(totalGainPrice).toDouble() < 0 -> Color_4876C7
                        else -> Color_CD4632
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }
            Divider(
                color = Color_Line_1A000000,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            )
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition", "UnsafeRepeatOnLifecycleDetector")
    @Preview
    @Composable
    private fun StockListComposable() {
        val coroutineScope = rememberCoroutineScope()
        Scaffold { paddingValues ->
            val listState = rememberLazyListState()
            LazyColumn(
                reverseLayout = true,
                state = listState,
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                items(
                    count = myStockViewModel.myStockInfoList.size
                ) {
                    StockListItem(
                        position = it,
                        myStockEntity = myStockViewModel.myStockInfoList[it]
                    )
                }
            }
            coroutineScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    myStockViewModel.scrollIndex.collect { position ->
                        listState.scrollToItem(position)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun StockListItem(
        position: Int,
        myStockEntity: MyStockEntity
    ) {
        val revealSwipeState = rememberRevealState()
        val coroutineScope = rememberCoroutineScope()
        val maxRevealDp = 110.dp
        RevealSwipe(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            maxRevealDp = maxRevealDp,
            backgroundCardEndColor = Color_FBFBFB,
            animateBackgroundCardColor = false,
            state = revealSwipeState,
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
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Center,
                        modifier = Modifier
                            .clickable {
                                val dialogData =
                                    MyStockPurchaseInputDialog.MyStockPurchaseInputDialogData(
                                        id = myStockEntity.id,
                                        subjectName = SubjectName(
                                            text = myStockEntity.subjectName,
                                            code = myStockEntity.subjectCode
                                        ),
                                        purchaseDate = myStockEntity.purchaseDate,
                                        purchasePrice = myStockEntity.purchasePrice,
                                        purchaseCount = myStockEntity.purchaseCount.toString()
                                    )
                                showPurchaseInputDialog(dialogData)
                                coroutineScope.launch {
                                    revealSwipeState.reset()
                                }
                            }
                            .fillMaxWidth()
                            .weight(0.333f)
                            .background(color = Color_80000000)
                    ) {
                        Text(
                            text = getString(R.string.Common_Edit),
                            fontSize = 16.sp,
                            color = Color_FFFFFF
                        )
                    }
                    Box(
                        contentAlignment = Center,
                        modifier = Modifier
                            .clickable {
                                showSellInputDialog(myStockEntity)
                                coroutineScope.launch {
                                    revealSwipeState.reset()
                                }
                            }
                            .fillMaxWidth()
                            .weight(0.333f)
                            .background(color = Color_4876C7)
                    ) {
                        Text(
                            text = getString(R.string.Common_Sell),
                            fontSize = 16.sp,
                            color = Color_FFFFFF
                        )
                    }
                    Box(
                        contentAlignment = Center,
                        modifier = Modifier
                            .clickable {
                                if (!myStockViewModel.isDeleteCheck()) {
                                    myStockViewModel.deleteMyStock(
                                        context = requireContext(),
                                        myStockEntity = myStockEntity
                                    )
                                    return@clickable
                                }
                                CommonTwoBtnDialog(
                                    mContext = mContext,
                                    CommonTwoBtnDialog.CommonTwoBtnData(
                                        noticeText = getString(R.string.Common_Notice_Delete_Check),
                                        leftBtnText = getString(R.string.Common_Cancel),
                                        leftBtnListener = { _: View, dialog: CommonTwoBtnDialog ->
                                            dialog.dismiss()
                                        },
                                        rightBtnText = getString(R.string.Common_Ok),
                                        rightBtnListener = { _: View, dialog: CommonTwoBtnDialog ->
                                            myStockViewModel.deleteMyStock(
                                                context = requireContext(),
                                                myStockEntity = myStockEntity
                                            )
                                            dialog.dismiss()
                                        }
                                    )
                                ).show()
                                coroutineScope.launch {
                                    revealSwipeState.reset()
                                }
                            }
                            .fillMaxWidth()
                            .weight(0.333f)
                            .background(color = Color_CD4632)
                    ) {
                        Text(
                            text = getString(R.string.Common_Delete),
                            fontSize = 16.sp,
                            color = Color_FFFFFF
                        )
                    }
                }
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
                elevation = 0.dp,
                backgroundColor = Color_FBFBFB
            ) {
                val purchasePriceNumber = StockUtils.getNumDeletedComma(myStockEntity.purchasePrice).toDouble()
                val currentPriceNumber = StockUtils.getNumDeletedComma(myStockEntity.currentPrice).toDouble()
                val gainPriceNumber = StockUtils.getNumDeletedComma(myStockEntity.gainPrice).toDouble()
                val yesterdayPriceNumber = StockUtils.getNumDeletedComma(myStockEntity.yesterdayPrice).toDouble()
                Column(
                    modifier = Modifier
//                    .padding(bottom = 10.dp)
                        .wrapContentHeight()
                ) {

                    Row(
                        Modifier
                            .padding(start = 15.dp, end = 15.dp)
                    ) {
                        Text(
                            text = myStockEntity.subjectName,
                            fontSize = 16.sp,
                            maxLines = 1,
                            color = Color_222222,
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
                                color = Color_222222,
                            )
                            //수익
                            Text(
                                text = StockUtils.getPriceNum(myStockEntity.gainPrice),
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = when {
                                    gainPriceNumber > 0 -> Color_CD4632
                                    gainPriceNumber < 0 -> Color_4876C7
                                    else -> Color_222222
                                },
                                modifier = Modifier
                                    .padding(start = 5.dp)
                            )

                            //수익 퍼센트
                            val allPurchasePriceNum = (purchasePriceNumber * myStockEntity.purchaseCount)
                            val allCurrentPriceNum = (currentPriceNumber * myStockEntity.purchaseCount)
                            val gainPercentNum = StockUtils.calculateGainPercent(allPurchasePriceNum, allCurrentPriceNum)
                            Text(
                                text = "(${StockUtils.getRoundsPercentNumber(gainPercentNum)})",
                                fontSize = 12.sp,
                                maxLines = 1,
                                color = when {
                                    gainPriceNumber > 0 -> Color_CD4632
                                    gainPriceNumber < 0 -> Color_4876C7
                                    else -> Color_222222
                                },
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
                            //매수일
                            Text(
                                text = getString(R.string.MyStockFragment_Purchase_Date),
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = Color_666666
                            )

                            Text(
                                text = myStockEntity.purchaseDate,
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = Color_222222,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .weight(0.5f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            //평균단가
                            Text(
                                text = getString(R.string.MyStockFragment_Purchase_Average),
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = Color_666666,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )

                            Text(
                                text = StockUtils.getPriceNum(myStockEntity.purchasePrice),
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = Color_222222,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.weight(0.35f)
                        ) {
                            Text(
                                text = getString(R.string.MyStockFragment_Holding_Quantity),
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_666666)
                            )

                            Text(
                                text = myStockEntity.purchaseCount.toString(),
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = colorResource(id = R.color.color_222222),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .weight(0.65f),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = getString(R.string.MyStockFragment_Current_Price),
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = Color_666666,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )

                            Text(
                                text = "${StockConfig.koreaMoneySymbol}${myStockEntity.currentPrice}",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = Color_222222,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )

                            Text(
                                text = StockUtils.getDayToDayPrice(
                                    currentPrice = currentPriceNumber,
                                    yesterdayPrice = yesterdayPriceNumber
                                ),
                                fontSize = 11.sp,
                                maxLines = 1,
                                color = when {
                                    currentPriceNumber - yesterdayPriceNumber > 0 -> Color_CD4632
                                    currentPriceNumber == yesterdayPriceNumber -> Color_222222
                                    else -> Color_4876C7
                                },
                                modifier = Modifier
                                    .padding(start = 5.dp)

                            )

                            Text(
                                text = "(${myStockEntity.dayToDayPercent}%)",
                                fontSize = 11.sp,
                                color = when {
                                    currentPriceNumber - yesterdayPriceNumber > 0 -> Color_CD4632
                                    currentPriceNumber == yesterdayPriceNumber -> Color_222222
                                    else -> Color_4876C7
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MarqueeText(
        text: String,
        modifier: Modifier = Modifier,
        gradientEdgeColor: Color = Color.White,
        color: Color = Color.Unspecified,
        fontSize: TextUnit = TextUnit.Unspecified,
        fontStyle: FontStyle? = null,
        fontWeight: FontWeight? = null,
        fontFamily: FontFamily? = null,
        letterSpacing: TextUnit = TextUnit.Unspecified,
        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        lineHeight: TextUnit = TextUnit.Unspecified,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        style: TextStyle = LocalTextStyle.current,
    ) {
        val createText = @Composable { localModifier: Modifier ->
            Text(
                text,
                textAlign = textAlign,
                modifier = localModifier,
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                lineHeight = lineHeight,
                overflow = overflow,
                softWrap = softWrap,
                maxLines = 1,
                onTextLayout = onTextLayout,
                style = style,
            )
        }
        var offset by remember { mutableStateOf(0) }
        val textLayoutInfoState = remember { mutableStateOf<TextLayoutInfo?>(null) }
        LaunchedEffect(textLayoutInfoState.value) {
            val textLayoutInfo = textLayoutInfoState.value ?: return@LaunchedEffect
            if (textLayoutInfo.textWidth <= textLayoutInfo.containerWidth) return@LaunchedEffect
            val duration = 2500 * textLayoutInfo.textWidth / textLayoutInfo.containerWidth
            val delay = 1000L

            do {
                val animation = TargetBasedAnimation(
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = duration,
                            delayMillis = 1000,
                            easing = LinearEasing,
                        ),
                        repeatMode = RepeatMode.Restart
                    ),
                    typeConverter = Int.VectorConverter,
                    initialValue = 0,
                    targetValue = -textLayoutInfo.textWidth
                )
                val startTime = withFrameNanos { it }
                do {
                    val playTime = withFrameNanos { it } - startTime
                    offset = (animation.getValueFromNanos(playTime))
                } while (!animation.isFinishedFromNanos(playTime))
                delay(delay)
            } while (true)
        }

        SubcomposeLayout(
            modifier = modifier.clipToBounds()
        ) { constraints ->
            val infiniteWidthConstraints = constraints.copy(maxWidth = Int.MAX_VALUE)
            var mainText = subcompose(MarqueeLayers.MainText) {
                createText(Modifier)
            }.first().measure(infiniteWidthConstraints)

            var gradient: Placeable? = null

            var secondPlaceableWithOffset: Pair<Placeable, Int>? = null
            if (mainText.width <= constraints.maxWidth) {
                mainText = subcompose(MarqueeLayers.SecondaryText) {
                    createText(Modifier.fillMaxWidth())
                }.first().measure(constraints)
                textLayoutInfoState.value = null
            } else {
                val spacing = constraints.maxWidth * 2 / 3
                textLayoutInfoState.value = TextLayoutInfo(
                    textWidth = mainText.width + spacing,
                    containerWidth = constraints.maxWidth
                )
                val secondTextOffset = mainText.width + offset + spacing
                val secondTextSpace = constraints.maxWidth - secondTextOffset
                if (secondTextSpace > 0) {
                    secondPlaceableWithOffset = subcompose(MarqueeLayers.SecondaryText) {
                        createText(Modifier)
                    }.first().measure(infiniteWidthConstraints) to secondTextOffset
                }
                gradient = subcompose(MarqueeLayers.EdgesGradient) {
                    Row {
                        GradientEdge(gradientEdgeColor, Color.Transparent)
                        Spacer(Modifier.weight(1f))
                        GradientEdge(Color.Transparent, gradientEdgeColor)
                    }
                }.first().measure(constraints.copy(maxHeight = mainText.height))
            }

            layout(
                width = constraints.maxWidth,
                height = mainText.height
            ) {
                mainText.place(offset, 0)
                secondPlaceableWithOffset?.let {
                    it.first.place(it.second, 0)
                }
                gradient?.place(0, 0)
            }
        }
    }

    @Composable
    private fun GradientEdge(
        startColor: Color, endColor: Color,
    ) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .fillMaxHeight()
                .background(
                    brush = Brush.horizontalGradient(
                        0f to startColor, 1f to endColor,
                    )
                )
        )
    }

    private enum class MarqueeLayers { MainText, SecondaryText, EdgesGradient }
    private data class TextLayoutInfo(val textWidth: Int, val containerWidth: Int)
}