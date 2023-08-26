package com.yjpapp.stockportfolio.data.repository

import com.yjpapp.stockportfolio.data.datasource.MyStockRoomDataSource
import com.yjpapp.stockportfolio.data.datasource.StockInfoDataSource
import com.yjpapp.stockportfolio.data.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.data.model.ResponseResult
import com.yjpapp.stockportfolio.data.model.request.ReqStockPriceInfo
import com.yjpapp.stockportfolio.data.model.response.RespStockPriceInfo
import retrofit2.Response
import javax.inject.Inject

interface MyStockRepository {
    suspend fun addMyStock(myStockEntity: MyStockEntity)
    suspend fun updateMyStock(myStockEntity: MyStockEntity)
    suspend fun deleteMyStock(myStockEntity: MyStockEntity)
    suspend fun getAllMyStock(): MutableList<MyStockEntity>
    suspend fun getStockPriceInfo(request: ReqStockPriceInfo): ResponseResult<RespStockPriceInfo>
}
class MyStockRepositoryImpl @Inject constructor(
    private val myStockLocalDataSource: MyStockRoomDataSource,
    private val stockInfoDataSource: StockInfoDataSource
): MyStockRepository {

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
}