package com.yjpapp.stockportfolio.function.memo

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.FragmentMemoListBinding
import com.yjpapp.stockportfolio.extension.repeatOnStarted
import com.yjpapp.stockportfolio.function.memo.detail.MemoReadWriteActivity
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

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
//    private lateinit var memoListPresenter: MemoListPresenter
    private lateinit var layoutManager: LinearLayoutManager
    private val memoListAdapter = MemoListAdapter(mutableListOf(), null).apply { setHasStableIds(true) }
    private val viewModel: MemoListViewModel by inject()

    private var _binding: FragmentMemoListBinding? = null
    private val binding get() = _binding!!

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (memoListAdapter.deleteModeOn) {
                memoListAdapter.deleteModeOn = false
                memoListAdapter.notifyDataSetChanged()
                showAddButton()
                hideDeleteButton()
            } else {
                Utils.runBackPressAppCloseEvent(mContext, activity as Activity)
            }
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memo_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    private fun initData() {
//        memoListPresenter = MemoListPresenter(mContext, this)
        viewModel.getAllMemoInfoList()
        //event handler
        lifecycleScope.launch {
            repeatOnStarted {
                viewModel.eventFlow.collect { event -> handleEvent(event) }
            }
        }
    }

    private fun initLayout() {
        setHasOptionsMenu(true)
        initRecyclerView()
    }

    private fun handleEvent(event: MemoListViewModel.Event) = when (event) {
        is MemoListViewModel.Event.SendToAllMemoListData -> {
            val memoList = event.data
            memoListAdapter.memoListData = memoList
            memoListAdapter.notifyDataSetChanged()
            if (memoList.size == 0) {
                showGuideMessage()
            } else {
                hideGuideMessage()
            }
        }
        is MemoListViewModel.Event.MemoListDataDeleteSuccess -> {
            val memoList = event.data
            memoListAdapter.deleteModeOn = false
            memoListAdapter.notifyDataSetChanged()
            showAddButton()
            hideDeleteButton()
            if (memoList.size == 0) {
                showGuideMessage()
            } else {
                hideGuideMessage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                when (requestCode) {
                    REQUEST_ADD -> {
                        viewModel.getAllMemoInfoList()
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
                viewModel.getAllMemoInfoList()
            }
        }
    }

    private fun initRecyclerView() {
        memoListAdapter.callBack = memoListAdapterCallBack
        val memoDataList = viewModel.allMemoListData
        layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
        binding.apply {
            if (memoDataList.size != 0) {
                layoutManager.scrollToPosition(memoDataList.size - 1)
                txtMemoListFragmentGuideMessage.visibility = View.GONE
            } else {
                txtMemoListFragmentGuideMessage.visibility = View.VISIBLE
            }
            recyclerviewMemoListFragment.adapter = memoListAdapter
            recyclerviewMemoListFragment.layoutManager = layoutManager
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
                val intent = Intent(mContext, MemoReadWriteActivity::class.java)
                intent.putExtra(INTENT_KEY_MEMO_MODE, MEMO_ADD_MODE)
                startReadWriteActivityForResult(intent, REQUEST_ADD)
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
        binding.apply {
            txtMemoListFragmentGuideMessage.visibility = View.VISIBLE
        }
    }

    override fun hideGuideMessage() {
        val memoDataList = viewModel.allMemoListData
        binding.apply {
            txtMemoListFragmentGuideMessage.visibility = View.GONE
        }
        layoutManager.scrollToPosition(memoDataList.size - 1)
    }

    override fun showDeleteCheckDialog() {
        AlertDialog.Builder(mContext)
                .setMessage(mContext.getString(R.string.MemoListFragment_Delete_Check_Message))
                .setPositiveButton(R.string.Common_Ok) { _, _ ->
                    viewModel.requestDeleteMemoInfo()
                }
                .setNegativeButton(R.string.Common_Cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    override fun setAdapter(memoListAdapter: MemoListAdapter) {
        binding.apply {
            recyclerviewMemoListFragment.adapter = memoListAdapter
        }
    }

    private val memoListAdapterCallBack = object : MemoListAdapter.CallBack {
        override fun onMemoListClicked(position: Int, imgMemoListCheck: Boolean) {
            if (memoListAdapter.deleteModeOn) {
                viewModel.requestUpdateDeleteCheck(position = position, deleteCheck = imgMemoListCheck)
            } else {
                val memoDataList = viewModel.allMemoListData
                val intent = Intent(mContext, MemoReadWriteActivity::class.java)
                intent.putExtra(INTENT_KEY_LIST_POSITION, position)
                intent.putExtra(INTENT_KEY_MEMO_INFO_ID, memoDataList[position].id)
                intent.putExtra(INTENT_KEY_MEMO_INFO_TITLE, memoDataList[position].title)
                intent.putExtra(
                    INTENT_KEY_MEMO_INFO_CONTENT,
                    memoDataList[position].content)
                intent.putExtra(INTENT_KEY_MEMO_MODE, MEMO_READ_MODE)
                startReadWriteActivityForResult(intent, REQUEST_READ)
            }
        }

        override fun onMemoListLongClicked(position: Int) {
            if (!memoListAdapter.deleteModeOn) {
                hideAddButton()
                showDeleteButton()
                viewModel.requestUpdateDeleteCheck(position, true)
            } else {
                showAddButton()
                hideDeleteButton()
            }
            Utils.runVibration(mContext, 100)
            memoListAdapter.deleteModeOn = !memoListAdapter.deleteModeOn
            viewModel.getAllMemoInfoList()
        }

        override fun onMemoDeleteCheckClicked(position: Int, deleteCheck: Boolean) {
            viewModel.requestUpdateDeleteCheck(position = position, deleteCheck = deleteCheck)
        }
    }
}