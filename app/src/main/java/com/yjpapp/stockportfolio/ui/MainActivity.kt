package com.yjpapp.stockportfolio.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.preference.PrefKey
import com.yjpapp.stockportfolio.ui.fragment.IncomeNoteFragment
import com.yjpapp.stockportfolio.ui.fragment.MemoListFragment
import com.yjpapp.stockportfolio.ui.fragment.MyStockFragment
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.activity_main.*


class  MainActivity : BaseActivity(R.layout.activity_main) {
    companion object{
        const val FRAGMENT_TAG_MY_STOCK = "my_stock"
        const val FRAGMENT_TAG_INCOME_NOTE = "income_note"
        const val FRAGMENT_TAG_MEMO_LIST = "memo_list"
    }
    private val myStockFragment = MyStockFragment()
    private val incomeNoteFragment = IncomeNoteFragment()
    private val memoListFragment = MemoListFragment()
    private var currentFragment: androidx.fragment.app.Fragment = myStockFragment

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

        lin_MainActivity_BottomMenu_MyStock.setOnClickListener(onClickListener)
        lin_MainActivity_BottomMenu_IncomeNote.setOnClickListener(onClickListener)
        lin_MainActivity_BottomMenu_Memo.setOnClickListener(onClickListener)

        startFirstFragment()
    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when(view?.id){
            R.id.lin_MainActivity_BottomMenu_MyStock -> {
                supportFragmentManager.beginTransaction().hide(currentFragment).show(myStockFragment).commit()
                currentFragment = myStockFragment
                preferenceController.setPreference(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION, FRAGMENT_TAG_MY_STOCK)
                txt_MainActivity_Title.text = getString(R.string.MyStockFragment_Title)
            }
            R.id.lin_MainActivity_BottomMenu_IncomeNote -> {
                supportFragmentManager.beginTransaction().hide(currentFragment).show(incomeNoteFragment).commit()
                currentFragment = incomeNoteFragment
                preferenceController.setPreference(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION, FRAGMENT_TAG_INCOME_NOTE)
                txt_MainActivity_Title.text = getString(R.string.IncomeNoteFragment_Title)
            }
            R.id.lin_MainActivity_BottomMenu_Memo -> {
                supportFragmentManager.beginTransaction().hide(currentFragment).show(memoListFragment).commit()
                currentFragment = memoListFragment
                preferenceController.setPreference(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION, FRAGMENT_TAG_MEMO_LIST)
                txt_MainActivity_Title.text = getString(R.string.MemoListFragment_Title)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(currentFragment == memoListFragment){
            val memoListFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_MEMO_LIST)
            memoListFragment?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        if(currentFragment == incomeNoteFragment ||
                currentFragment == memoListFragment){
            super.onBackPressed()
            return
        }
        Utils.runBackPressAppCloseEvent(mContext, this)
    }

    private fun startFirstFragment(){
        supportFragmentManager.run {
            if(preferenceController.isExists(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION)){
                when (preferenceController.getPreference(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION)){
                    FRAGMENT_TAG_MY_STOCK -> {
                        beginTransaction()
                            .add(R.id.cons_MainActivity_fragment,myStockFragment, FRAGMENT_TAG_MY_STOCK)
                            .commit()
                        beginTransaction()
                            .add(R.id.cons_MainActivity_fragment, incomeNoteFragment, FRAGMENT_TAG_INCOME_NOTE)
                            .hide(incomeNoteFragment)
                            .commit()
                        beginTransaction()
                            .add(R.id.cons_MainActivity_fragment, memoListFragment, FRAGMENT_TAG_MEMO_LIST)
                            .hide(memoListFragment)
                            .commit()
                        currentFragment = myStockFragment
                        txt_MainActivity_Title.text = getString(R.string.MyStockFragment_Title)
                    }
                    FRAGMENT_TAG_INCOME_NOTE -> {
                        beginTransaction()
                            .add(R.id.cons_MainActivity_fragment,myStockFragment, FRAGMENT_TAG_MY_STOCK)
                            .hide(myStockFragment)
                            .commit()
                        beginTransaction()
                            .add(R.id.cons_MainActivity_fragment, incomeNoteFragment, FRAGMENT_TAG_INCOME_NOTE)
                            .commit()
                        beginTransaction()
                            .add(R.id.cons_MainActivity_fragment, memoListFragment, FRAGMENT_TAG_MEMO_LIST)
                            .hide(memoListFragment)
                            .commit()
                        currentFragment = incomeNoteFragment
                        txt_MainActivity_Title.text = getString(R.string.IncomeNoteFragment_Title)
                    }
                    FRAGMENT_TAG_MEMO_LIST -> {
                        beginTransaction()
                            .add(R.id.cons_MainActivity_fragment,myStockFragment, FRAGMENT_TAG_MY_STOCK)
                            .hide(myStockFragment)
                            .commit()
                        beginTransaction()
                            .add(R.id.cons_MainActivity_fragment, incomeNoteFragment, FRAGMENT_TAG_INCOME_NOTE)
                            .hide(incomeNoteFragment)
                            .commit()
                        beginTransaction()
                            .add(R.id.cons_MainActivity_fragment, memoListFragment, FRAGMENT_TAG_MEMO_LIST)
                            .commit()
                        currentFragment = memoListFragment
                        txt_MainActivity_Title.text = getString(R.string.MemoListFragment_Title)
                    }
                }

            }else{
                beginTransaction()
                    .add(R.id.cons_MainActivity_fragment,myStockFragment, FRAGMENT_TAG_MY_STOCK)
                    .commit()
                beginTransaction()
                    .add(R.id.cons_MainActivity_fragment, incomeNoteFragment, FRAGMENT_TAG_INCOME_NOTE)
                    .hide(incomeNoteFragment)
                    .commit()
                beginTransaction()
                    .add(R.id.cons_MainActivity_fragment, memoListFragment, FRAGMENT_TAG_MEMO_LIST)
                    .hide(memoListFragment)
                    .commit()
                currentFragment = myStockFragment
                txt_MainActivity_Title.text = getString(R.string.MyStockFragment_Title)
                preferenceController.setPreference(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION, FRAGMENT_TAG_MY_STOCK)
            }
        }
    }

}