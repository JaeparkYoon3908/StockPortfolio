package com.yjpapp.stockportfolio.function.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVPActivity
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.databinding.ActivityMainBinding
import com.yjpapp.stockportfolio.function.my.MyFragment
import com.yjpapp.stockportfolio.function.incomenote.IncomeNoteFragment
import com.yjpapp.stockportfolio.function.memo.MemoListFragment
import com.yjpapp.stockportfolio.function.mystock.MyStockFragment
import com.yjpapp.stockportfolio.util.Utils

/**
 * Main
 * 디자인 패턴 : MVP
 * @author Yoon Jae-park
 * @since 2020.12
 */
class MainActivity : BaseMVPActivity<ActivityMainBinding>(), MainView {
    companion object {
        const val FRAGMENT_TAG_MY_STOCK = "my_stock"
        const val FRAGMENT_TAG_INCOME_NOTE = "income_note"
        const val FRAGMENT_TAG_MEMO_LIST = "memo_list"
        const val FRAGMENT_TAG_AD = "ad"
    }

    private val myStockFragment = MyStockFragment()
    private val incomeNoteFragment = IncomeNoteFragment()
    private val memoListFragment = MemoListFragment()
    private val adFragment = MyFragment()
    private var currentFragment: Fragment = myStockFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initData() {
        preferenceController.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, "false")
    }

    override fun initLayout() {
        binding.apply {
            setSupportActionBar(toolbarMainActivity)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

//        addFragment()
        startFirstFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (currentFragment == memoListFragment) {
            val memoListFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_MEMO_LIST)
            memoListFragment?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        if (currentFragment == incomeNoteFragment ||
            currentFragment == memoListFragment
        ) {
            super.onBackPressed()
            return
        }
        Utils.runBackPressAppCloseEvent(mContext, this)
    }

    private fun startFirstFragment() {
        if (preferenceController.isExists(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION)) {
            when (preferenceController.getPreference(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION)) {
                FRAGMENT_TAG_MY_STOCK -> {
                    showMyStock()
                    switchingBottomIconMyStock()
                }
                FRAGMENT_TAG_INCOME_NOTE -> {
                    showIncomeNote()
                    switchingBottomIconIncomeNote()
                }
                FRAGMENT_TAG_MEMO_LIST -> {
                    showMemoList()
                    switchingBottomIconMemo()
                }
                FRAGMENT_TAG_AD -> {
                    showAdFragment()
                    switchingBottomIconAd()
                }
            }
        } else {
            showMyStock()
            switchingBottomIconMyStock()
//            showIncomeNote()
//            switchingBottomIconIncomeNote()
        }
    }

//    override fun addFragment() {
//        supportFragmentManager.beginTransaction()
//            .add(R.id.cons_MainActivity_fragment, myStockFragment, FRAGMENT_TAG_MY_STOCK)
//            .add(R.id.cons_MainActivity_fragment, incomeNoteFragment, FRAGMENT_TAG_INCOME_NOTE)
//            .add(R.id.cons_MainActivity_fragment, memoListFragment, FRAGMENT_TAG_MEMO_LIST)
//            .hide(myStockFragment)
//            .hide(incomeNoteFragment)
//            .hide(memoListFragment)
//            .commit()
//    }

    override fun showMyStock() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.cons_MainActivity_fragment, myStockFragment, FRAGMENT_TAG_MY_STOCK)
            .commit()
        currentFragment = myStockFragment
        binding.apply {
            txtMainActivityTitle.text = getString(R.string.MyStockFragment_Title)
        }
        preferenceController.setPreference(
            PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION,
            FRAGMENT_TAG_MY_STOCK
        )
    }

//    override fun hideMyStock() {
//        supportFragmentManager.beginTransaction()
//            .hide(myStockFragment)
//            .commit()
//    }

    override fun showIncomeNote() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.cons_MainActivity_fragment, incomeNoteFragment, FRAGMENT_TAG_INCOME_NOTE)
            .commit()
        currentFragment = incomeNoteFragment
        preferenceController.setPreference(
            PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION,
            FRAGMENT_TAG_INCOME_NOTE
        )
        binding.apply {
            txtMainActivityTitle.text = getString(R.string.IncomeNoteFragment_Title)
        }
    }

