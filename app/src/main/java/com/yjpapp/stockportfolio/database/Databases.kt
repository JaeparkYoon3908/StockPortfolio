package com.yjpapp.stockportfolio.database

object Databases {
    /**
     * table name
     */
    const val TABLE_MEMO: String = "memo_table"
    const val TABLE_INCOME_NOTE: String = "income_note_table"

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

}