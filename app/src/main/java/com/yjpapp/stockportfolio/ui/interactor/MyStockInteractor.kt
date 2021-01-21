package com.yjpapp.stockportfolio.ui.interactor

import android.content.Context
import com.yjpapp.stockportfolio.database.DatabaseController
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.database.data.MyStockInfo

/**
 * MyStockFragment의 Model 역할하는 class
 *
 * @author Yun Jae-park
 * @since 2020.12
 */
class MyStockInteractor {

    companion object {
        @Volatile private var instance: MyStockInteractor? = null
        private lateinit var mContext: Context
        private lateinit var databaseController: DatabaseController
        @JvmStatic
        fun getInstance(context: Context): MyStockInteractor =
                instance ?: synchronized(this) {
                    instance ?: MyStockInteractor().also {
                        instance = it
                        mContext = context
                        databaseController = DatabaseController.getInstance(mContext)
                    }
                }
    }

    fun getAllMyStockList(): ArrayList<MyStockInfo?> {
        return databaseController.getAllMyStockList()
    }

    fun insertMyStockInfo(myStockInfo: MyStockInfo){
        databaseController.insertMyStockData(myStockInfo)
    }

    fun updateMyStockInfo(myStockInfo: MyStockInfo){
        databaseController.updateMyStockData(myStockInfo)
    }

    fun updateCurrentPrice(id: Int, currentPrice: String){
        databaseController.updateCurrentPrice(id, currentPrice)
    }

    fun deleteMyStockInfo(id: Int){
        databaseController.deleteData(id, Databases.TABLE_MY_STOCK)

    }
}