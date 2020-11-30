package com.yjpapp.stockportfolio.ui.memo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.MemoInfo
import com.yjpapp.stockportfolio.ui.BaseActivity
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.activity_memo.*

class MemoActivity: BaseActivity(R.layout.activity_memo) {
    private lateinit var memoData: ArrayList<MemoInfo?>
    private var memoListAdapter: MemoListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initLayout()

    }

    override fun initData() {
        memoData = databaseController.getAllMemoDataInfo()
    }

    override fun initLayout(){
        //Toolbar
        setSupportActionBar(toolbar_MemoActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initRecyclerView()
    }

    private fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
        if(memoData.size != 0){
            layoutManager.scrollToPosition(memoData.size-1)
        }
        recyclerview_MemoActivity.layoutManager = layoutManager

        memoListAdapter = MemoListAdapter(memoData)
        recyclerview_MemoActivity.adapter = memoListAdapter
        recyclerview_MemoActivity.itemAnimator = FadeInAnimator()

    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_memo, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.menu_MemoActivity_Add -> {
                val intent = Intent(mContext, MemoAddActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}