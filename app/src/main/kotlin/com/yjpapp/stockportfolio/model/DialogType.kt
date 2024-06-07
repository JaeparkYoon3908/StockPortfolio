package com.yjpapp.stockportfolio.model

import com.yjpapp.data.model.MyStockData
import com.yjpapp.stockportfolio.ui.main.mystock.dialog.MyStockPurchaseInputDialogData

sealed interface DialogType {
    data object None: DialogType // 다이얼로그 없음
    data object Common: DialogType // 일반 다이얼로그
    data object Error: DialogType // 에러 다이얼로그
    data object InsertPurchaseInput: DialogType // 나의 주식 추가 다이얼로그
    data class UpdatePurchaseInput( // 나의 주식 수정 다이얼로그
        val myStockData: MyStockData
    ): DialogType
}