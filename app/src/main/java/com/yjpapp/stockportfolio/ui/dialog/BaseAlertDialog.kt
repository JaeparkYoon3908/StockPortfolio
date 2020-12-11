package com.yjpapp.stockportfolio.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager

import com.yjpapp.stockportfolio.R

open class BaseAlertDialog(context: Context, layoutId: Int): AlertDialog(context) {
    private var mContext: Context

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setView(View.inflate(context, R.layout.dialog_add_portfolio, null))
        mContext = context
        val window = window
        if (window != null) {
            // 백그라운드 투명
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val params = window.attributes
            // 화면에 가득 차도록
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.MATCH_PARENT

            // 열기&닫기 시 애니메이션 설정
            params.windowAnimations = R.style.AnimationPopupStyle
            window.attributes = params
            // UI 하단 정렬
            window.setGravity(Gravity.BOTTOM)
        }
    }
}