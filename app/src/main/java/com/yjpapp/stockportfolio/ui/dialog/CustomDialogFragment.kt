package com.yjpapp.stockportfolio.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.yjpapp.stockportfolio.R


class CustomDialogFragment : DialogFragment(), View.OnClickListener {
    private var mMainMsg: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mMainMsg = arguments?.getString(ARG_DIALOG_MAIN_MSG)
        }
    }

    override fun onResume() {
        super.onResume()
//        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, Utils.dpToPx(350))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_add_portfolio, null))
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun dismissDialog() {
        this.dismiss()
    }

    companion object {
        private const val TAG = "CustomDialogFragment"
        private const val ARG_DIALOG_MAIN_MSG = "dialog_main_msg"
        fun newInstance(mainMsg: String?): CustomDialogFragment {
            val bundle = Bundle()
            bundle.putString(ARG_DIALOG_MAIN_MSG, mainMsg)
            val fragment = CustomDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}