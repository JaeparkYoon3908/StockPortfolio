package com.yjpapp.stockportfolio.ui.income_note

import android.os.Bundle
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_new_main.*


class MainActivity : BaseActivity(R.layout.activity_new_main) {

    private val MENU_POSITION_MY_STOCK = 0
    private val MENU_POSITION_INCOME_NOTE = 1
    private val MENU_POSITION_MEMO = 2
    private var menu_position = MENU_POSITION_MY_STOCK

//    private var incomeNoteListAdapter: IncomeNoteListAdapter? = null
//    private var allPortfolioList: ArrayList<PortfolioInfo?>? = null
//    private var insertMode: Boolean = false
//    private var editSelectPosition = 0

    private lateinit var mainFragmentAdapter: MainFragmentAdapter

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
            .replace(R.id.cons_MainActivity_fragment,
                fragmentShortcutEdit) // container : Activity에 있는 부모 Layout
            .addToBackStack(null)
            .commit()
    }
}