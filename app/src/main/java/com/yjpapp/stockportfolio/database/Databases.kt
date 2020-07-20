package com.yjpapp.stockportfolio.database

object Databases {
    /**
     * table name
     */
    internal const val TABLE_HEAD: String = "head_table"
    internal const val TABLE_DATA: String = "data_table"

    /**
     * proceed_list_data_table columns
     */
    internal const val COL_PERIOD: String = "period" // 기간
    internal const val COL_SUBJECT: String = "subject" // 종류

    /**
     * proceed_list_data_table columns
     */
    internal const val COL_ID: String = "id" // id
    internal const val COL_DATE_OF_SALE: String = "date_of_sale" // 매매일
    internal const val COL_SUBJECT_NAME: String = "subject_name" // 종목명
    internal const val COL_REAL_GAINS_LOSSES_AMOUNT: String = "real_gains_losses_amount" // 순손익금액
    internal const val COL_GAIN_PERCENT: String = "gain_percent" // 수익률
    internal const val COL_PURCHASE_PRICE: String = "purchase_price" //매수단가
    internal const val COL_SELL_PRICE: String ="sell_price" //매도단가

    /**
     * create table query
     */
    internal const val CREATE_HEAD_TABLE: String =
        "create table " + TABLE_HEAD + "(" +
                COL_PERIOD + "text," +
                COL_SUBJECT + "text);"

    internal const val CREATE_DATA_TABLE: String =
        "create table " + TABLE_DATA + "(" +
                COL_ID + " integer primary key autoincrement, " +
                COL_DATE_OF_SALE + " text," +
                COL_SUBJECT_NAME + " text," +
                COL_REAL_GAINS_LOSSES_AMOUNT + " text," +
                COL_GAIN_PERCENT + " text," +
                COL_PURCHASE_PRICE + " text," +
                COL_SELL_PRICE + " text);"
}