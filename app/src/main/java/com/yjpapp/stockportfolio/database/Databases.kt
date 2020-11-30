package com.yjpapp.stockportfolio.database

object Databases {
    /**
     * table name
     */
    const val TABLE_MEMO: String = "memo_table"
    const val TABLE_PORTFOLIO: String = "portfolio_table"

    /**
     * memo_table columns
     */
    const val COL_MEMO_ID: String = "id" //primary key, id
    const val COL_MEMO_DATE: String = "date" //날짜
    const val COL_MEMO_TITLE: String = "title" //제목
    const val COL_MEMO_CONTENT: String = "content" //내용

    /**
     * portfolio_table columns
     */
    const val COL_PORTFOLIO_ID: String = "id" // primary key, id
    const val COL_PORTFOLIO_SUBJECT_NAME: String = "subject_name" // 종목명
    const val COL_PORTFOLIO_REAL_GAINS_LOSSES_AMOUNT: String = "real_gains_losses_amount" // 순손익금액
    const val COL_PORTFOLIO_PURCHASE_DATE: String = "purchase_date" //매수일
    const val COL_PORTFOLIO_SELL_DATE: String = "sell_date" // 매도일
    const val COL_PORTFOLIO_PURCHASE_PRICE: String = "purchase_price" //매수단가
    const val COL_PORTFOLIO_SELL_PRICE: String ="sell_price" //매도단가
    const val COL_PORTFOLIO_GAIN_PERCENT: String = "gain_percent" // 수익률
    const val COL_PORTFOLIO_SELL_COUNT: String = "sell_count" // 매도수량
//    const val COL_DELETE_CHECK: String ="delete_check"
    /**
     * create table query
     */
    const val CREATE_MEMO_TABLE: String =
        "create table " + TABLE_MEMO + "(" +
                COL_MEMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MEMO_DATE + " TEXT," +
                COL_MEMO_TITLE + " TEXT," +
                COL_MEMO_CONTENT + " TEXT);"

    const val CREATE_PORTFOLIO_TABLE: String =
        "create table " + TABLE_PORTFOLIO + "(" +
                COL_PORTFOLIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PORTFOLIO_SUBJECT_NAME + " TEXT," +
                COL_PORTFOLIO_REAL_GAINS_LOSSES_AMOUNT + " TEXT," +
                COL_PORTFOLIO_PURCHASE_DATE + " TEXT," +
                COL_PORTFOLIO_SELL_DATE + " TEXT," +
                COL_PORTFOLIO_GAIN_PERCENT + " TEXT," +
                COL_PORTFOLIO_PURCHASE_PRICE + " TEXT," +
                COL_PORTFOLIO_SELL_PRICE + " TEXT," +
                COL_PORTFOLIO_SELL_COUNT + " TEXT);"
//                COL_DELETE_CHECK + " TEXT DEFAULT FALSE);"
}