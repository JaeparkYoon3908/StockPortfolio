package com.yjpapp.stockportfolio.ui.memo

import android.os.Bundle
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_memo_detail.*

class MemoDetailActivity: BaseActivity(R.layout.activity_memo_detail) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initLayout()
    }

    override fun initData() {
    }

    override fun initLayout() {
        //Toolbar
        setSupportActionBar(toolbar_MemoDetailActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}