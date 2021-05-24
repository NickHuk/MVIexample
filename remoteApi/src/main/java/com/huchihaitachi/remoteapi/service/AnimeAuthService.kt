package com.huchihaitachi.remoteapi.service

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AnimeAuthService {

  @Headers("Content-Type: application/json", "Accept: application/json")
  @FormUrlEncoded
  @POST("oauth/token")
  fun codeToTokenExchange(
    @Field("code") code: String,
    @Field("grant_type") grantType: String = "authorization_code",
    @Field("client_id") clientId: String = "5574",
    @Field("client_secret") clientSecret: String = "p64ORQN8Z6MyUsu156jcT3XQaCTEuk1EpSd64Dnp",
    @Field("redirect_uri") redirectUrl: String = "https://huchihaitatchi.mviexample.com"
  ): Single<ExchangeResponse>
}