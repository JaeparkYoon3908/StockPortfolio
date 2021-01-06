package com.yjpapp.stockportfolio.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.yjpapp.stockportfolio.R

class CommonCheckDialog (context: Context) : AlertDialog(context) {
    //TODO 확인하는 커스텀 다이얼로그 디자인 고안.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_common_check)
    }
}