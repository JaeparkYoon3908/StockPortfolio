package com.yjpapp.stockportfolio.ui.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.MemoInfo
import com.yjpapp.stockportfolio.ui.MainActivity
import com.yjpapp.stockportfolio.ui.activity.MemoReadWriteActivity
import com.yjpapp.stockportfolio.ui.adapter.MemoListAdapter
import com.yjpapp.stockportfolio.ui.presenter.MemoListPresenter
import com.yjpapp.stockportfolio.ui.view.MemoListView
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

class MemoListFragment : Fragment(), MemoListAdapter.MemoActivityCallBack, MemoListView {
    companion object {
        const val INTENT_KEY_MEMO_MODE = "INTENT_KEY_MEMO_MODE"
        const val INTENT_KEY_MEMO_INFO_ID = "INTENT_KEY_MEMO_INFO_ID"
        const val INTENT_KEY_MEMO_INFO_TITLE = "INTENT_KEY_MEMO_INFO_TITLE"
        const val INTENT_KEY_MEMO_INFO_CONTENT = "INTENT_KEY_MEMO_INFO_CONTENT"
        const val INTENT_KEY_LIST_POSITION = "INTENT_KEY_LIST_POSITION"

        const val MEMO_READ_MODE = "MEMO_READ_MODE" //메모 읽기 모드
        const val MEMO_ADD_MODE = "MEMO_WRITE_MODE" //새 메모 추가모드
        const val MEMO_UPDATE_MODE = "MEMO_UPDATE_MODE" //메모 읽기 모드 -? 업데잍 모드

        const val REQUEST_ADD = 0
        const val REQUEST_READ = 1

        const val RESULT_EMPTY = 10000
        const val RESULT_DELETE = RESULT_EMPTY + 1
        const val RESULT_UPDATE = RESULT_EMPTY + 2
    }

    private lateinit var mContext: Context
    private lateinit var mRootView: View
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var memoListPresenter: MemoListPresenter

    private lateinit var layoutManager: LinearLayoutManager
    private var memoListAdapter: MemoListAdapter? = null

    private lateinit var toolbar_MemoListActivity: Toolbar
    private lateinit var txt_MemoListActivity_GuideMessage: TextView
    private lateinit var recyclerview_MemoListActivity: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (memoListAdapter?.isDeleteModeOn()!!) {
                    setDeleteModeOff()
                } else {
                    Utils.runBackPressAppCloseEvent(mContext, activity as Activity)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mRootView = inflater.inflate(R.layout.fragment_memo_list, container, false)
        initData()
        initLayout()
        return mRootView
    }

    private fun initData() {
        memoListPresenter = MemoListPresenter(mContext, this)
    }

