package com.yjpapp.stockportfolio.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CommonLoadingViewBinding

class CommonLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var binding =
        CommonLoadingViewBinding.inflate(LayoutInflater.from(context), this, true)

    enum class LoadingColorType {
        WHITE,
        BLACK
    }

    private var mAnimation: Animation? = null

    init {
        setLoadingImageColor(LoadingColorType.WHITE) //default
        setTouchEnable(false)
        startAnimation()
    }

    /*------------------------------------------------------------------------------------------------------------------------------
	 * Public Method
	 -----------------------a------------------------------------------------------------------------------------------------------- */
    fun setLoadingImageColor(animationColorType: LoadingColorType) {
        when (animationColorType) {
            LoadingColorType.WHITE -> {
                binding.loadingImage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ffffffff"))
            }
            LoadingColorType.BLACK -> {
                binding.loadingImage.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff000000"))
            }
        }
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