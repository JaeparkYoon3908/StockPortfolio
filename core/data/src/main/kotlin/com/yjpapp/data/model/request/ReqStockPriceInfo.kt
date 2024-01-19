package com.yjpapp.data.model.request

data class ReqStockPriceInfo(
    /**
     * 필수
     */
    val serviceKey: String = "THnjzgvbJb4e6Do4H22ehdKY0i1MSGfgzbEeOddm5QPipubruRWCa85lj1dS95Ji/BcaIiPUOPhfr+ziyLFgvw==", //서비스키
    val numOfRows: String = "",      //한 페이지 결과 수
    val pageNo: String = "",         //페이지 번호
    val resultType: String = "json", //결과형식 : 구분(xml, json) Default: xml
    /**
     * 옵션
     */
    val basDt: String = "",      //기준일자 : 검색값과 기준일자가 일치하는 데이터를 검색
//    val beginBasDt: String = "", //기준일자 : 기준일자가 검색값보다 크거나 같은 데이터를 검색
//    val endBasDt: String = "",   //기준일자 : 기준일자가 검색값보다 작은 데이터를 검색
//    val likeBasDt: String = "",  //기준일자 : 기준일자값이 검색값을 포함하는 데이터를 검색
    val isinCd: String = "",      //ISIN코드 : 검색값과 ISIN코드이 일치하는 데이터를 검색
//    val likeIsinCd: String = "",    //ISIN코드 : ISIN코드가 검색값을 포함하는 데이터를 검색
//    val itmsNm: String = "",      //종목명 : 검색값과 종목명이 일치하는 데이터를 검색
    val likeItmsNm: String = "", //종목명 : 종목명이 검색값을 포함하는 데이터를 검색
//    val mrktCls: String = "", //시장구분 : 검색값과 시장구분이 일치하는 데이터를 검색
//    val beginVs: String = "", //대비 : 대비가 검색값보다 크거나 같은 데이터를 검색
//    val endVs: String = "", //대비 : 대비가 검색값보다 작은 데이터를 검색
//    val beginFltRt: String = "", //등락률 : 등락률이 검색값보다 크거나 같은 데이터를 검색
//    val endFltRt: String = "", //등락률 : 등락률이 검색값보다 작은 데이터를 검색
//    val beginTrqu: String = "", //거래량 : 거래량이 검색값보다 크거나 같은 데이터를 검색
//    val endTrqu: String = "",  //거래량 : 거래량이 검색값보다 작은 데이터를 검색
//    val beginTrPrc: String = "", //거래대금 : 거래대금이 검색값보다 크거나 같은 데이터를 검색
//    val endTrPrc: String = "", //거래대금 : 거래대금이 검색값보다 작은 데이터를 검색
//    val beginLstgStCnt: String = "", //상장주식수 : 상장주식수가 검색값보다 크거나 같은 데이터를 검색
//    val endLstgStCnt: String = "", //상장주식수 : 상장주식수가 검색값보다 작은 데이터를 검색
//    val beginMrktTotAmt: String = "", //시가총액 : 시가총액이 검색값보다 크거나 같은 데이터를 검색
//    val endMrktTotAmt: String = "" //시가총액 : 시가총액이 검색값보다 작은 데이터를 검색
)