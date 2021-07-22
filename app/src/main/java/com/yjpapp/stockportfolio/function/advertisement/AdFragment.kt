package com.yjpapp.stockportfolio.function.advertisement

import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.rewarded.RewardedAd
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseMVVMFragment
import com.yjpapp.stockportfolio.databinding.FragmentAdBinding
import org.koin.android.ext.android.inject

class AdFragment : BaseMVVMFragment<FragmentAdBinding>() {

    private val adViewModel by lazy { AdViewModel(this.mContext) }
    private lateinit var rewardedAd: RewardedAd

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMyPhoneNum()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_ad
    }

    override fun setViewModel() {
        mDataBinding.viewModel = adViewModel
    }

    private fun setMyPhoneNum() {
        mDataBinding.etPhoneNum.setText("01048533908")
        adViewModel.phoneNum.value = mDataBinding.etPhoneNum.text.toString()
    }

    //    private fun setAdvertise(){
    //        MobileAds.initialize(this)
    //        rewardedAd = RewardedAd(this, "ca-app-pub-4132154272836445~8488670395")
    //        val activityContext: Activity = this@MainActivity
    //        val adCallback = object: RewardedAdCallback() {
    //            override fun onRewardedAdOpened() {
    //                // Ad opened.
    //            }
    //            override fun onRewardedAdClosed() {
    //                // Ad closed.
    //            }
    //            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
    //                // User earned reward.
    //            }
    //            override fun onRewardedAdFailedToShow(adError: AdError) {
    //                // Ad failed to display.
    //            }
    //        }
    //        rewardedAd.show(activityContext, adCallback)
    //    }
}