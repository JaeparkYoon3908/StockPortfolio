package com.yjpapp.data.repository

import androidx.compose.runtime.toMutableStateList
import com.yjpapp.data.datasource.MyStockRoomDataSource
import com.yjpapp.data.mapper.mapping
import com.yjpapp.data.model.MyStockData
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqStockPriceInfo
import com.yjpapp.data.model.response.StockPriceData
import com.yjpapp.network.datasource.DataPortalDataSource
import retrofit2.HttpException
import java.text.DecimalFormat
import javax.inject.Inject

interface MyStockRepository {
    suspend fun addMyStock(myStockData: MyStockData)
    suspend fun updateMyStock(myStockData: MyStockData): Boolean
    suspend fun deleteMyStock(myStockData: MyStockData)
    suspend fun getAllMyStock(): List<MyStockData>
    suspend fun getStockPriceInfo(request: ReqStockPriceInfo): ResponseResult<List<StockPriceData>>
    suspend fun refreshMyStock(): Boolean
}

class MyStockRepositoryImpl @Inject constructor(
    private val myStockLocalDataSource: MyStockRoomDataSource,
    private val stockInfoDataSource: DataPortalDataSource
) : MyStockRepository {

    override suspend fun addMyStock(myStockData: MyStockData) =
        myStockLocalDataSource.requestInsertMyStock(myStockData.mapping())


    override suspend fun updateMyStock(myStockData: MyStockData) =
        myStockLocalDataSource.requestUpdateMyStock(myStockData.mapping())


    override suspend fun deleteMyStock(myStockData: MyStockData) =
        myStockLocalDataSource.requestDeleteMyStock(myStockData.mapping())


    override suspend fun getAllMyStock() = myStockLocalDataSource.requestGetAllMyStock().map { it.mapping() }

    override suspend fun getStockPriceInfo(request: ReqStockPriceInfo): ResponseResult<List<StockPriceData>> {
        val hashMap = HashMap<String, String>().apply {
            this["serviceKey"] = request.serviceKey
            this["numOfRows"] = request.numOfRows
            this["pageNo"] = request.pageNo
            this["resultType"] = request.resultType
            this["basDt"] = request.basDt
            this["likeItmsNm"] = request.likeItmsNm
        }
        val response = stockInfoDataSource.getStockPriceInfo(hashMap)
        return try {
            if (response.isSuccessful) {
                val data = response.body()?.response?.body?.items?.item?.map { it.mapping() }?: listOf()
                ResponseResult.Success(data, "200", "SUCCESS")
            } else {
                ResponseResult.Error("400", response.message())
            }
        } catch (e: HttpException) {
            ResponseResult.Error("500", "${e.message}")
        } catch (e: Throwable) {
            ResponseResult.Error("501", "${e.message}")
        }
    }

    override suspend fun refreshMyStock(): Boolean {
        getAllMyStock().toMutableStateList().forEach { myStockEntity ->
            val reqStockPriceInfo = ReqStockPriceInfo(
                numOfRows = "20",
                pageNo = "1",
                isinCd = myStockEntity.subjectCode,
                likeItmsNm = myStockEntity.subjectName
            )
            val result = getStockPriceInfo(reqStockPriceInfo)
            if (result is ResponseResult.Success) {
                val item = result.data?: listOf()
                val resultItem = item.find { it.isinCd == myStockEntity.subjectCode }

                if (resultItem != null) {
                    if (!updateMyStock(
                            myStockEntity.copy(
                                currentPrice = getNumInsertComma(resultItem.clpr),
                                dayToDayPrice = resultItem.vs,
                                dayToDayPercent = resultItem.fltRt,
                                basDt = resultItem.basDt
                            )
                        )
                    ) {
                        return false
                    }
                } else {
                    //서버에 빈 값이 내려옴
                    return false
                }
            } else {
                //에러
                return false
            }
        }
        return true
    }

    //5000000 => 5,000,000 변환
    private fun getNumInsertComma(num: String): String {
        if (num.isEmpty()) return "0"
        val decimalFormat = DecimalFormat("###,###")
        return decimalFormat.format(num.replace(",", "").toDouble())
    }

}