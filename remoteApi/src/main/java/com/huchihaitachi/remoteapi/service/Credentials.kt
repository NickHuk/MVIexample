package com.huchihaitachi.remoteapi.service

import com.google.gson.annotations.SerializedName

data class Credentials(
  val code: String,
  @SerializedName("grant_type") val grantType: String = "authorization_code",
  @SerializedName("client_id") val clientId: String = "5574",
  @SerializedName("client_secret") val clientSecret: String = "p64ORQN8Z6MyUsu156jcT3XQaCTEuk1EpSd64Dnp",
  @SerializedName("redirect_uri") val redirectUri: String = "https://huchihaitatchi.mviexample.com"
)