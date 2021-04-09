package com.yjpapp.stockportfolio.ui.memo

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.FragmentMemoListBinding
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

/**
 * 메모리스트 화면
 *
 * @author Yoon Jae-park
 * @since 2020.11
 */
class MemoListFragment : Fragment(), MemoListView {
    companion object {
        const val INTENT_KEY_MEMO_MODE = "INTENT_KEY_MEMO_MODE"
        const val INTENT_KEY_MEMO_INFO_ID = "INTENT_KEY_MEMO_INFO_ID"
        const val INTENT_KEY_MEMO_INFO_TITLE = "INTENT_KEY_MEMO_INFO_TITLE"
        const val INTENT_KEY_MEMO_INFO_CONTENT = "INTENT_KEY_MEMO_INFO_CONTENT"
        const val INTENT_KEY_LIST_POSITION = "INTENT_KEY_LIST_POSITION"

        const val MEMO_READ_MODE = "MEMO_READ_MODE" //메모 읽기 모드
        const val MEMO_ADD_MODE = "MEMO_WRITE_MODE" //새 메모 추가모드
        const val MEMO_UPDATE_MODE = "MEMO_UPDATE_MODE" //메모 읽기 모드

        const val REQUEST_ADD = 0
        const val REQUEST_READ = 1

        const val RESULT_EMPTY = 10000
        const val RESULT_DELETE = RESULT_EMPTY + 1
        const val RESULT_UPDATE = RESULT_EMPTY + 2
    }

    private lateinit var mContext: Context
    private lateinit var memoListPresenter: MemoListPresenter
    private lateinit var layoutManager: LinearLayoutManager

    private var _viewBinding: FragmentMemoListBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            memoListPresenter.onBackPressedClick(activity as Activity)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
//        mRootView = inflater.inflate(R.layout.fragment_memo_list, container, false)
        _viewBinding = FragmentMemoListBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initLayout()
    }

    override fun onResume() {
        super.onResume()
        memoListPresenter.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null

    }

    private fun initData() {
        memoListPresenter = MemoListPresenter(mContext, this)
    }

    private fun initLayout() {
        setHasOptionsMenu(true)
        initRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_OK -> {
                when (requestCode) {
                    REQUEST_ADD -> {
                        memoListPresenter.addMemoInfo()
                    }
                }
            }
            RESULT_EMPTY -> {
                val mode = data?.getStringExtra(INTENT_KEY_MEMO_MODE)
//                (activity as MainActivity).logcat("mode = $mode")
                Toasty.normal(mContext, getString(R.string.MemoListFragment_Empty_Data_Message), Toasty.LENGTH_LONG).show()
            }
            RESULT_DELETE, RESULT_UPDATE -> {
                val position = data?.getIntExtra(INTENT_KEY_LIST_POSITION, 0)!!
                memoListPresenter.updateMemoInfo()
            }

        }
    }

    private fun initRecyclerView() {
        val memoDataList = memoListPresenter.getAllMemoInfoList()
        layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
        viewBinding.apply {
            if (memoDataList.size != 0) {
                layoutManager.scrollToPosition(memoDataList.size - 1)
                txtMemoListFragmentGuideMessage.visibility = View.GONE
            } else {
                txtMemoListFragmentGuideMessage.visibility = View.VISIBLE
            }
            recyclerviewMemoListFragment.layoutManager = layoutManager

//        memoListAdapter = MemoListAdapter(memoDataList, memoListPresenter)
//        recyclerview_MemoListActivity.adapter = memoListAdapter
            recyclerviewMemoListFragment.itemAnimator = SlideInLeftAnimator()
        }

    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.menu_memo_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
//                finish()
            }
            R.id.menu_MemoListFragment_Add -> {
                memoListPresenter.onAddButtonClicked()
            }

            R.id.menu_MemoListFragment_Delete -> {
                showDeleteCheckDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //View Interface CallBack
    override fun startReadWriteActivityForResult(intent: Intent, requestCode: Int) {
        activity?.startActivityForResult(intent, requestCode)
    }

//    override fun onBackPressed() {
//        if(memoListAdapter?.getDeleteModeOn()!!){
//            setDeleteModeOff()
//        }else{
//            finish()
//        }
//    }

//    override fun setDeleteModeOff() {
//        showAddButton()
//        hideDeleteButton()
//    }

    override fun showAddButton() {
        menu?.findItem(R.id.menu_MemoListFragment_Add)?.isVisible = true
    }

    override fun hideAddButton() {
        menu?.findItem(R.id.menu_MemoListFragment_Add)?.isVisible = false
    }

    override fun showDeleteButton() {
        menu?.findItem(R.id.menu_MemoListFragment_Delete)?.isVisible = true
    }

    override fun hideDeleteButton() {
        menu?.findItem(R.id.menu_MemoListFragment_Delete)?.isVisible = false
    }

    override fun showGuideMessage() {
        viewBinding.apply {
            txtMemoListFragmentGuideMessage.visibility = View.VISIBLE
        }

    }

    override fun hideGuideMessage() {
        val memoDataList = memoListPresenter.getAllMemoInfoList()
        viewBinding.apply {
            txtMemoListFragmentGuideMessage.visibility = View.GONE
        }
        layoutManager.scrollToPosition(memoDataList.size - 1)
    }

    override fun showDeleteCheckDialog() {
        AlertDialog.Builder(mContext)
                .setMessage(mContext.getString(R.string.MemoListFragment_Delete_Check_Message))
                .setPositiveButton(R.string.Common_Ok) { _, _ ->
                    memoListPresenter.deleteMemoInfo()
                }
                .setNegativeButton(R.string.Common_Cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    override fun setAdapter(memoListAdapter: MemoListAdapter) {
        viewBinding.apply {
            recyclerviewMemoListFragment.adapter = memoListAdapter
        }
    }
}