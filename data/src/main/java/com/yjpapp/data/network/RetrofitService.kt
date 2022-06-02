package com.yjpapp.data.network

import com.yjpapp.data.model.RepositoryResult
import com.yjpapp.data.model.request.ReqIncomeNoteInfo
import com.yjpapp.data.model.request.ReqSNSLogin
import com.yjpapp.data.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
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

    @GET("/v1/nid/me")
    suspend fun requestGetNaverUserInfo(): Response<RespGetNaverUserInfo>

    @GET("/oauth2.0/token")
    suspend fun requestDeleteNaverUserInfo(@QueryMap params: HashMap<String, String>): Response<RespNaverDeleteUserInfo>

    @GET("/oauth2.0/authorize")
    suspend fun requestRetryNaverUserLogin(@QueryMap params: HashMap<String, String>): Response<RespNaverDeleteUserInfo>

    /**
     * 수익 노트 API
     */
    @POST("/api/income_note/list")
    suspend fun requestPostIncomeNote(@Body reqIncomeNoteInfo: ReqIncomeNoteInfo): Response<RespIncomeNoteListInfo.IncomeNoteInfo>

    @GET("/api/income_note/list")
    suspend fun requestGetIncomeNote(@QueryMap params: HashMap<String, String>): Response<RespIncomeNoteListInfo>

    @PUT("/api/income_note/list")
    suspend fun requestPutIncomeNote(@Body reqIncomeNoteInfo: ReqIncomeNoteInfo): Response<RespIncomeNoteListInfo.IncomeNoteInfo>

    @DELETE("/api/income_note/delete/{id}")
    suspend fun requestDeleteIncomeNote(@Path("id")id: Int): Response<RespStatusInfo>

    @GET("/api/income_note/total_gain")
    suspend fun requestTotalGainIncomeNote(@QueryMap params: HashMap<String, String>): Response<RespTotalGainIncomeNoteData>

}