package com.yjpapp.stockportfolio.function.memo.detail

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.constance.StockConfig
import com.yjpapp.stockportfolio.localdb.sqlte.Databases
import com.yjpapp.stockportfolio.databinding.ActivityMemoReadWriteBinding
import com.yjpapp.stockportfolio.function.memo.MemoListFragment
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.util.Utils
import org.koin.android.ext.android.inject

/**
 * 메모 읽기 및 추가 화면
 * 디자인 패턴 : MVVM
 * @author Yoon Jae-park
 * @since 2020.12.27
 */
class MemoReadWriteActivity: BaseActivity() {
    private var mode: String? = null
    private var memoListPosition = 0
    private var id = 0
    private var title: String? = null
    private var content: String? = null
    private val viewModel: MemoReadWriteViewModel by inject()
    private var _binding: ActivityMemoReadWriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_memo_read_write)
        initView()
        initData()
    }

    private fun initView() {
        //Toolbar
        binding.apply {
            setSupportActionBar(toolbarMemoReadWriteActivity)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)

            etMemoReadWriteActivityTitle.onFocusChangeListener = onFocusChangeListener
            etMemoReadWriteActivityContent.onFocusChangeListener = onFocusChangeListener
        }

    }

    private fun initData() {
        mode = intent.getStringExtra(MemoListFragment.INTENT_KEY_MEMO_MODE)
        if(mode == MemoListFragment.MEMO_READ_MODE){ // 읽기 모드
            memoListPosition = intent.getIntExtra(MemoListFragment.INTENT_KEY_LIST_POSITION, 0)
            id = intent.getIntExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_ID, 0)
            title = intent.getStringExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_TITLE)
            content = intent.getStringExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_CONTENT)
        }
    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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

    private fun showCompleteButton() {
        menu?.findItem(R.id.menu_MemoReadWriteFragment_Complete)?.isVisible = true
    }

    private fun hideCompleteButton() {
        menu?.findItem(R.id.menu_MemoReadWriteFragment_Complete)?.isVisible = false
    }

    private fun showDeleteButton() {
        menu?.findItem(R.id.menu_MemoReadWriteFragment_Delete)?.isVisible = true
    }

    private fun hideDeleteButton() {
        menu?.findItem(R.id.menu_MemoReadWriteFragment_Delete)?.isVisible = false
    }

    private fun onDeleteButtonClick() {
        if (preferenceController.getPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK) == StockConfig.TRUE) {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.MemoListFragment_Delete_Check_Message))
                .setPositiveButton(R.string.Common_Ok) {_,_ ->
                    deleteMemo()
                }
                .setNegativeButton(R.string.Common_Cancel) {dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } else {
            deleteMemo()
        }
    }

    private fun deleteMemo() {
        databaseController.deleteData(id, Databases.TABLE_MEMO)
        val intent = Intent(mContext, MemoListFragment::class.java)
        intent.putExtra(MemoListFragment.INTENT_KEY_LIST_POSITION, memoListPosition)
        setResult(MemoListFragment.RESULT_DELETE, intent)
        finish()
    }

    private fun onCompleteButtonClick() {
        binding.apply {
            if (mode == MemoListFragment.MEMO_ADD_MODE) {
                if (etMemoReadWriteActivityTitle.text.toString() == "" && etMemoReadWriteActivityContent.text.toString() == "") {
                    setResult(MemoListFragment.RESULT_EMPTY)
                } else {
                    val date = Utils.getTodayYYYY_MM_DD()
                    val title = etMemoReadWriteActivityTitle.text.toString()
                    val content = etMemoReadWriteActivityContent.text.toString()
                    viewModel.requestAddMemoData(date, title, content)
                    setResult(RESULT_OK)
                }
                finish()
            } else if (mode == MemoListFragment.MEMO_UPDATE_MODE) {
                if (etMemoReadWriteActivityTitle.text.toString() == "" && etMemoReadWriteActivityContent.text.toString() == "") {
                    setResult(MemoListFragment.RESULT_EMPTY)
                } else {
                    val date = Utils.getTodayYYYY_MM_DD()
                    val title = etMemoReadWriteActivityTitle.text.toString()
                    val content = etMemoReadWriteActivityContent.text.toString()
                    viewModel.requestUpdateMemoData(id, date, title, content)
                    setResult(MemoListFragment.RESULT_UPDATE)
                }
                finish()
            }
        }
    }
}