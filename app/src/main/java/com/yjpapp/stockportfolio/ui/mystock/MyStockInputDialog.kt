package com.yjpapp.stockportfolio.ui.mystock

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseInteractor
import com.yjpapp.stockportfolio.database.sqlte.DatabaseController
import com.yjpapp.stockportfolio.database.sqlte.DatabaseOpenHelper

class MyStockInputDialog(context: Context): AlertDialog(context) {
    companion object{
        @Volatile private var instance: MyStockInputDialog? = null
        @JvmStatic
        fun getInstance(context: Context): MyStockInputDialog =
                instance ?: synchronized(this) {
                    instance ?: MyStockInputDialog(context).also {
                        instance = it
                    }
                }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_input_my_stock)
        initLayout()
    }

    private fun initLayout(){

    }
}