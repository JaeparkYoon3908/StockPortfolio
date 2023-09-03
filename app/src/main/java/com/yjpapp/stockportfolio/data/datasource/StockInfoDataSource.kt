package com.yjpapp.stockportfolio.data.datasource

import com.yjpapp.stockportfolio.data.APICall
import com.yjpapp.stockportfolio.data.network.service.DataPortalService
import javax.inject.Inject

class StockInfoDataSource @Inject constructor(
    private val dataPortalService: DataPortalService,
) {
    suspend fun getStockPriceData(params: HashMap<String, String>) = APICall.handleApi {
        dataPortalService.getStockPriceInfo(params)
    }
}