//    override fun hideIncomeNote() {
//        supportFragmentManager.beginTransaction()
//            .hide(incomeNoteFragment)
//            .commit()
//    }

    override fun showMemoList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.cons_MainActivity_fragment, memoListFragment, FRAGMENT_TAG_MEMO_LIST)
            .commit()
        currentFragment = memoListFragment
        preferenceController.setPreference(
            PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION,
            FRAGMENT_TAG_MEMO_LIST
        )
        binding.apply {
            txtMainActivityTitle.text = getString(R.string.MemoListFragment_Title)
        }
    }

//    override fun hideMemoList() {
//        supportFragmentManager.beginTransaction()
//            .hide(memoListFragment)
//            .commit()
//    }

    override fun showAdFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.cons_MainActivity_fragment,
                adFragment,
                FRAGMENT_TAG_AD
            )
            .commit()
        currentFragment = adFragment
        preferenceController.setPreference(
            PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION,
            FRAGMENT_TAG_AD
        )
        binding.apply {
            txtMainActivityTitle.text = getString(R.string.MyFragment_Title)
        }
    }

    override fun hideAdFragment() {
        supportFragmentManager.beginTransaction()
            .hide(adFragment)
            .commit()
    }

    override fun hideCurrentFragment() {
        supportFragmentManager.beginTransaction()
            .hide(currentFragment)
            .commit()
    }

    override fun clickBottomMenu(view: View?) {
        when (currentFragment) {
            myStockFragment -> {
                switchingBottomIconMyStock()
            }
            incomeNoteFragment -> {
                switchingBottomIconIncomeNote()
            }
            memoListFragment -> {
                switchingBottomIconMemo()
            }
            adFragment -> {
                switchingBottomIconAd()
            }
        }
        when (view?.id) {
            R.id.lin_MainActivity_BottomMenu_MyStock -> {
                showMyStock()
                switchingBottomIconMyStock()
            }
            R.id.lin_MainActivity_BottomMenu_IncomeNote -> {
                showIncomeNote()
                switchingBottomIconIncomeNote()
            }
            R.id.lin_MainActivity_BottomMenu_Memo -> {
                showMemoList()
                switchingBottomIconMemo()
            }
            R.id.lin_MainActivity_BottomMenu_Ad -> {
                showAdFragment()
                switchingBottomIconAd()
            }
        }
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    //나의 주식 바텀 아이콘 on off 스위칭
    private fun switchingBottomIconMyStock() {
        binding.imgMainActivityBottomMenuMyStock.isSelected =
            !binding.imgMainActivityBottomMenuMyStock.isSelected
        binding.txtMainActivityBottomMenuMyStock.isSelected =
            !binding.txtMainActivityBottomMenuMyStock.isSelected
    }

    private fun switchingBottomIconIncomeNote() {
        binding.imgMainActivityBottomMenuIncomeNote.isSelected =
            !binding.imgMainActivityBottomMenuIncomeNote.isSelected
        binding.txtMainActivityBottomMenuIncomeNote.isSelected =
            !binding.txtMainActivityBottomMenuIncomeNote.isSelected
    }

    private fun switchingBottomIconMemo() {
        binding.imgMainActivityBottomMenuMemo.isSelected =
            !binding.imgMainActivityBottomMenuMemo.isSelected
        binding.txtMainActivityBottomMenuMemo.isSelected =
            !binding.txtMainActivityBottomMenuMemo.isSelected
    }

    private fun switchingBottomIconAd() {
        binding.imgMainActivityBottomMenuAd.isSelected =
            !binding.imgMainActivityBottomMenuAd.isSelected
        binding.txtMainActivityBottomMenuAd.isSelected =
            !binding.txtMainActivityBottomMenuAd.isSelected
    }
}