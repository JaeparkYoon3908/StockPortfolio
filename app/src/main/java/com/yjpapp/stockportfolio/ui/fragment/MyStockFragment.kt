package com.yjpapp.stockportfolio.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.MyStockInfo
import com.yjpapp.stockportfolio.databinding.FragmentMyStockBinding
import com.yjpapp.stockportfolio.ui.adapter.MyStockListAdapter
import com.yjpapp.stockportfolio.ui.dialog.MyStockFilterDialog
import com.yjpapp.stockportfolio.ui.dialog.MyStockInputDialog
import com.yjpapp.stockportfolio.ui.presenter.MyStockPresenter
import com.yjpapp.stockportfolio.ui.view.MyStockView
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.dialog_input_my_stock.*


/**
 * 나의 주식 화면
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

//TODO 내가 갖고있는 주식 실시간 변동 사항 및 수익 분석 할 수 있는 기능 만들기.
class MyStockFragment: Fragment(), MyStockView {
    private lateinit var mContext: Context
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var allMyStockList: ArrayList<MyStockInfo>
    private lateinit var myStockPresenter: MyStockPresenter

    private var _viewBinding: FragmentMyStockBinding? = null
    private val viewBinding get() = _viewBinding!!
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myStockPresenter = MyStockPresenter(this)
        myStockPresenter.onAttach(context)
        mContext = context
        //Fragment BackPress Event Call
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View{
        _viewBinding = FragmentMyStockBinding.inflate(inflater, container, false)

        initData()
        initLayout()

        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        myStockPresenter.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        myStockPresenter.onDestroy()
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
                myStockPresenter.onAddButtonClick()
                showInputDialog(false, null)
            }

            R.id.menu_MyStockFragment_Edit -> {
                myStockPresenter.onMenuEditButtonClick()
//                startService()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initData(){
        allMyStockList = ArrayList()
//        val symbol = "005930.KS"
//        val region = "KR"
//        YahooFinanceProtocolManager.getInstance(mContext).getStockProfile(symbol, region,
//            object: Callback<JsonObject?> {
//            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
//                if(response.code() == 200 || response.code() == 204){
//                    try {
//                        val jsonObject = JSONObject(response.body().toString())
//                        val priceJSONArray = jsonObject.getJSONObject("price").toString()
//                        val price: Price = Gson().fromJson(priceJSONArray, Price::class.java)
//                        Log.d("YJP", "price = $price")
//
//                        Thread(Runnable {
//                            // performing some dummy time taking operation
//                            activity?.runOnUiThread {
//                                val currentPrice = price.regularMarketPrice.raw.toString()
//                                val change = (price.regularMarketPrice.raw.toDouble() - price.regularMarketPreviousClose.raw.toDouble()) //변동 가격
//                                val changePercent = price.regularMarketChangePercent.fmt
//
//                            }
//                        }).start()
//
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
//                Log.d("RequestResult", "RetrofitExample, Type : get, Result : onFailure, Error Message : " + t.message)
//            }
//        })
    }

    private fun initLayout(){
        setHasOptionsMenu(true)
        viewBinding.apply {
//            txtIncomeNoteFragmentFilter.setOnClickListener {
//                showFilterDialog()
//            }
        }
        initRecyclerView()
    }
    private fun initRecyclerView(){
        layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        viewBinding.apply {
            recyclerviewMyStockFragment.layoutManager = layoutManager
            val myStockInfo = MyStockInfo(0, "코엔텍", "1500", "2020.12.23", "15%", "1000", "1150", 10)
            for(i in 0 until 10){
                allMyStockList.add(myStockInfo)
            }

            recyclerviewMyStockFragment.itemAnimator = FadeInAnimator()
        }
    }

    override fun addButtonClick() {
        myStockPresenter.onAddButtonClick()
    }

    override fun showInputDialog(editMode: Boolean, myStockInfo: MyStockInfo?) {
        MyStockInputDialog(mContext, myStockPresenter).apply {
            if(editMode){
                if(!isShowing){
                    show()
                    et_subject_name.setText(myStockInfo?.subjectName)
                    et_purchase_date.setText(myStockInfo?.purchaseDate)
                    et_purchase_price.setText(myStockInfo?.purchasePrice)
                    et_purchase_count.setText(myStockInfo?.purchaseCount.toString())
                }
            }else{
                if(!isShowing) {
                    show()
                }
            }
        }

    }

    override fun showAddButton() {
        menu?.findItem(R.id.menu_MyStockFragment_Add)?.isVisible = true
    }

    override fun hideAddButton() {
        menu?.findItem(R.id.menu_MyStockFragment_Add)?.isVisible = false
    }

    override fun showEditButton() {
        menu?.findItem(R.id.menu_MyStockFragment_Edit)?.isVisible = true
    }

    override fun hideEditButton() {
        menu?.findItem(R.id.menu_MyStockFragment_Edit)?.isVisible = false
    }

    override fun setAdapter(myStockListAdapter: MyStockListAdapter) {
        viewBinding.apply {
            recyclerviewMyStockFragment.adapter = myStockListAdapter
        }
    }

    override fun scrollTopPosition(topPosition: Int) {
        viewBinding.apply {
            recyclerviewMyStockFragment.scrollToPosition(topPosition)
        }
    }

    override fun bindTotalGainData() {

    }

    override fun changeFilterText(text: String) {
//        viewBinding.apply {
//            txtIncomeNoteFragmentFilter.text = text
//        }
    }

    override fun showFilterDialog() {
        val myStockFilterDialog = MyStockFilterDialog(myStockPresenter)
        myStockFilterDialog.show(childFragmentManager, tag)
    }
}