package com.yjpapp.stockportfolio.function

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.databinding.ActivityMainBinding
import com.yjpapp.stockportfolio.function.incomenote.IncomeNoteFragment
import com.yjpapp.stockportfolio.function.memo.MemoListFragment
import com.yjpapp.stockportfolio.function.my.MyFragment
import com.yjpapp.stockportfolio.function.mystock.MyStockComposeFragment
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main
 * 디자인 패턴 : MVP
 * @author Yoon Jae-park
 * @since 2020.12
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    companion object {
        const val FRAGMENT_TAG_MY_STOCK = "my_stock"
        const val FRAGMENT_TAG_INCOME_NOTE = "income_note"
        const val FRAGMENT_TAG_MEMO_LIST = "memo_list"
        const val FRAGMENT_TAG_MY = "my"
    }

    private val myStockFragment = MyStockComposeFragment()
    private val incomeNoteFragment = IncomeNoteFragment()
    private val memoListFragment = MemoListFragment()
    private val myFragment = MyFragment()
    private var currentFragment: Fragment = myStockFragment
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initLayout()
    }

    private fun initData() {
        viewModel.requestSetPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, "false")
    }

    private fun initLayout() {
        binding.apply {
            setSupportActionBar(toolbarMainActivity)
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.cons_MainActivity_fragment, myStockFragment, FRAGMENT_TAG_MY_STOCK)
            .add(R.id.cons_MainActivity_fragment, incomeNoteFragment, FRAGMENT_TAG_INCOME_NOTE)
            .add(R.id.cons_MainActivity_fragment, memoListFragment, FRAGMENT_TAG_MEMO_LIST)
            .add(R.id.cons_MainActivity_fragment, myFragment, FRAGMENT_TAG_MY)
            .hide(myStockFragment)
            .hide(incomeNoteFragment)
            .hide(memoListFragment)
            .hide(myFragment)
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        startFirstFragment()
    }

    override fun onBackPressed() {
        if (currentFragment == incomeNoteFragment ||
            currentFragment == memoListFragment
        ) {
            super.onBackPressed()
            return
        }
        viewModel.runBackPressAppCloseEvent(applicationContext, this)
    }

    private fun startFirstFragment() {
        if (viewModel.requestIsExistPreference(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION)) {
            when (viewModel.requestGetPreference(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION)) {
                FRAGMENT_TAG_MY_STOCK -> {
                    showFragment(myStockFragment)
                    switchingBottomIconMyStock()
                }
                FRAGMENT_TAG_INCOME_NOTE -> {
                    showFragment(incomeNoteFragment)
                    switchingBottomIconIncomeNote()
                }
                FRAGMENT_TAG_MEMO_LIST -> {
                    showFragment(memoListFragment)
                    switchingBottomIconMemo()
                }
                FRAGMENT_TAG_MY -> {
                    showFragment(myFragment)
                    switchingBottomIconMy()
                }
            }
        } else {
            //TODO 나의 주식 릴리즈 이후 오픈
//            showFragment(myStockFragment)
//            switchingBottomIconMyStock()
            showFragment(incomeNoteFragment)
            switchingBottomIconIncomeNote()
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(currentFragment)
            .show(fragment)
            .commit()
        currentFragment = fragment
        when (fragment) {
            myStockFragment -> {
                binding.apply {
                    txtMainActivityTitle.text = getString(R.string.MyStockFragment_Title)
                }
                viewModel.requestSetPreference(
                    PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION,
                    FRAGMENT_TAG_MY_STOCK
                )
            }

            incomeNoteFragment -> {
                viewModel.requestSetPreference(
                    PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION,
                    FRAGMENT_TAG_INCOME_NOTE
                )
                binding.apply {
                    txtMainActivityTitle.text = getString(R.string.IncomeNoteFragment_Title)
                }
            }

            memoListFragment -> {
                viewModel.requestSetPreference(
                    PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION,
                    FRAGMENT_TAG_MEMO_LIST
                )
                binding.apply {
                    txtMainActivityTitle.text = getString(R.string.MemoListFragment_Title)
                }
            }

            myFragment -> {
                viewModel.requestSetPreference(
                    PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION,
                    FRAGMENT_TAG_MY
                )
                binding.apply {
                    txtMainActivityTitle.text = getString(R.string.MyFragment_Title)
                }
            }
        }

    }

    fun clickBottomMenu(view: View?) {
        when (currentFragment) {
            myStockFragment -> {
                if (view?.id == R.id.lin_MainActivity_BottomMenu_MyStock) return
                switchingBottomIconMyStock()
            }
            incomeNoteFragment -> {
                if (view?.id == R.id.lin_MainActivity_BottomMenu_IncomeNote) return
                switchingBottomIconIncomeNote()
            }
            memoListFragment -> {
                if (view?.id == R.id.lin_MainActivity_BottomMenu_Memo) return
                switchingBottomIconMemo()
            }
            myFragment -> {
                if (view?.id == R.id.lin_MainActivity_BottomMenu_My) return
                switchingBottomIconMy()
            }
        }
        when (view?.id) {
            R.id.lin_MainActivity_BottomMenu_MyStock -> {
                showFragment(myStockFragment)
                switchingBottomIconMyStock()
            }
            R.id.lin_MainActivity_BottomMenu_IncomeNote -> {
                showFragment(incomeNoteFragment)
                switchingBottomIconIncomeNote()
            }
            R.id.lin_MainActivity_BottomMenu_Memo -> {
                showFragment(memoListFragment)
                switchingBottomIconMemo()
            }
            R.id.lin_MainActivity_BottomMenu_My -> {
                showFragment(myFragment)
                switchingBottomIconMy()
            }
        }
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

    private fun switchingBottomIconMy() {
        binding.imgMainActivityBottomMenuAd.isSelected =
            !binding.imgMainActivityBottomMenuAd.isSelected
        binding.txtMainActivityBottomMenuAd.isSelected =
            !binding.txtMainActivityBottomMenuAd.isSelected
    }
}