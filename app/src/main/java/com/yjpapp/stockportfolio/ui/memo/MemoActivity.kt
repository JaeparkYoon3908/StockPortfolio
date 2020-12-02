package com.yjpapp.stockportfolio.ui.memo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.MemoInfo
import com.yjpapp.stockportfolio.ui.BaseActivity
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_memo.*

class MemoActivity: BaseActivity(R.layout.activity_memo) {
    private val REQUEST_ADD = 0
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            when(requestCode){
                REQUEST_ADD -> {
                    memoData = databaseController.getAllMemoDataInfo()
                    memoListAdapter?.setMemoListData(memoData)
                    memoListAdapter?.notifyDataSetChanged()
                    recyclerview_MainActivity.scrollToPosition(memoData.size - 1)
                }
            }
        }

    }

    private fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
        if(memoData.size != 0){
            layoutManager.scrollToPosition(memoData.size-1)
            txt_MemoActivity_GuideMessage.visibility = View.GONE
        }else{
            txt_MemoActivity_GuideMessage.visibility = View.VISIBLE
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
                startActivityForResult(intent, REQUEST_ADD)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}