package com.yjpapp.stockportfolio.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.yjpapp.stockportfolio.R
import kotlinx.android.synthetic.main.dailog_main_filter.*

class MainFilterDialog (context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dailog_main_filter)
        initLayout()
    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when(view?.id){
            R.id.cons_MainFilterDialog_MainContainer -> {
                dismiss()
            }
            R.id.txt_MainFilterDialog_cancel -> {
                dismiss()
            }
        }
    }

    private fun initLayout(){
        cons_MainFilterDialog_MainContainer.setOnClickListener(onClickListener)
        txt_MainFilterDialog_cancel.setOnClickListener(onClickListener)
//        txt_MainFilterDialog_all.setOnClickListener(onClickListener)
//        txt_MainFilterDialog_gain.setOnClickListener(onClickListener)
//        txt_MainFilterDialog_loss.setOnClickListener(onClickListener)
    }

}