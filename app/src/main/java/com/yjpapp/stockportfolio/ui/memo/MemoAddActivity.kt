package com.yjpapp.stockportfolio.ui.memo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_memo_add.*

class MemoAddActivity: BaseActivity(R.layout.activity_memo_add) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()

    }
    override fun initData() {

    }

    override fun initLayout() {
        //Toolbar
        setSupportActionBar(toolbar_MemoAddActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_memo_add, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.menu_MemoAddActivity_Complete -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}