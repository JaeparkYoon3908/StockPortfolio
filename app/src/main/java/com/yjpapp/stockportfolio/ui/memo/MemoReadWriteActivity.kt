package com.yjpapp.stockportfolio.ui.memo

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.database.model.MemoInfo
import com.yjpapp.stockportfolio.ui.BaseActivity
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.activity_memo_read_write.*


class MemoReadWriteActivity: BaseActivity(R.layout.activity_memo_read_write) {
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

        et_MemoReadWriteActivity_title.onFocusChangeListener = onFocusChangeListener
        et_MemoReadWriteActivity_content.onFocusChangeListener = onFocusChangeListener
    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_memo_read_write, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if(mode == MemoListActivity.MEMO_READ_MODE){
            et_MemoReadWriteActivity_title.setText(title)
            et_MemoReadWriteActivity_content.setText(content)
            menu.findItem(R.id.menu_MemoReadWriteActivity_Complete)?.isVisible = false
        }else{
            menu.findItem(R.id.menu_MemoReadWriteActivity_Delete)?.isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.menu_MemoReadWriteActivity_Complete -> {
                if (mode == MemoListActivity.MEMO_ADD_MODE) {
                    if (et_MemoReadWriteActivity_title.text.toString() == ("") && et_MemoReadWriteActivity_content.text.toString() == "") {
                        setResult(MemoListActivity.RESULT_EMPTY)
                    } else {
                        setResult(RESULT_OK)
                        addMemo()
                    }
                    finish()
                } else if (mode == MemoListActivity.MEMO_UPDATE_MODE) {
                    if (et_MemoReadWriteActivity_title.text.toString() == ("") && et_MemoReadWriteActivity_content.text.toString() == "") {
                        setResult(MemoListActivity.RESULT_EMPTY)
                    } else {
                        setResult(MemoListActivity.RESULT_UPDATE)
                        updateMemo()
                    }
                    finish()
                }
            }
            R.id.menu_MemoReadWriteActivity_Delete -> {
                AlertDialog.Builder(this)
                    .setMessage(getString(R.string.MemoListActivity_Delete_Check_Message))
                    .setPositiveButton(R.string.Common_Ok) {_,_ ->
                        databaseController.deleteData(id, Databases.TABLE_MEMO)
                        val intent = Intent(mContext, MemoListActivity::class.java)
                        intent.putExtra(MemoListActivity.INTENT_KEY_LIST_POSITION, memoListPosition)
                        setResult(MemoListActivity.RESULT_DELETE, intent)
                        finish()

                    }
                    .setNegativeButton(R.string.Common_Cancel) {dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addMemo(){
        val date = Utils.getTodayYYYY_MM_DD()
        val title = et_MemoReadWriteActivity_title.text.toString()
        val content = et_MemoReadWriteActivity_content.text.toString()
        val memoInfo = MemoInfo(0, date, title, content, false)
        databaseController.insertMemoData(memoInfo)

    }

    private fun updateMemo(){
        val date = Utils.getTodayYYYY_MM_DD()
        val title = et_MemoReadWriteActivity_title.text.toString()
        val content = et_MemoReadWriteActivity_content.text.toString()
        val memoInfo = MemoInfo(id, date, title, content, false)
        databaseController.updateMemoData(memoInfo)
    }

//    @SuppressLint("ClickableViewAccessibility")
//    private val onTouchListener = View.OnTouchListener
    private val onFocusChangeListener = View.OnFocusChangeListener { _:View, _:Boolean ->
        if(mode == MemoListActivity.MEMO_READ_MODE){
            mode = MemoListActivity.MEMO_UPDATE_MODE
            menu?.findItem(R.id.menu_MemoReadWriteActivity_Complete)?.isVisible = true
            menu?.findItem(R.id.menu_MemoReadWriteActivity_Delete)?.isVisible = false
        }
    }
}