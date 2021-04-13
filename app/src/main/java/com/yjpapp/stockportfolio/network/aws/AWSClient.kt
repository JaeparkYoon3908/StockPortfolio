package com.yjpapp.stockportfolio.network.aws

import com.yjpapp.stockportfolio.network.yahoofinance.YahooFinanceClient

class AWSClient {
    companion object {
        private val ourInstance = AWSClient()
        const val baseUrl =
            "http://18.222.228.174/"
        fun getInstance(): AWSClient{
            return ourInstance
        }
    }
}