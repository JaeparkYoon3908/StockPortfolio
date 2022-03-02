package com.yjpapp.stockportfolio.function

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.databinding.ActivityMainBinding
import com.yjpapp.stockportfolio.function.incomenote.IncomeNoteFragment
import com.yjpapp.stockportfolio.function.memo.MemoListFragment
import com.yjpapp.stockportfolio.function.my.MyFragment
import com.yjpapp.stockportfolio.function.mystock.MyStockFragment
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.util.KeepStateNavigator
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main
 * 디자인 패턴 : MVP
 * @author Yoon Jae-park
 * @since 2020.12
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initLayout()
    }

    private fun initData() {
        viewModel.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, "false")
    }

    private fun initLayout() {
        binding.apply {
            setSupportActionBar(toolbarMainActivity)
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.consMainActivityFragment.id) as NavHostFragment

        val navController = navHostFragment.navController
        //Custom Navigator 추가
        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, binding.consMainActivityFragment.id)
        navController.navigatorProvider += navigator
        navController.setGraph(R.navigation.nav_main)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
//        if (currentFragment == incomeNoteFragment ||
//            currentFragment == memoListFragment
//        ) {
//            super.onBackPressed()
//            return
//        }
        viewModel.runBackPressAppCloseEvent(applicationContext, this)
    }


    fun startLoadingAnimation() {
        binding.viewMasking.visibility = View.VISIBLE
        binding.ivLoading.visibility = View.VISIBLE
        binding.ivLoading.startAnimation()
    }

    fun stopLoadingAnimation() {
        binding.viewMasking.visibility = View.GONE
        binding.ivLoading.visibility = View.GONE
        binding.ivLoading.stopAnimation()
    }
}