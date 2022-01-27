package com.yjpapp.stockportfolio.function.memo.detail

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.constance.StockConfig
import com.yjpapp.stockportfolio.databinding.ActivityMemoReadWriteBinding
import com.yjpapp.stockportfolio.dialog.CommonTwoBtnDialog
import com.yjpapp.stockportfolio.function.memo.MemoListFragment
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.util.Utils
import dagger.hilt.android.AndroidEntryPoint

/**
 * 메모 읽기 및 추가 화면
 * 디자인 패턴 : MVVM
 * @author Yoon Jae-park
 * @since 2020.12.27
 */
@AndroidEntryPoint
class MemoReadWriteActivity :
    BaseActivity<ActivityMemoReadWriteBinding>(R.layout.activity_memo_read_write) {
    private val activityViewModel: MemoReadWriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    override fun onBackPressed() {
        checkMemoWriteCancel()
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
        binding.viewModel = activityViewModel
        activityViewModel.apply {
            mode = intent.getStringExtra(MemoListFragment.INTENT_KEY_MEMO_MODE)
            if (mode == MemoListFragment.MEMO_READ_MODE) { // 읽기 모드
                memoListPosition = intent.getIntExtra(MemoListFragment.INTENT_KEY_LIST_POSITION, 0)
                id = intent.getIntExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_ID, 0)
                savedTitle = intent.getStringExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_TITLE)?: ""
                savedContent = intent.getStringExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_CONTENT)?: ""
            }
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
            if (activityViewModel.mode == MemoListFragment.MEMO_READ_MODE) {
                etMemoReadWriteActivityTitle.setText(activityViewModel.savedTitle)
                etMemoReadWriteActivityContent.setText(activityViewModel.savedContent)
                hideCompleteButton()
            } else {
                hideDeleteButton()
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                checkMemoWriteCancel()
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

    private val onFocusChangeListener = View.OnFocusChangeListener { _: View, _: Boolean ->
        if (activityViewModel.mode == MemoListFragment.MEMO_READ_MODE) {
            activityViewModel.mode = MemoListFragment.MEMO_UPDATE_MODE
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
        if (activityViewModel.requestGetPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK) == StockConfig.TRUE) {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.MemoListFragment_Delete_Check_Message))
                .setPositiveButton(R.string.Common_Ok) { _, _ ->
                    deleteMemo()
                }
                .setNegativeButton(R.string.Common_Cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } else {
            deleteMemo()
        }
    }

    private fun deleteMemo() {
        activityViewModel.requestDeleteMemoData()
        val intent = Intent(applicationContext, MemoListFragment::class.java)
        intent.putExtra(MemoListFragment.INTENT_KEY_LIST_POSITION, activityViewModel.memoListPosition)
        setResult(MemoListFragment.RESULT_DELETE, intent)
        finish()
    }

    private fun onCompleteButtonClick() {
        binding.apply {
            if (activityViewModel.mode == MemoListFragment.MEMO_ADD_MODE) {
                if (etMemoReadWriteActivityTitle.text.toString() == "" && etMemoReadWriteActivityContent.text.toString() == "") {
                    setResult(MemoListFragment.RESULT_EMPTY)
                } else {
                    val date = Utils.getTodayYYYY_MM_DD()
                    val title = etMemoReadWriteActivityTitle.text.toString()
                    val content = etMemoReadWriteActivityContent.text.toString()
                    activityViewModel.requestAddMemoData(date, title, content)
                    setResult(RESULT_OK)
                }
                finish()
            } else if (activityViewModel.mode == MemoListFragment.MEMO_UPDATE_MODE) {
                if (etMemoReadWriteActivityTitle.text.toString() == "" && etMemoReadWriteActivityContent.text.toString() == "") {
                    setResult(MemoListFragment.RESULT_EMPTY)
                } else {
                    val date = Utils.getTodayYYYY_MM_DD()
                    val title = etMemoReadWriteActivityTitle.text.toString()
                    val content = etMemoReadWriteActivityContent.text.toString()
                    activityViewModel.requestUpdateMemoData(date, title, content)
                    setResult(MemoListFragment.RESULT_UPDATE)
                }
                finish()
            }
        }
    }

    private fun checkMemoWriteCancel() {
        when (activityViewModel.mode) {
            MemoListFragment.MEMO_ADD_MODE -> {
                if (binding.etMemoReadWriteActivityTitle.text.toString().isNotEmpty() ||
                    binding.etMemoReadWriteActivityContent.text.toString().isNotEmpty()) {
                    showCheckActivityFinishDialog(getString(R.string.MemoListFragment_Msg_Check_Add_Mode_Cancel_Memo))
                } else {
                    finish()
                }
            }
            MemoListFragment.MEMO_READ_MODE -> {
                finish()
            }
            MemoListFragment.MEMO_UPDATE_MODE -> {
                if (activityViewModel.savedTitle != binding.etMemoReadWriteActivityTitle.text.toString() ||
                    activityViewModel.savedContent != binding.etMemoReadWriteActivityContent.text.toString()) {
                    showCheckActivityFinishDialog(getString(R.string.MemoListFragment_Msg_Check_Update_Mode_Cancel_Memo))
                } else {
                    finish()
                }
            }
        }
    }
    private fun showCheckActivityFinishDialog(noticeText: String) {
        CommonTwoBtnDialog(this, CommonTwoBtnDialog.CommonTwoBtnData(
            noticeText = noticeText,
            leftBtnText = getString(R.string.Common_Cancel),
            leftBtnListener = object : CommonTwoBtnDialog.OnClickListener {
                override fun onClick(view: View, dialog: CommonTwoBtnDialog) {
                    dialog.dismiss()
                }
            },
            rightBtnText = getString(R.string.Common_Ok),
            rightBtnListener = object : CommonTwoBtnDialog.OnClickListener {
                override fun onClick(view: View, dialog: CommonTwoBtnDialog) {
                    dialog.dismiss()
                    finish()
                }
            }
        )).show()
    }
}