package com.yjpapp.data.repository

import com.yjpapp.data.APICall
import com.yjpapp.data.datasource.MyStockRoomDataSource
import com.yjpapp.data.mapper.mapping
import com.yjpapp.data.model.MyStockData
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqKoreaStockPriceInfo
import com.yjpapp.data.model.response.StockPriceData
import com.yjpapp.data.model.response.UsaStockSymbolData
import com.yjpapp.network.datasource.AlphaVantageDataSource
import com.yjpapp.network.datasource.DataPortalDataSource
import com.yjpapp.network.model.StockPriceInfo
import retrofit2.HttpException
import java.text.DecimalFormat
import javax.inject.Inject

interface MyStockRepository {
    suspend fun addMyStock(myStockData: MyStockData)
    suspend fun updateMyStock(myStockData: MyStockData): Boolean
    suspend fun deleteMyStock(myStockData: MyStockData)
    //type 1: 한국 주식, 2: 미국 주식
    suspend fun getAllMyStock(): ResponseResult<List<MyStockData>>
    suspend fun getKoreaStockPriceInfo(request: ReqKoreaStockPriceInfo): ResponseResult<List<StockPriceData>>
    suspend fun getUsaStockSymbol(keywords: String): ResponseResult<List<UsaStockSymbolData>>
    suspend fun getUsaStockInfo(symbol: String): ResponseResult<StockPriceData>
    suspend fun refreshMyStock(type: Int): Boolean
}

class MyStockRepositoryImpl @Inject constructor(
    private val myStockLocalDataSource: MyStockRoomDataSource,
    private val stockInfoDataSource: DataPortalDataSource,
    private val alphaVantageDataSource: AlphaVantageDataSource
) : MyStockRepository {
    override suspend fun addMyStock(myStockData: MyStockData) =
        myStockLocalDataSource.requestInsertMyStock(myStockData.mapping())


    override suspend fun updateMyStock(myStockData: MyStockData) =
        myStockLocalDataSource.requestUpdateMyStock(myStockData.mapping())


    override suspend fun deleteMyStock(myStockData: MyStockData) =
        myStockLocalDataSource.requestDeleteMyStock(myStockData.mapping())


    override suspend fun getAllMyStock(): ResponseResult<List<MyStockData>> {
        return try {
            val data = myStockLocalDataSource.requestGetAllMyStock().map { it.mapping() }
            ResponseResult.Success(data = data, resultCode = "200",  resultMessage = "SUCCESS")
        } catch (e: Exception) {
            ResponseResult.Error(errorCode = "400", errorMessage = e.message?: "Unknown error")
        }
    }

    override suspend fun getKoreaStockPriceInfo(request: ReqKoreaStockPriceInfo): ResponseResult<List<StockPriceData>> {
        val hashMap = HashMap<String, String>().apply {
            this["serviceKey"] = request.serviceKey
            this["numOfRows"] = request.numOfRows
            this["pageNo"] = request.pageNo
            this["resultType"] = request.resultType
            this["basDt"] = request.basDt
            this["likeItmsNm"] = request.likeItmsNm
        }
        val response = APICall.handleApi { stockInfoDataSource.getKoreaStockPriceInfo(hashMap) }
        return if (response is ResponseResult.Success) {
            //mapping
            val data = response.data?.response?.body?.items?.item?.map { it.mapping() }?: listOf()
            ResponseResult.Success(data, "200", "SUCCESS")
        } else {
            ResponseResult.Error(errorCode = response.resultCode, errorMessage = response.resultMessage)
        }
    }

    override suspend fun getUsaStockSymbol(keywords: String): ResponseResult<List<UsaStockSymbolData>> {
        val response = APICall.handleApi { alphaVantageDataSource.getUSAStockSymbol(keywords = keywords) }
        return if (response is ResponseResult.Success) {
            val data = response.data?.bestMatches
                ?.filter { it.region == "United States" }
                ?.map { UsaStockSymbolData(symbol = it.symbol, name = it.name, type = it.type) }
                ?: listOf()
            ResponseResult.Success(data, "200", "SUCCESS")
        } else {
            ResponseResult.Error(errorCode = response.resultCode, errorMessage = response.resultMessage)
        }
    }

    override suspend fun getUsaStockInfo(symbol: String): ResponseResult<StockPriceData> {
        //TODO 작성 따로하기
        val response = alphaVantageDataSource.getUSAStockInfo(symbol = symbol)
        return ResponseResult.Error("500", "error")
    }

    override suspend fun refreshMyStock(type: Int): Boolean {
        return when (val result = getAllMyStock()) {
            is ResponseResult.Success -> {
                result.data?.forEach { myStockEntity ->
                    val reqKoreaStockPriceInfo = ReqKoreaStockPriceInfo(
                        numOfRows = "20",
                        pageNo = "1",
                        isinCd = myStockEntity.subjectCode,
                        likeItmsNm = myStockEntity.subjectName
                    )
                    val stockPriceInfo = getKoreaStockPriceInfo(reqKoreaStockPriceInfo)
                    if (stockPriceInfo is ResponseResult.Success) {
                        val item = stockPriceInfo.data?: listOf()
                        val resultItem = item.find { it.isinCd == myStockEntity.subjectCode }
                        if (resultItem != null) {
                            updateMyStock(
                                myStockEntity.copy(
                                    currentPrice = getNumInsertComma(resultItem.clpr),
                                    dayToDayPrice = resultItem.vs,
                                    dayToDayPercent = resultItem.fltRt,
                                    basDt = resultItem.basDt
                                )
                            )
                        }
                    }
                }
                true
            }
            is ResponseResult.Error -> {
                false
            }
        }
    }

    //5000000 => 5,000,000 변환
    private fun getNumInsertComma(num: String): String {
        if (num.isEmpty()) return "0"
        val decimalFormat = DecimalFormat("###,###")
        return decimalFormat.format(num.replace(",", "").toDouble())
    }

}