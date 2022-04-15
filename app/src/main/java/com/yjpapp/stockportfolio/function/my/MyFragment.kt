package com.yjpapp.stockportfolio.function.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yjpapp.stockportfolio.BuildConfig
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.dialog.CommonTwoBtnDialog
import com.yjpapp.stockportfolio.databinding.FragmentMyBinding
import com.yjpapp.stockportfolio.extension.repeatOnStarted
import com.yjpapp.stockportfolio.function.MainActivity
import com.yjpapp.stockportfolio.function.login.LoginActivity
import com.yjpapp.stockportfolio.common.dialog.CommonDialogManager
import com.yjpapp.stockportfolio.common.dialog.CommonOneBtnDialog
import com.yjpapp.stockportfolio.test.TestActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 마이 화면
 * 디자인 패턴 : MVVM
 * @author Yoon Jae-park
 * @since 2021.09
 */
@AndroidEntryPoint
class MyFragment : Fragment() {
    private val TAG = MyFragment::class.java.simpleName
    private var _binding: FragmentMyBinding? = null
    private val binding: FragmentMyBinding get() = _binding!!
    private val myViewModel: MyViewModel by viewModels()
    private var mainActivity: MainActivity? = null
    override fun onAttach(context: Context) {
        try {
            mainActivity = requireActivity() as MainActivity
        } catch (e: ClassCastException) {
            e.stackTrace
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    private fun initData() {
        binding.apply {
            viewModel = myViewModel
            callBack = this@MyFragment.callBack
            lifecycleOwner = this@MyFragment
        }
        lifecycleScope.launch {
            repeatOnStarted {
                myViewModel.uiState.collect { handleEvent(it) }
            }
        }
    }

    private fun handleEvent(event: MyViewModel.Event) {
        when (event) {
            is MyViewModel.Event.StartLoginActivity -> {
                startLoginActivity()
                requireActivity().finish()
            }
            is MyViewModel.Event.StartLoadingAnimation -> {
                mainActivity?.startLoadingAnimation()
            }
            is MyViewModel.Event.StopLoadingAnimation -> {
                mainActivity?.stopLoadingAnimation()
            }
            is MyViewModel.Event.ResponseServerError -> {
                if (myViewModel.isDialogShowing) {
                    return
                }
                val fragmentDialog = CommonOneBtnDialog(
                    requireContext(),
                    CommonOneBtnDialog.CommonOneBtnData(
                        noticeText = event.msg,
                        btnText = getString(R.string.Common_Ok),
                        btnListener = { _: View, dialog ->
                            dialog.dismiss()
                            myViewModel.isDialogShowing = false
                        }
                    )
                )
                fragmentDialog.show(requireActivity().supportFragmentManager, TAG)
                myViewModel.isDialogShowing = true
            }
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
                    CommonTwoBtnDialog(requireContext(), CommonTwoBtnDialog.CommonTwoBtnData(
                        noticeText = requireContext().getString(R.string.My_Msg_Logout_Check),
                        leftBtnText = requireContext().getString(R.string.Common_Cancel),
                        rightBtnText = requireContext().getString(R.string.Common_Ok),
                        leftBtnListener = { _: View, dialog: CommonTwoBtnDialog ->
                            dialog.dismiss()
                        },
                        rightBtnListener = { _: View, dialog: CommonTwoBtnDialog ->
                            myViewModel.requestLogout()
                            dialog.dismiss()
                        }
                    )).show()
                }
                R.id.btn_member_off -> {
                    CommonTwoBtnDialog(requireContext(), CommonTwoBtnDialog.CommonTwoBtnData(
                        noticeText = requireContext().getString(R.string.My_Msg_Member_Off_Check),
                        leftBtnText = requireContext().getString(R.string.Common_Cancel),
                        rightBtnText = requireContext().getString(R.string.Common_Ok),
                        leftBtnListener = { _: View, dialog: CommonTwoBtnDialog ->
                            dialog.dismiss()

                        },
                        rightBtnListener = { _: View, dialog: CommonTwoBtnDialog ->
                            myViewModel.requestMemberOff()
                            dialog.dismiss()
                        }
                    )).show()
                }
                R.id.img_login_type_icon -> {
                    if (BuildConfig.DEBUG) {
                        val intent = Intent(requireContext(), TestActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

        override fun onSwitchClick(view: View) {
            when (view.id) {
                //자동 로그인
                R.id.switch_auto_login -> {
                    val isChecked = binding.switchAutoLogin.isChecked
                    myViewModel.requestSetAutoLogin(isChecked)
                }
                //수익노트 자동 새로 고침
                R.id.switch_my_stock_auto_refresh -> {
                    val isChecked = binding.switchMyStockAutoRefresh.isChecked
                    myViewModel.requestMyStockSetAutoRefresh(isChecked)
                }
                //나의 주식에서 수익노트로 자동추가
                R.id.switch_my_stock_auto_add -> {
                    val isChecked = binding.switchMyStockAutoAdd.isChecked
                    myViewModel.requestMyStockAutoAdd(isChecked)
                }
                //나의 주식 삭제 시 삭제 확인 띄우기
                R.id.switch_my_stock_show_delete_check -> {
                    val isChecked = binding.switchMyStockShowDeleteCheck.isChecked
                    myViewModel.requestMyStockShowDeleteCheck(isChecked)
                }
                //수익 노트 삭제 시 삭제 확인 띄우기
                R.id.switch_income_note_show_delete_check -> {
                    val isChecked = binding.switchIncomeNoteShowDeleteCheck.isChecked
                    myViewModel.requestIncomeNoteShowDeleteCheck(isChecked)
                }
                //메모 삭제 시 확인 띄우기
                R.id.switch_memo_show_delete_check -> {
                    val isChecked = binding.switchMemoShowDeleteCheck.isChecked
                    myViewModel.requestMemoShowDeleteCheck(isChecked)
                }
                //메모 삭제 모드 진입 시 진동 켜기
                R.id.switch_memo_vibrate_off -> {
                    val isChecked = binding.switchMemoVibrateOff.isChecked
                    myViewModel.requestMemoVibrateOff(isChecked)
                }
            }
        }
    }

    private fun startLoginActivity() {
        Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            requireContext().startActivity(this)
        }
    }

}