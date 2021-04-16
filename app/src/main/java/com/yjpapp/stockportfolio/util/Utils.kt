package com.yjpapp.stockportfolio.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.sqlte.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.preference.PrefKey
import com.yjpapp.stockportfolio.preference.PreferenceController
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*

/**
 * 개발에 필요한 함수들
 *
 * @author Yoon Jae-park
 * @since 2020.07
 */
object Utils {
    //yyyymmdd로 변환
    fun getTodayYYYYMMDD(): String {
        val currentTime: Long = System.currentTimeMillis()
        val todayDate = Date(currentTime)
        val sdformat = SimpleDateFormat("yyyyMMdd")
        return sdformat.format(todayDate)

    }

    //yyyy.mm.dd로 변환
    fun getTodayYYYY_MM_DD(): String{
        val yyyymmdd = getTodayYYYYMMDD()
        var result: String = ""
        result += yyyymmdd.substring(0, 4) + "."
        result += yyyymmdd.substring(4, 6) + "."
        result += yyyymmdd.substring(6, 8)
        return result
    }

    //5,000,000 => 5000000 변환
    fun getNumDeletedComma(num: String): String{
        var result: String = ""
        val split = num.split(",")
        for (i in split.indices) {
            result += split[i]
        }
        return result
    }

    //14% => 14 변환.
    fun getNumDeletedPercent(num: String): String{
        var result: String = ""
        val split = num.split("%")
        for (i in split.indices) {
            result += split[i]
        }
        return result
    }

    //2020.11.18 =>20201118 변환.
    fun getNumDeletedDot(num: String): String{
        var result: String = ""
        val split = num.split(".")
        for (i in split.indices) {
            result += split[i]
        }
        return result
    }

    //수익률 계산
    fun calculateGainPercent(purchasePrice: String, sellPrice: String): Double{
        val purchasePriceNum = getNumDeletedComma(purchasePrice)
        val sellPriceNum = getNumDeletedComma(sellPrice)

        return (((sellPriceNum.toDouble() / purchasePriceNum.toDouble()) -1) * 100)
    }

    //전체 수익률 계산 함수 -> (수익률) = (매도총금액) / (매수총금액) * 100 - 100
    fun calculateTotalGainPercent(incomeNoteInfoList: ArrayList<IncomeNoteInfo?>): Double{
        var allPurchasePrice = 0.0
        var allSellPrice = 0.0
        incomeNoteInfoList.forEach {
            if(it?.purchasePrice != null && it.realPainLossesAmount != null){
                allPurchasePrice += getNumDeletedComma(it.purchasePrice!!).toDouble() * it.sellCount
                allSellPrice += getNumDeletedComma(it.sellPrice!!).toDouble() * it.sellCount
            }
        }
        return ((allSellPrice / allPurchasePrice) * 100) - 100
    }

//    fun calculateTotalGainPercent(percentList: ArrayList<Double>): Double{
//
//
//        return if(percentList.size>0){
//            var result = percentList[0]
////            percentList.forEach {
////                result = (1+result)*(1+it)-1
////            }
//            for(i in 0 until percentList.size - 1){
//                result = (1+result)*(1+percentList[i+1])-1
//            }
//            result
//        }else{
//            0.00
//        }
//    }

    //퍼센티지 붙이기
    fun getRoundsPercentNumber(number: Double): String{
        var result = ""
        try{
            result = String.format("%.2f", number)
        }catch (e: Exception){
            result = "0"
        }
        result += "%"
        return result
    }

    fun runVibration(mContext: Context, milliseconds: Long){
        val vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator!!.vibrate(VibrationEffect.createOneShot(milliseconds,
                VibrationEffect.DEFAULT_AMPLITUDE))
        }else{
            vibrator!!.vibrate(milliseconds);
        }
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun runBackPressAppCloseEvent(mContext: Context, activity:Activity){
        val isAllowAppClose = PreferenceController.getInstance(mContext).getPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE)
        if(isAllowAppClose == "true"){
            activity.finish()
        }else{
            Toasty.normal(mContext,mContext.getString(R.string.Common_BackButton_AppClose_Message)).show()
            PreferenceController.getInstance(mContext).setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, "true")
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                PreferenceController.getInstance(mContext).setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, "false")
            },3000)
        }
    }

//    fun getMyPhoneNum(mContext: Context): String{
//        var phoneNum = ""
//        val telManager = mContext.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
//        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return ""
//        }
//        phoneNum = telManager.line1Number
//        if(phoneNum.isNotEmpty()){
//            if(phoneNum.startsWith("+82")){
//                phoneNum = phoneNum.replace("+82", "0")
//            }
//        }
//
//        if(phoneNum==null){
//            phoneNum = ""
//        }
//        return phoneNum
//    }
}