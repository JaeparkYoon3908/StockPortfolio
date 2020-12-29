package com.yjpapp.stockportfolio.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.preference.PrefKey
import com.yjpapp.stockportfolio.ui.income_note.IncomeNoteFragment
import com.yjpapp.stockportfolio.ui.memo.MemoListFragment
import com.yjpapp.stockportfolio.ui.my_stock.MyStockFragment
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(R.layout.activity_main) {
    companion object{
        const val FRAGMENT_TAG_MY_STOCK = "my_stock"
        const val FRAGMENT_TAG_INCOME_NOTE = "income_note"
        const val FRAGMENT_TAG_MEMO = "memo"
        const val MENU_POSITION_MY_STOCK = 0
        const val MENU_POSITION_INCOME_NOTE = 1
        const val MENU_POSITION_MEMO = 2
        var menu_position = MENU_POSITION_MY_STOCK
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initLayout()
    }

    override fun initData() {
        preferenceController.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, "false")
    }

    override fun initLayout() {
        setSupportActionBar(toolbar_IncomeNoteActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val myStockFragment = MyStockFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.cons_MainActivity_fragment, myStockFragment)
            .addToBackStack(null)
            .commit()
        lin_MainActivity_BottomMenu_MyStock.setOnClickListener(onClickListener)
        lin_MainActivity_BottomMenu_IncomeNote.setOnClickListener(onClickListener)
        lin_MainActivity_BottomMenu_Memo.setOnClickListener(onClickListener)

    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when(view?.id){
            R.id.lin_MainActivity_BottomMenu_MyStock -> {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                val myStockFragment = MyStockFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cons_MainActivity_fragment, myStockFragment, FRAGMENT_TAG_MY_STOCK)
                    .addToBackStack(null)
                    .commit()
                txt_MainActivity_Title.text = getString(R.string.MyStockFragment_Title)
                menu_position = MENU_POSITION_MY_STOCK

            }
            R.id.lin_MainActivity_BottomMenu_IncomeNote -> {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                val incomeNoteFragment = IncomeNoteFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cons_MainActivity_fragment, incomeNoteFragment, FRAGMENT_TAG_INCOME_NOTE)
                    .addToBackStack(null)
                    .commit()
                txt_MainActivity_Title.text = getString(R.string.IncomeNoteFragment_Title)
                menu_position = MENU_POSITION_INCOME_NOTE
            }
            R.id.lin_MainActivity_BottomMenu_Memo -> {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                val memoListFragment = MemoListFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.cons_MainActivity_fragment, memoListFragment, FRAGMENT_TAG_MEMO)
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
            val memoListFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_MEMO)
            memoListFragment?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        if(menu_position == MENU_POSITION_INCOME_NOTE ||
            menu_position == MENU_POSITION_MEMO){
            super.onBackPressed()
            return
        }
        Utils.runBackPressAppCloseEvent(mContext, this)
    }
}