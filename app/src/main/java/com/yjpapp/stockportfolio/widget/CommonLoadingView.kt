package com.yjpapp.stockportfolio.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CommonLoadingViewBinding

class CommonLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var binding =
        CommonLoadingViewBinding.inflate(LayoutInflater.from(context), this, true)

    enum class LoadingType {
        NONE,
        PAGE,
        SELLER_PAGE
    }

    private var mAnimation: Animation? = null

    init {
        setLoadingType(LoadingType.SELLER_PAGE)
        setTouchEnable(false)
    }

    /*------------------------------------------------------------------------------------------------------------------------------
	 * Public Method
	 ------------------------------------------------------------------------------------------------------------------------------ */
    fun setLoadingType(animationType: LoadingType?) {
//        var loadingResId: Int = R.drawable.ic_loading
//        if (animationType == LoadingType.PAGE) {
        val loadingResId = R.drawable.ic_seller_page_loading
//        }
        binding.loadingImage.setBackgroundResource(loadingResId)
    }

    /**
     * Animation start
     */
    fun startAnimation() {
        mAnimation = AnimationUtils.loadAnimation(context, R.anim.page_animation)
        mAnimation?.interpolator = LinearInterpolator()
        postDelayed({
            if(mAnimation !== null) {
                binding.loadingImage.startAnimation(mAnimation)
            }
        }, 100)
    }

    fun stopAnimation() {
        if(mAnimation !== null) {
            binding.loadingImage.clearAnimation()
            binding.loadingImage.setBackgroundResource(android.R.color.transparent)
            mAnimation = null
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setTouchEnable(enable: Boolean) {
        if (enable) {
            setOnTouchListener(null)
        } else {
            setOnTouchListener { v, event -> true }
        }
    }
}