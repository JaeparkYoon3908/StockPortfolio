package com.yjpapp.stockportfolio.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.MyStockInfo
import com.yjpapp.stockportfolio.network.YahooFinanceProtocolManager
import com.yjpapp.stockportfolio.network.data.Price
import com.yjpapp.stockportfolio.ui.adapter.MyStockListAdapter
import com.yjpapp.stockportfolio.ui.dialog.MyStockInputDialog
import com.yjpapp.stockportfolio.ui.presenter.MyStockPresenter
import com.yjpapp.stockportfolio.ui.view.MyStockView
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.dialog_input_my_stock.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//TODO 내가 갖고있는 주식 실시간 변동 사항 및 수익 분석 할 수 있는 기능 만들기.
class MyStockFragment: Fragment(), MyStockView {
    private lateinit var mContext: Context
    private lateinit var mRootView: View
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private lateinit var txt_current_price_data: TextView
    private lateinit var txt_current_price_change_data: TextView
    private lateinit var txt_current_price_change_percent_data: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var myStockListAdapter: MyStockListAdapter
    private lateinit var allMyStockList: ArrayList<MyStockInfo>
    private lateinit var myStockPresenter: MyStockPresenter
    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        mRootView = inflater.inflate(R.layout.fragment_my_stock, container, false)
        initData()
        initLayout()
        return mRootView
    }

    override fun onResume() {
        super.onResume()
        myStockPresenter.onResume()
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

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initData(){
        myStockPresenter = MyStockPresenter(mContext, this)
        allMyStockList = ArrayList()
        val symbol = "005930.KS"
        val region = "KR"
        YahooFinanceProtocolManager.getInstance(mContext).getStockProfile(symbol, region,
            object: Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                if(response.code() == 200 || response.code() == 204){
                    try {
                        val jsonObject = JSONObject(response.body().toString())
                        val priceJSONArray = jsonObject.getJSONObject("price").toString()
                        val price: Price = Gson().fromJson(priceJSONArray, Price::class.java)
                        Log.d("YJP", "price = $price")

                        Thread(Runnable {
                            // performing some dummy time taking operation
                            activity?.runOnUiThread {
                                val currentPrice = price.regularMarketPrice.raw.toString()
                                val change = (price.regularMarketPrice.raw.toDouble() - price.regularMarketPreviousClose.raw.toDouble()) //변동 가격
                                val changePercent = price.regularMarketChangePercent.fmt
//                                txt_current_price_data.text = currentPrice
//                                if(change<0){
//                                    txt_current_price_change_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
//                                }else{
//                                    txt_current_price_change_data.setTextColor(mContext.getColor(R.color.green))
//                                }
//                                txt_current_price_change_data.text = "$change ($changePercent)"
//                                txt_current_price_change_percent_data.text = changePercent //주가 변동 퍼센트
                            }
                        }).start()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Log.d("RequestResult", "RetrofitExample, Type : get, Result : onFailure, Error Message : " + t.message)
            }
        })
    }

    private fun initLayout(){
        setHasOptionsMenu(true)
//        txt_current_price_data = mRootView.findViewById(R.id.txt_current_price_data)
//        txt_current_price_change_percent_data = mRootView.findViewById(R.id.txt_current_price_change_percent_data)
        initRecyclerView()
    }
    private fun initRecyclerView(){
        recyclerView = mRootView.findViewById(R.id.recyclerview_MyStockFragment)
        layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        recyclerView.layoutManager = layoutManager
        val myStockInfo = MyStockInfo(0, "코엔텍", "1500", "2020.12.23", "15%", "1000", "1150", "10")
        for(i in 0 until 10){
            allMyStockList.add(myStockInfo)
        }

//        myStockListAdapter = MyStockListAdapter(allMyStockList, myStockPresenter)
//        recyclerView.adapter = myStockListAdapter
        recyclerView.itemAnimator = FadeInAnimator()
    }

    override fun addButtonClick() {
        TODO("Not yet implemented")
    }

    override fun showInputDialog(editMode: Boolean, myStockInfo: MyStockInfo?) {
        MyStockInputDialog(mContext, myStockPresenter).apply {
            if(editMode){
                if(!isShowing){
                    show()
                    et_subject_name.setText(myStockInfo?.subjectName)
                    et_purchase_date.setText(myStockInfo?.purchaseDate)
                    et_purchase_price.setText(myStockInfo?.purchasePrice)
                    et_purchase_count.setText(myStockInfo?.holdingQuantity.toString())
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
        recyclerView.adapter = myStockListAdapter
    }


}