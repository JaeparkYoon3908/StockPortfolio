package com.yjpapp.stockportfolio.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.FragmentTestBinding

class TestFragment: Fragment() {
    private val TAG = TestFragment::class.java.simpleName
    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!
    private val testViewModel: TestViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView.text = testViewModel.activityDataSendText
    }
}