package com.yjpapp.stockportfolio

import com.yjpapp.stockportfolio.util.StockUtils
import org.junit.Assert.assertNotEquals
import org.junit.Test

class UtilsTest {
    @Test
    fun newsDate_isCorrect() {
        val beforeParseDate = "Sat, 14 Oct 2023 14:20:47 09:00"
//        val beforeParseDate = "SSat, 14 Oct 2023 14:20:47 09:00" //is not correct date format
        val parsDate = StockUtils.parseNewsDate(beforeParseDate)
        println(parsDate)
        assertNotEquals(beforeParseDate, parsDate)
    }
}