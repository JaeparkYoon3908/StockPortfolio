package com.yjpapp.stockportfolio.database

object Databases {
    /**
     * table name
     */
    const val TABLE_HEAD: String = "head_table"
    const val TABLE_DATA: String = "data_table"

    /**
     * proceed_list_data_table columns
     */
    const val COL_PERIOD: String = "period" // 기간
    const val COL_SUBJECT: String = "subject" // 종류

    /**
     * proceed_list_data_table columns
     */
    const val COL_ID: String = "id" // id
    const val COL_SUBJECT_NAME: String = "subject_name" // 종목명
    const val COL_REAL_GAINS_LOSSES_AMOUNT: String = "real_gains_losses_amount" // 순손익금액
    const val COL_PURCHASE_DATE: String = "purchase_date" //매수일
    const val COL_SELL_DATE: String = "sell_date" // 매도일
    const val COL_PURCHASE_PRICE: String = "purchase_price" //매수단가
    const val COL_SELL_PRICE: String ="sell_price" //매도단가
    const val COL_GAIN_PERCENT: String = "gain_percent" // 수익률
    const val COL_SELL_COUNT: String = "sell_count" // 매도수량
//    const val COL_DELETE_CHECK: String ="delete_check"
    /**
     * create table query
     */
    const val CREATE_HEAD_TABLE: String =
        "create table " + TABLE_HEAD + "(" +
                COL_PERIOD + "TEXT," +
                COL_SUBJECT + "TEXT);"

    const val CREATE_DATA_TABLE: String =
        "create table " + TABLE_DATA + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SUBJECT_NAME + " TEXT," +
                COL_REAL_GAINS_LOSSES_AMOUNT + " TEXT," +
                COL_PURCHASE_DATE + " TEXT," +
                COL_SELL_DATE + " TEXT," +
                COL_GAIN_PERCENT + " TEXT," +
                COL_PURCHASE_PRICE + " TEXT," +
                COL_SELL_PRICE + " TEXT," +
                COL_SELL_COUNT + " TEXT);"
//                COL_DELETE_CHECK + " TEXT DEFAULT FALSE);"
}