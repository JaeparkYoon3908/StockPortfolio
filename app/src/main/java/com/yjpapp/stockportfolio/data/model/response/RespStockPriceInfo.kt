package com.yjpapp.stockportfolio.data.model.response

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
    var basDt: String = "",
    var srtnCd: String = "",
    var isinCd: String = "",
    var itmsNm: String = "", //종목의 명칭
    var mrktCtg: String = "",
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
)
