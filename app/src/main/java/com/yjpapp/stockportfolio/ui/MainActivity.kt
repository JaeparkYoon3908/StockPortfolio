package com.yjpapp.stockportfolio.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.income_note.IncomeNoteFragment
import com.yjpapp.stockportfolio.ui.memo.MemoListFragment
import com.yjpapp.stockportfolio.ui.my_stock.MyStockFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(R.layout.activity_main) {
    private val TAG_MY_STOCK = "my_stock"
    private val TAG_INCOME_NOTE = "income_note"
    private val TAG_MEMO = "memo"
    private val MENU_POSITION_MY_STOCK = 0
    private val MENU_POSITION_INCOME_NOTE = 1
    private val MENU_POSITION_MEMO = 2

    private var menu_position = MENU_POSITION_MY_STOCK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initLayout()

//        preferenceController.setPreference(SettingPrefKey.KEY_ViBRATION, "완전세게")
//        logcat(preferenceController.getPreference(SettingPrefKey.KEY_ViBRATION))
    }

    override fun initData() {

    }

    override fun initLayout() {
        setSupportActionBar(toolbar_IncomeNoteActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val fragmentShortcutEdit = IncomeNoteFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.cons_MainActivity_fragment, fragmentShortcutEdit)
            .addToBackStack(null)
            .commit()
        lin_MainActivity_BottomMenu_MyStock.setOnClickListener(onClickListener)
        lin_MainActivity_BottomMenu_IncomeNote.setOnClickListener(onClickListener)
        lin_MainActivity_BottomMenu_Memo.setOnClickListener(onClickListener)

    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when(view?.id){
            R.id.lin_MainActivity_BottomMenu_MyStock -> {
                val myStockFragment = MyStockFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cons_MainActivity_fragment, myStockFragment, TAG_MY_STOCK)
                    .addToBackStack(null)
                    .commit()
                txt_MainActivity_Title.text = getString(R.string.MyStockFragment_Title)
                menu_position = MENU_POSITION_MY_STOCK

            }
            R.id.lin_MainActivity_BottomMenu_IncomeNote -> {
                val incomeNoteFragment = IncomeNoteFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cons_MainActivity_fragment, incomeNoteFragment, TAG_INCOME_NOTE)
                    .addToBackStack(null)
                    .commit()
                txt_MainActivity_Title.text = getString(R.string.IncomeNoteFragment_Title)
                menu_position = MENU_POSITION_INCOME_NOTE
            }
            R.id.lin_MainActivity_BottomMenu_Memo -> {
                val memoListFragment = MemoListFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cons_MainActivity_fragment, memoListFragment, TAG_MEMO)
                    .addToBackStack(null)
                    .commit()
                txt_MainActivity_Title.text = getString(R.string.MemoListFragment_Title)
                menu_position = MENU_POSITION_MEMO
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(menu_position == MENU_POSITION_MEMO){
            val memoListFragment = supportFragmentManager.findFragmentByTag(TAG_MEMO)
            memoListFragment?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //TODO 뒤로가기 버튼 누를 때 한번 더 묻는 코드 작성.
    }
}