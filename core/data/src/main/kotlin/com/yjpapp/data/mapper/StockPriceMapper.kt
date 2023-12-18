package com.yjpapp.data.mapper

import com.yjpapp.data.model.response.StockPriceData
import com.yjpapp.network.model.StockPriceInfo

fun StockPriceInfo.mapping(): StockPriceData =
    StockPriceData(
        basDt = this.basDt,
        srtnCd = this.srtnCd,
        isinCd = this.isinCd,
        itmsNm = this.itmsNm,
        mrktCtg = this.mrktCtg,
        clpr = this.clpr,
        vs = this.vs,
        fltRt = this.fltRt,
        mkp = this.mkp,
        hipr = this.hipr,
        lopr = this.lopr,
        trqu = this.trqu,
        trPrc = this.trPrc,
        lstgStCnt = this.lstgStCnt,
        mrktTotAmt = this.mrktTotAmt
    )