    private fun initLayout() {
        setHasOptionsMenu(true)

        txt_MemoListActivity_GuideMessage = mRootView.findViewById(R.id.txt_MemoListFragment_GuideMessage)
        recyclerview_MemoListActivity = mRootView.findViewById(R.id.recyclerview_MemoListFragment)
        initRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_OK -> {
                when (requestCode) {
                    REQUEST_ADD -> {
                        // TODO refresh 되는 상황 실질적 DB Add는 ReadWriteActivity에서 하는듯함
                        memoListPresenter.addMemoInfo()
                    }
                }
            }

            RESULT_EMPTY -> {
                val mode = data?.getStringExtra(INTENT_KEY_MEMO_MODE)
                (activity as MainActivity).logcat("mode = $mode")
                Toasty.normal(mContext, "내용이 없어 메모를 저장하지 않습니다.", Toasty.LENGTH_LONG).show()
            }
            RESULT_DELETE -> {
                val position = data?.getIntExtra(INTENT_KEY_LIST_POSITION, 0)!!
                memoListPresenter.deleteMemoInfo()
            }

            RESULT_UPDATE -> {
                memoListPresenter.updateMemoInfo()

            }
        }
    }

    //Adapter CallBack
    override fun onMemoListLongClicked(position: Int) {
        if (memoListAdapter?.isDeleteModeOn()!!) {
            hideAddButton()
            showDeleteButton()
            memoListPresenter.updateDeleteCheck(position, true)
        } else {
            showAddButton()
            hideDeleteButton()
        }
    }

    override fun onMemoListClicked(position: Int) {
        val memoDataList = memoListPresenter.getAllMemoInfoList()
        val intent = Intent(mContext, MemoReadWriteActivity::class.java)
        intent.putExtra(INTENT_KEY_LIST_POSITION, position)
        intent.putExtra(INTENT_KEY_MEMO_INFO_ID, memoDataList[position]?.id)
        intent.putExtra(INTENT_KEY_MEMO_INFO_TITLE, memoDataList[position]?.title)
        intent.putExtra(INTENT_KEY_MEMO_INFO_CONTENT,
                memoDataList[position]?.content)
        intent.putExtra(INTENT_KEY_MEMO_MODE, MEMO_READ_MODE)

        activity?.startActivityForResult(intent, REQUEST_READ)
    }

    override fun onMemoDeleteCheck(position: Int, deleteCheck: Boolean) {
        memoListPresenter.updateDeleteCheck(position, deleteCheck)
    }

    private fun initRecyclerView() {
        val memoDataList = memoListPresenter.getAllMemoInfoList()
        layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
        if (memoDataList.size != 0) {
            layoutManager.scrollToPosition(memoDataList.size - 1)
            txt_MemoListActivity_GuideMessage.visibility = View.GONE
        } else {
            txt_MemoListActivity_GuideMessage.visibility = View.VISIBLE
        }
        recyclerview_MemoListActivity.layoutManager = layoutManager

        memoListAdapter = MemoListAdapter(memoDataList, this)
        recyclerview_MemoListActivity.adapter = memoListAdapter
        recyclerview_MemoListActivity.itemAnimator = SlideInLeftAnimator()
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
                memoListPresenter.onDeleteButtonClicked()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //View Interface CallBack
    override fun startMemoReadWriteActivity() {
        val intent = Intent(mContext, MemoReadWriteActivity::class.java)
        intent.putExtra(INTENT_KEY_MEMO_MODE, MEMO_ADD_MODE)
        activity?.startActivityForResult(intent, REQUEST_ADD)
    }

    override fun refreshMemoListData(updateMemoList: ArrayList<MemoInfo?>) {

    }

//    override fun onBackPressed() {
//        if(memoListAdapter?.getDeleteModeOn()!!){
//            setDeleteModeOff()
//        }else{
//            finish()
//        }
//    }

    override fun setDeleteModeOff() {
        memoListAdapter?.setDeleteModeOn(false)
        memoListAdapter?.notifyDataSetChanged()
        showAddButton()
        hideDeleteButton()
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
        txt_MemoListActivity_GuideMessage.visibility = View.VISIBLE
    }

    override fun hideGuideMessage() {
        val memoDataList = memoListPresenter.getAllMemoInfoList()
        txt_MemoListActivity_GuideMessage.visibility = View.GONE
        layoutManager.scrollToPosition(memoDataList.size - 1)
    }

    override fun addMemoListView(updateMemoList: ArrayList<MemoInfo?>) {
        memoListAdapter?.setMemoListData(updateMemoList)
        memoListAdapter?.notifyItemInserted(updateMemoList.size - 1)
    }

    override fun updateMemoListView(updateMemoList: ArrayList<MemoInfo?>) {
        val memoDataList = memoListPresenter.getAllMemoInfoList()
        memoListAdapter?.setMemoListData(memoDataList)
        memoListAdapter?.notifyDataSetChanged()
    }

    override fun deleteMemoListView(updateMemoList: ArrayList<MemoInfo?>, position: Int) {
        memoListAdapter?.setMemoListData(updateMemoList)
        memoListAdapter?.notifyItemRemoved(position)
        memoListAdapter?.notifyDataSetChanged()
        if (updateMemoList.size == 0) {
            showGuideMessage()
        }
    }


}