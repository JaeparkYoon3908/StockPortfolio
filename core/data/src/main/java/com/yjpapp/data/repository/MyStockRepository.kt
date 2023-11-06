package com.yjpapp.data.repository

import androidx.compose.runtime.toMutableStateList
import com.yjpapp.data.APICall
import com.yjpapp.data.datasource.MyStockRoomDataSource
import com.yjpapp.database.mystock.MyStockEntity
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqStockPriceInfo
import com.yjpapp.network.datasource.DataPortalDataSource
import com.yjpapp.network.model.RespStockPriceInfo
import java.text.DecimalFormat
import javax.inject.Inject

interface MyStockRepository {
    suspend fun addMyStock(myStockEntity: MyStockEntity)
    suspend fun updateMyStock(myStockEntity: MyStockEntity): Boolean
    suspend fun deleteMyStock(myStockEntity: MyStockEntity)
    suspend fun getAllMyStock(): List<MyStockEntity>
    suspend fun getStockPriceInfo(request: ReqStockPriceInfo): ResponseResult<RespStockPriceInfo>
    suspend fun refreshMyStock(): Boolean
}

class MyStockRepositoryImpl @Inject constructor(
    private val myStockLocalDataSource: MyStockRoomDataSource,
    private val stockInfoDataSource: DataPortalDataSource
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
            APICall.handleApi { stockInfoDataSource.getStockPriceInfo(this) }
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
            if (result.isSuccessful && result.data != null) {
                val item = result.data.response.body.items.item
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