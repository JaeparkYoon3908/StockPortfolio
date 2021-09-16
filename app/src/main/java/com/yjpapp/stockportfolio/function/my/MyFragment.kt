package com.yjpapp.stockportfolio.function.my

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVVMFragment
import com.yjpapp.stockportfolio.databinding.FragmentMyBinding
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import org.koin.android.ext.android.inject

class MyFragment : BaseMVVMFragment<FragmentMyBinding>() {
    private val myViewModel: MyViewModel by inject()
    private val preferenceController by lazy { PreferenceController.getInstance(mContext) }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initData()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my
    }

    private fun initLayout() {

    }

    private fun initData() {
        binding.apply {
            viewModel = myViewModel
            callBack = this@MyFragment.callBack
            lifecycleOwner = this@MyFragment
        }
    }

    interface CallBack {
        fun onClick(view: View)
        fun onSwitchClick(view: View)
    }

    private val callBack = object : CallBack {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.btn_logout -> {
                    //TODO 로그아웃
                }
                R.id.btn_member_off -> {
                    //TODO 회원 탈퇴
                }
            }
        }

        override fun onSwitchClick(view: View) {
            when (view.id) {
                R.id.switch_auto_refresh -> {
                    val isChecked = binding.switchAutoRefresh.isChecked
                    preferenceController.setPreference(PrefKey.KEY_SETTING_AUTO_REFRESH, isChecked)
                }
                R.id.switch_auto_add -> {
                    val isChecked = binding.switchAutoAdd.isChecked
                    preferenceController.setPreference(PrefKey.KEY_SETTING_AUTO_ADD, isChecked)
                }
                R.id.switch_show_delete_check -> {
                    val isChecked = binding.switchShowDeleteCheck.isChecked
                    preferenceController.setPreference(PrefKey.KEY_SETTING_SHOW_DELETE_CHECK, isChecked)
                }
            }
        }

    }
}