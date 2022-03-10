package com.yjpapp.stockportfolio.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.VersionedPackage
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Base64
import com.yjpapp.stockportfolio.common.StockConfig
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * 개발에 필요한 함수들
 *
 * @author Yoon Jae-park
 * @since 2020.07
 */
object StockUtils {
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
        val result = StringBuffer().apply {
            append(yyyymmdd.substring(0, 4))
            append(".")
            append(yyyymmdd.substring(4, 6))
            append(".")
            append(yyyymmdd.substring(6, 8))
        }
        return result.toString()
    }

    //현재 yyyy.mm 반환
    fun getTodayYYMMDD(): List<String> {
        val currentTime: Long = System.currentTimeMillis()
        val todayDate = Date(currentTime)
        val sdformat = SimpleDateFormat("yyyy-MM-DD")
        return sdformat.format(todayDate).split("-")
    }

    //5,000,000 => 5000000 변환
    fun getNumDeletedComma(num: String): String{
        val result = StringBuffer()
        val split = num.split(",")
        for (i in split.indices) {
            result.append(split[i])
        }
        return result.toString()
    }

    //5000000 => 5,000,000 변환
    fun getNumInsertComma(num: String): String{
        var result = ""
        val decimalFormat = DecimalFormat("###,###")
        result = decimalFormat.format(num.replace(",", "").toDouble())
        return result
    }
    //5000000 => $5,000,000 변환
    fun getPriceNum(num: String): String {
        val result = StringBuffer().apply {
            append(StockConfig.koreaMoneySymbol)
            append(getNumInsertComma(num))
        }
        return result.toString()
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

//    //전체 수익률 계산 함수 -> (수익률) = (매도총금액) / (매수총금액) * 100 - 100
//    fun calculateTotalGainPercent(incomeNoteInfoList: MutableList<RespIncomeNoteInfo.IncomeNoteList?>): Double{
//        var allPurchasePrice = 0.0
//        var allSellPrice = 0.0
//        incomeNoteInfoList.forEach {
//            if(it?.purchasePrice != null && it.realPainLossesAmount != null){
//                allPurchasePrice += getNumDeletedComma(it.purchasePrice!!).toDouble() * it.sellCount
//                allSellPrice += getNumDeletedComma(it.sellPrice!!).toDouble() * it.sellCount
//            }
//        }
//        return ((allSellPrice / allPurchasePrice) * 100) - 100
//    }

    //퍼센티지 붙이기
    fun getRoundsPercentNumber(number: Double): String{
        var result = ""
        result = try{
            String.format("%.2f", number)
        }catch (e: Exception){
            "0"
        }
        result += "%"
        return result
    }

    //전일대비 가격 구하기
    fun getDayToDayPrice(yesterdayPrice: Double, currentPrice: Double, local: String = StockConfig.LOCAL_KOREA): String {
        when (local) {
            StockConfig.LOCAL_KOREA -> {
                return getNumInsertComma((currentPrice - yesterdayPrice).roundToInt().toString())
            }
        }
        //기본 값 : USA
        return getNumInsertComma((currentPrice - yesterdayPrice).toString())
    }

    @SuppressLint("MissingPermission")
    fun runVibration(mContext: Context, milliseconds: Long){
        val vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator!!.vibrate(VibrationEffect.createOneShot(milliseconds,
                VibrationEffect.DEFAULT_AMPLITUDE))
        }else{
            vibrator!!.vibrate(milliseconds);
        }
    }

    private fun getHashKey(packageManager: PackageManager, packageName: VersionedPackage) {
        var packageInfo: PackageInfo? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) StockLog.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                StockLog.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                StockLog.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }
}