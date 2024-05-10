package com.yjpapp.data.model.response

import java.io.Serializable

data class StockPriceData(
    val basDt: String = "", //기준 일자
    val srtnCd: String = "", //단축코드 : 종목 코드보다 짧으면서 유일성이 보장되는 코드(6자리)
    val isinCd: String = "", //ISIN 코드 : 국제 채권 식별 번호. 유가증권(채권)의 국제인증 고유번호
    val itmsNm: String = "", //종목의 명칭
    val mrktCtg: String = "", //시장구분 : 주식의 시장 구분 (KOSPI/KOSDAQ/KONEX 중 1)
    val clpr: String = "", //종가 : 정규시장의 매매시간 종료시까지 형성되는 최종가격
    val vs: String = "", //전일 대비 등락
    val fltRt: String = "", //등락률
    val mkp: String = "", //시가
    val hipr: String = "", //고가
    val lopr: String = "", //저가
    val trqu: String = "", //거래량
    val trPrc: String = "", //거래대금
    val lstgStCnt: String = "", //상장 주식수
    val mrktTotAmt: String = "" // 시가총액
): Serializable