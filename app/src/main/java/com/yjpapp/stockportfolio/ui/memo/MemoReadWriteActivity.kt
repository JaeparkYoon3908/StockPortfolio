package com.yjpapp.stockportfolio.ui.memo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.model.MemoInfo
import com.yjpapp.stockportfolio.ui.BaseActivity
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.activity_memo_add.*


class MemoReadWriteActivity: BaseActivity(R.layout.activity_memo_add) {
    private var mode: String? = null
    private var memoListPosition = 0
    private var id = 0
    private var title: String? = null
    private var content: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initLayout()

    }
    override fun initData() {
        mode = intent.getStringExtra(MemoListActivity.INTENT_KEY_MEMO_MODE)
        if(mode == MemoListActivity.MEMO_READ_MODE){ // 읽기 모드
            memoListPosition = intent.getIntExtra(MemoListActivity.INTENT_KEY_LIST_POSITION, 0)
            id = intent.getIntExtra(MemoListActivity.INTENT_KEY_MEMO_INFO_ID, 0)
            title = intent.getStringExtra(MemoListActivity.INTENT_KEY_MEMO_INFO_TITLE)
            content = intent.getStringExtra(MemoListActivity.INTENT_KEY_MEMO_INFO_CONTENT)
        }
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
        //0번 : complete
        //1번 : delete
        menuInflater.inflate(R.menu.menu_memo_read_write, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if(mode == MemoListActivity.MEMO_READ_MODE){
            et_MemoAddActivity_title.setText(title)
            et_MemoAddActivity_title.isEnabled = false
            et_MemoAddActivity_content.setText(content)
            et_MemoAddActivity_content.isEnabled = false
            menu.findItem(R.id.menu_MemoAddActivity_Complete)?.isVisible = false
        }else{
            et_MemoAddActivity_title.isEnabled = true
            et_MemoAddActivity_content.isEnabled = true
            menu.findItem(R.id.menu_MemoAddActivity_Delete)?.isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.menu_MemoAddActivity_Complete -> {
                if(mode == MemoListActivity.MEMO_ADD_MODE){
                    if (et_MemoAddActivity_title.text.toString() == ("") && et_MemoAddActivity_content.text.toString() == "") {
                        setResult(MemoListActivity.RESULT_EMPTY)
                    } else {
                        setResult(RESULT_OK)
                        addMemo()
                    }
                    finish()
                }else if(mode == MemoListActivity.MEMO_READ_MODE){
                    //TODO 메모 업데이트 적용
                }
            }
            R.id.menu_MemoAddActivity_Delete -> {
                databaseController.deleteData(id, Databases.TABLE_MEMO)
                val intent = Intent(mContext, MemoListActivity::class.java)
                intent.putExtra(MemoListActivity.INTENT_KEY_LIST_POSITION, memoListPosition)
                setResult(MemoListActivity.RESULT_DELETE, intent)
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