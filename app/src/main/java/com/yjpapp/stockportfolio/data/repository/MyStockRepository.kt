package com.yjpapp.stockportfolio.data.repository

import androidx.compose.runtime.toMutableStateList
import com.yjpapp.stockportfolio.data.datasource.MyStockRoomDataSource
import com.yjpapp.stockportfolio.data.datasource.StockInfoDataSource
import com.yjpapp.stockportfolio.data.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.data.model.ResponseResult
import com.yjpapp.stockportfolio.data.model.request.ReqStockPriceInfo
import com.yjpapp.stockportfolio.data.model.response.RespStockPriceInfo
import com.yjpapp.stockportfolio.util.StockUtils
import retrofit2.Response
import javax.inject.Inject

interface MyStockRepository {
    suspend fun addMyStock(myStockEntity: MyStockEntity)
    suspend fun updateMyStock(myStockEntity: MyStockEntity): Boolean
    suspend fun deleteMyStock(myStockEntity: MyStockEntity)
    suspend fun getAllMyStock(): MutableList<MyStockEntity>
    suspend fun getStockPriceInfo(request: ReqStockPriceInfo): ResponseResult<RespStockPriceInfo>
    suspend fun refreshMyStock(): Boolean
}

class MyStockRepositoryImpl @Inject constructor(
    private val myStockLocalDataSource: MyStockRoomDataSource,
    private val stockInfoDataSource: StockInfoDataSource
) : MyStockRepository {

    override suspend fun addMyStock(myStockEntity: MyStockEntity) =
        myStockLocalDataSource.requestInsertMyStock(myStockEntity)


    override suspend fun updateMyStock(myStockEntity: MyStockEntity) =
        myStockLocalDataSource.requestUpdateMyStock(myStockEntity)


    override suspend fun deleteMyStock(myStockEntity: MyStockEntity) =
        myStockLocalDataSource.requestDeleteMyStock(myStockEntity)


    override suspend fun getAllMyStock() = myStockLocalDataSource.requestGetAllMyStock()

    override suspend fun getStockPriceInfo(request: ReqStockPriceInfo) =
        HashMap<String, String>().apply {
            this["serviceKey"] = request.serviceKey
            this["numOfRows"] = request.numOfRows
            this["pageNo"] = request.pageNo
            this["resultType"] = request.resultType
            this["basDt"] = request.basDt
            this["likeItmsNm"] = request.likeItmsNm
        }.run {
            stockInfoDataSource.getStockPriceData(this)
        }

    override suspend fun refreshMyStock(): Boolean {
        getAllMyStock().toMutableStateList().forEach {
            val reqStockPriceInfo = ReqStockPriceInfo(
                numOfRows = "20",
                pageNo = "1",
                isinCd = it.subjectCode,
                likeItmsNm = it.subjectName
            )
            val result = getStockPriceInfo(reqStockPriceInfo)
            if (result.isSuccessful && result.data != null) {
                val item = result.data.response.body.items.item
                if (item.isNotEmpty()) {
                    if (!updateMyStock(
                            MyStockEntity(
                                id = it.id,
                                subjectName = item.first().itmsNm,
                                subjectCode = item.first().isinCd,
                                purchaseDate = it.purchaseDate,
                                purchasePrice = it.purchasePrice,
                                purchaseCount = it.purchaseCount,
                                currentPrice = StockUtils.getNumInsertComma(item.first().clpr),
                                dayToDayPrice = item.first().vs,
                                dayToDayPercent = item.first().fltRt,
                                basDt = item.first().basDt,
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
}