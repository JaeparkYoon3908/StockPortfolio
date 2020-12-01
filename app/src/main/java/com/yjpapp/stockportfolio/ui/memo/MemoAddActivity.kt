package com.yjpapp.stockportfolio.ui.memo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.MemoInfo
import com.yjpapp.stockportfolio.ui.BaseActivity
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.activity_memo_add.*

class MemoAddActivity: BaseActivity(R.layout.activity_memo_add) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initLayout()

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
                if(et_MemoAddActivity_title.text.toString() == ("")){
                    //TODO Toast Text 문자들 String에 넣기.
                    Toast.makeText(mContext, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }

                if(et_MemoAddActivity_content.text.toString() == ""){
                    Toast.makeText(mContext, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return true
                }

                addMemo()
                setResult(RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addMemo(){
        val date = Utils.getTodayYYYY_MM_DD()
        val title = et_MemoAddActivity_title.text.toString()
        val content = et_MemoAddActivity_content.text.toString()
        val memoInfo = MemoInfo(0, date, title, content)
        databaseController.insertMemoData(memoInfo)
    }
}