package com.yjpapp.data.model.request

data class ReqStockPriceInfo(
    /**
     * 필수
     */
    val serviceKey: String = "THnjzgvbJb4e6Do4H22ehdKY0i1MSGfgzbEeOddm5QPipubruRWCa85lj1dS95Ji/BcaIiPUOPhfr+ziyLFgvw==", //서비스키
    var numOfRows: String = "",      //한 페이지 결과 수
    var pageNo: String = "",         //페이지 번호
    val resultType: String = "json", //결과형식 : 구분(xml, json) Default: xml
    /**
     * 옵션
     */
    var basDt: String = "",      //기준일자 : 검색값과 기준일자가 일치하는 데이터를 검색
//    var beginBasDt: String = "", //기준일자 : 기준일자가 검색값보다 크거나 같은 데이터를 검색
//    var endBasDt: String = "",   //기준일자 : 기준일자가 검색값보다 작은 데이터를 검색
//    var likeBasDt: String = "",  //기준일자 : 기준일자값이 검색값을 포함하는 데이터를 검색
    var isinCd: String = "",      //ISIN코드 : 검색값과 ISIN코드이 일치하는 데이터를 검색
//    var likeIsinCd: String = "",    //ISIN코드 : ISIN코드가 검색값을 포함하는 데이터를 검색
//    var itmsNm: String = "",      //종목명 : 검색값과 종목명이 일치하는 데이터를 검색
    var likeItmsNm: String = "", //종목명 : 종목명이 검색값을 포함하는 데이터를 검색
//    var mrktCls: String = "", //시장구분 : 검색값과 시장구분이 일치하는 데이터를 검색
//    var beginVs: String = "", //대비 : 대비가 검색값보다 크거나 같은 데이터를 검색
//    var endVs: String = "", //대비 : 대비가 검색값보다 작은 데이터를 검색
//    var beginFltRt: String = "", //등락률 : 등락률이 검색값보다 크거나 같은 데이터를 검색
//    var endFltRt: String = "", //등락률 : 등락률이 검색값보다 작은 데이터를 검색
//    var beginTrqu: String = "", //거래량 : 거래량이 검색값보다 크거나 같은 데이터를 검색
//    var endTrqu: String = "",  //거래량 : 거래량이 검색값보다 작은 데이터를 검색
//    var beginTrPrc: String = "", //거래대금 : 거래대금이 검색값보다 크거나 같은 데이터를 검색
//    var endTrPrc: String = "", //거래대금 : 거래대금이 검색값보다 작은 데이터를 검색
//    var beginLstgStCnt: String = "", //상장주식수 : 상장주식수가 검색값보다 크거나 같은 데이터를 검색
//    var endLstgStCnt: String = "", //상장주식수 : 상장주식수가 검색값보다 작은 데이터를 검색
//    var beginMrktTotAmt: String = "", //시가총액 : 시가총액이 검색값보다 크거나 같은 데이터를 검색
//    var endMrktTotAmt: String = "" //시가총액 : 시가총액이 검색값보다 작은 데이터를 검색
)