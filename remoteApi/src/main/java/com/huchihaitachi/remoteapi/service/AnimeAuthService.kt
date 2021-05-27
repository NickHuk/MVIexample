package com.huchihaitachi.remoteapi.service

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AnimeAuthService {

  @Headers("Content-Type: application/json", "Accept: application/json")
  @POST("oauth/token")
  fun codeToTokenExchange(@Body credentials: Credentials): Single<ExchangeResponse>
}