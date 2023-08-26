package com.yjpapp.stockportfolio.data.network.service

import com.yjpapp.stockportfolio.data.model.response.*
import com.yjpapp.stockportfolio.data.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.data.model.response.RespLoginUserInfo
import com.yjpapp.stockportfolio.data.model.response.RespStatusInfo
import com.yjpapp.stockportfolio.data.model.response.RespTotalGainIncomeNoteData
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Body

interface RaspberryPiService {
    /**
     * User API
     */
    @POST("/api/user/regist_user")
    suspend fun requestRegUser(@Body reqSnsLogin: ReqSNSLogin): Response<RespLoginUserInfo>

//    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("/api/user/user_info")
    suspend fun requestUserInfo(@QueryMap params: HashMap<String, String>): Response<RespLoginUserInfo>

    @DELETE("/api/user/delete_user")
    suspend fun requestDeleteUserInfo(): Response<RespStatusInfo>

    /**
     * 수익 노트 API
     */
    @DELETE("/api/income_note/delete/{id}")
    suspend fun requestDeleteIncomeNote(@Path("id")id: Int): Response<RespStatusInfo>

    @GET("/api/income_note/total_gain")
    suspend fun requestTotalGainIncomeNote(@QueryMap params: HashMap<String, String>): Response<RespTotalGainIncomeNoteData>

}