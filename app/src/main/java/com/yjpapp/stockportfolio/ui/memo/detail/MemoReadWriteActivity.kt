package com.yjpapp.stockportfolio.ui.memo.detail

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVPActivity
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.databinding.ActivityMemoReadWriteBinding
import com.yjpapp.stockportfolio.ui.memo.MemoListFragment
import com.yjpapp.stockportfolio.ui.memo.detail.MemoReadWritePresenter
import com.yjpapp.stockportfolio.ui.memo.detail.MemoReadWriteView
import com.yjpapp.stockportfolio.util.Utils

/**
 * 메모 읽기 및 추가 화면
 *
 * @author Yoon Jae-park
 * @since 2020.12.27
 */
class MemoReadWriteActivity: BaseMVPActivity<ActivityMemoReadWriteBinding>(), MemoReadWriteView {
    private lateinit var memoReadWritePresenter: MemoReadWritePresenter
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
        memoReadWritePresenter = MemoReadWritePresenter(mContext, this)
        mode = intent.getStringExtra(MemoListFragment.INTENT_KEY_MEMO_MODE)
        if(mode == MemoListFragment.MEMO_READ_MODE){ // 읽기 모드
            memoListPosition = intent.getIntExtra(MemoListFragment.INTENT_KEY_LIST_POSITION, 0)
            id = intent.getIntExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_ID, 0)
            title = intent.getStringExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_TITLE)
            content = intent.getStringExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_CONTENT)
        }
    }

    override fun initLayout() {
        //Toolbar
        binding.apply {
            setSupportActionBar(toolbarMemoReadWriteActivity)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)

            etMemoReadWriteActivityTitle.onFocusChangeListener = onFocusChangeListener
            etMemoReadWriteActivityContent.onFocusChangeListener = onFocusChangeListener
        }

    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_memo_read_write, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        binding.apply {
            if(mode == MemoListFragment.MEMO_READ_MODE){
                etMemoReadWriteActivityTitle.setText(title)
                etMemoReadWriteActivityContent.setText(content)
                hideCompleteButton()
            }else{
                hideDeleteButton()
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.menu_MemoReadWriteFragment_Complete -> {
                onCompleteButtonClick()
            }
            R.id.menu_MemoReadWriteFragment_Delete -> {
                onDeleteButtonClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val onFocusChangeListener = View.OnFocusChangeListener { _:View, _:Boolean ->
        if(mode == MemoListFragment.MEMO_READ_MODE){
            mode = MemoListFragment.MEMO_UPDATE_MODE
            showCompleteButton()
            hideDeleteButton()
        }
    }

    override fun showCompleteButton() {
        menu?.findItem(R.id.menu_MemoReadWriteFragment_Complete)?.isVisible = true
    }

    override fun hideCompleteButton() {
        menu?.findItem(R.id.menu_MemoReadWriteFragment_Complete)?.isVisible = false
    }

    override fun showDeleteButton() {
        menu?.findItem(R.id.menu_MemoReadWriteFragment_Delete)?.isVisible = true
    }

    override fun hideDeleteButton() {
        menu?.findItem(R.id.menu_MemoReadWriteFragment_Delete)?.isVisible = false
    }

    override fun onDeleteButtonClick() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.MemoListFragment_Delete_Check_Message))
            .setPositiveButton(R.string.Common_Ok) {_,_ ->
                databaseController.deleteData(id, Databases.TABLE_MEMO)
                val intent = Intent(mContext, MemoListFragment::class.java)
                intent.putExtra(MemoListFragment.INTENT_KEY_LIST_POSITION, memoListPosition)
                setResult(MemoListFragment.RESULT_DELETE, intent)
                finish()

            }
            .setNegativeButton(R.string.Common_Cancel) {dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onCompleteButtonClick() {
        binding.apply {
            if (mode == MemoListFragment.MEMO_ADD_MODE) {
                if (etMemoReadWriteActivityTitle.text.toString() == ("") && etMemoReadWriteActivityContent.text.toString() == "") {
                    setResult(MemoListFragment.RESULT_EMPTY)
                } else {
                    val date = Utils.getTodayYYYY_MM_DD()
                    val title = etMemoReadWriteActivityTitle.text.toString()
                    val content = etMemoReadWriteActivityContent.text.toString()
                    memoReadWritePresenter.requestAddMemoData(date, title, content)
                    setResult(RESULT_OK)
                }
                finish()
            } else if (mode == MemoListFragment.MEMO_UPDATE_MODE) {
                if (etMemoReadWriteActivityTitle.text.toString() == ("") && etMemoReadWriteActivityContent.text.toString() == "") {
                    setResult(MemoListFragment.RESULT_EMPTY)
                } else {
                    val date = Utils.getTodayYYYY_MM_DD()
                    val title = etMemoReadWriteActivityTitle.text.toString()
                    val content = etMemoReadWriteActivityContent.text.toString()
                    memoReadWritePresenter.requestUpdateMemoData(id, date, title, content)
                    setResult(MemoListFragment.RESULT_UPDATE)
                }
                finish()
            }
        }
    }

    override fun getViewBinding(): ActivityMemoReadWriteBinding {
        return ActivityMemoReadWriteBinding.inflate(layoutInflater)
    }
}