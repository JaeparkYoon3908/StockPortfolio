package com.yjpapp.stockportfolio.ui.memo

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.database.model.MemoInfo
import com.yjpapp.stockportfolio.ui.BaseActivity
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.activity_memo_list.*

class MemoListActivity: BaseActivity(R.layout.activity_memo_list), MemoListAdapter.MemoActivityCallBack {
    companion object {
        const val INTENT_KEY_MEMO_MODE = "INTENT_KEY_MEMO_MODE"
        const val INTENT_KEY_MEMO_INFO_ID = "INTENT_KEY_MEMO_INFO_ID"
        const val INTENT_KEY_MEMO_INFO_TITLE = "INTENT_KEY_MEMO_INFO_TITLE"
        const val INTENT_KEY_MEMO_INFO_CONTENT = "INTENT_KEY_MEMO_INFO_CONTENT"
        const val INTENT_KEY_LIST_POSITION = "INTENT_KEY_LIST_POSITION"

        const val MEMO_READ_MODE = "MEMO_READ_MODE" //메모 읽기 모드
        const val MEMO_ADD_MODE = "MEMO_WRITE_MODE" //새 메모 추가모드
        const val MEMO_UPDATE_MODE = "MEMO_UPDATE_MODE" //메모 읽기 모드 -? 업데잍 모드

        const val REQUEST_ADD = 0
        const val REQUEST_READ = 1

        const val RESULT_EMPTY = 10000
        const val RESULT_DELETE = RESULT_EMPTY + 1
        const val RESULT_UPDATE = RESULT_EMPTY + 2
    }

    private lateinit var memoDataList: ArrayList<MemoInfo?>
    private lateinit var layoutManager: LinearLayoutManager
    private var memoListAdapter: MemoListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initLayout()
    }

    override fun initData() {
        memoDataList = databaseController.getAllMemoDataInfo()
    }

    override fun initLayout(){
        //Toolbar
        setSupportActionBar(toolbar_MemoListActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_OK -> {
                when (requestCode) {
                    REQUEST_ADD -> {
                        memoDataList = databaseController.getAllMemoDataInfo()
                        memoListAdapter?.setMemoListData(memoDataList)
                        memoListAdapter?.notifyItemInserted(memoDataList.size - 1)
                        if (memoDataList.size != 0) {
                            txt_MemoListActivity_GuideMessage.visibility = View.GONE
                            layoutManager.scrollToPosition(memoDataList.size - 1)
                        }
                    }
                }
            }

            RESULT_EMPTY -> {
                val mode = data?.getStringExtra(INTENT_KEY_MEMO_MODE)
                logcat("mode = $mode")
                Toasty.normal(mContext, "내용이 없어 메모를 저장하지 않습니다.", Toasty.LENGTH_LONG).show()
            }
            RESULT_DELETE -> {
                memoDataList = databaseController.getAllMemoDataInfo()
                memoListAdapter?.setMemoListData(memoDataList)
                memoListAdapter?.notifyItemRemoved(data?.getIntExtra(INTENT_KEY_LIST_POSITION, 0)!!)
                memoListAdapter?.notifyDataSetChanged()
                if (memoDataList.size == 0) {
                    txt_MemoListActivity_GuideMessage.visibility = View.VISIBLE
                }
            }

            RESULT_UPDATE -> {
                memoDataList = databaseController.getAllMemoDataInfo()
                memoListAdapter?.setMemoListData(memoDataList)
                memoListAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onAdapterItemLongClicked() {
        if(memoListAdapter?.getDeleteModeOn()!!){
            menu?.findItem(R.id.menu_MemoListActivity_Add)?.isVisible = false
            menu?.findItem(R.id.menu_MemoListActivity_Delete)?.isVisible = true
        }else{
            menu?.findItem(R.id.menu_MemoListActivity_Add)?.isVisible = true
            menu?.findItem(R.id.menu_MemoListActivity_Delete)?.isVisible = false
        }
    }

    override fun onAdapterItemClicked(position: Int) {
        val intent = Intent(mContext, MemoReadWriteActivity::class.java)
        intent.putExtra(MemoListActivity.INTENT_KEY_LIST_POSITION, position)
        intent.putExtra(MemoListActivity.INTENT_KEY_MEMO_INFO_ID, memoDataList[position]?.id)
        intent.putExtra(MemoListActivity.INTENT_KEY_MEMO_INFO_TITLE, memoDataList[position]?.title)
        intent.putExtra(MemoListActivity.INTENT_KEY_MEMO_INFO_CONTENT,
            memoDataList[position]?.content)
        intent.putExtra(MemoListActivity.INTENT_KEY_MEMO_MODE, MEMO_READ_MODE)

        startActivityForResult(intent, REQUEST_READ)
    }

    private fun initRecyclerView(){
        layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
        if(memoDataList.size != 0){
            layoutManager.scrollToPosition(memoDataList.size - 1)
            txt_MemoListActivity_GuideMessage.visibility = View.GONE
        }else{
            txt_MemoListActivity_GuideMessage.visibility = View.VISIBLE
        }
        recyclerview_MemoListActivity.layoutManager = layoutManager

        memoListAdapter = MemoListAdapter(memoDataList, this)
        recyclerview_MemoListActivity.adapter = memoListAdapter
        recyclerview_MemoListActivity.itemAnimator = SlideInLeftAnimator()
    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_memo_list, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.menu_MemoListActivity_Add -> {
                val intent = Intent(mContext, MemoReadWriteActivity::class.java)
                intent.putExtra(INTENT_KEY_MEMO_MODE, MEMO_ADD_MODE)
                startActivityForResult(intent, REQUEST_ADD)
            }

            R.id.menu_MemoListActivity_Delete -> {
                AlertDialog.Builder(this)
                    .setMessage(getString(R.string.MemoListActivity_Delete_Check_Message))
                    .setPositiveButton(R.string.Common_Ok) {_,_ ->
                        val memoInfo = memoListAdapter?.getMemoListData()
                        for (i in memoInfo?.indices!!) {
                            if (memoInfo[i]?.deleteChecked!!) {
                                databaseController.deleteData(memoInfo[i]?.id!!,
                                    Databases.TABLE_MEMO)
                                memoDataList = databaseController.getAllMemoDataInfo()
                                memoListAdapter?.setMemoListData(memoDataList)
                                memoListAdapter?.notifyItemRemoved(i)
                            }
                        }
                        setDeleteModeOff()
                        if (memoDataList.size == 0) {
                            txt_MemoListActivity_GuideMessage.visibility = View.VISIBLE
                        }

                    }
                    .setNegativeButton(R.string.Common_Cancel) {dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(memoListAdapter?.getDeleteModeOn()!!){
            setDeleteModeOff()
        }else{
            finish()
        }
    }

    private fun setDeleteModeOff(){
        memoListAdapter?.setDeleteModeOn(false)
        memoListAdapter?.notifyDataSetChanged()
        menu?.findItem(R.id.menu_MemoListActivity_Add)?.isVisible = true
        menu?.findItem(R.id.menu_MemoListActivity_Delete)?.isVisible = false
    }
}