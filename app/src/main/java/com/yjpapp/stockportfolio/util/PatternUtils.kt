package com.yjpapp.stockportfolio.util

import com.yjpapp.stockportfolio.common.StockConfig

object PatternUtils {
    //이메일 마스킹킹
    fun getEmailMasking(email: String?): String {
        if (email == null) {
            return ""
        }
        val result = StringBuffer()
        val nickNameSplit = email.split("@")
        if (nickNameSplit.size == 2) {
            if (nickNameSplit[0].length > 3) {
                result.append(nickNameSplit[0].substring(0, 3))
            } else {
                result.append(nickNameSplit[0])
            }
            result.append("***")
                .append("@")
                .append(nickNameSplit[1])
        }

        return result.toString()
    }

    fun isEmailForm(email: String?, loginType: String): Boolean {
        if (email == null) return false
        val pattern = android.util.Patterns.EMAIL_ADDRESS
        return when (loginType) {
            StockConfig.LOGIN_TYPE_NAVER -> {
                email.contains("@naver.com") && pattern.matcher(email).matches()
            }
            StockConfig.LOGIN_TYPE_GOOGLE -> {
                email.contains("@gmail.com") && pattern.matcher(email).matches()
            }
            StockConfig.LOGIN_TYPE_FACEBOOK -> {
                pattern.matcher(email).matches()
            }
            else -> false
        }
    }

    fun isNaverEmailForm(email: String?): Boolean {
        if (email == null) return false
        val pattern = android.util.Patterns.EMAIL_ADDRESS
        return email.contains("@naver.com") && pattern.matcher(email).matches()
    }
}