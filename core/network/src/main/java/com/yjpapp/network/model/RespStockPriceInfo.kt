package com.yjpapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RespStockPriceInfo(
    var response: Response = Response()
) {
    @Serializable
    data class Response(
        var header: Header = Header(),
        var body: Body = Body()
    ) {
        @Serializable
        data class Header(
            var resultCode: String = "",
            var resultMsg: String = ""
        )

        @Serializable
        data class Body(
            var numOfRows: Int = 0,
            var pageNo: Int = 0,
            var totalCount: Int = 0,
            var items: Item = Item()
        ) {
            @Serializable
            data class Item(
                var item: List<StockPriceInfo> = listOf()
            )
        }
    }
}

@Serializable
data class StockPriceInfo(
    var basDt: String = "", //기준 일자
    var srtnCd: String = "", //단축코드 : 종목 코드보다 짧으면서 유일성이 보장되는 코드(6자리)
    var isinCd: String = "", //ISIN 코드 : 국제 채권 식별 번호. 유가증권(채권)의 국제인증 고유번호
    var itmsNm: String = "", //종목의 명칭
    var mrktCtg: String = "", //시장구분 : 주식의 시장 구분 (KOSPI/KOSDAQ/KONEX 중 1)
    var clpr: String = "", //종가 : 정규시장의 매매시간 종료시까지 형성되는 최종가격
    var vs: String = "", //전일 대비 등락
    var fltRt: String = "", //등락률
    var mkp: String = "", //시가
    var hipr: String = "", //고가
    var lopr: String = "", //저가
    var trqu: String = "", //거래량
    var trPrc: String = "", //거래대금
    var lstgStCnt: String = "", //상장 주식수
    var mrktTotAmt: String = "" // 시가총액
): java.io.Serializable
