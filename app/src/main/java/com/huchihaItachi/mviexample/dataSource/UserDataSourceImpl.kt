package com.huchihaItachi.mviexample.dataSource

import android.content.SharedPreferences
import androidx.core.content.edit
import com.huchihaitachi.datasource.UserDataSource
import com.huchihaitachi.remoteapi.service.AnimeAuthService
import com.huchihaitachi.remoteapi.service.Credentials
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
  private val animeAuthService: AnimeAuthService,
  private val preferences: SharedPreferences
) : UserDataSource {

  override fun isAuthToken(): Single<Boolean> =
    Single.just(preferences.contains(AUTH_TOKEN))

  override fun exchangeCodeToToken(code: String): Single<String> =
    animeAuthService.codeToTokenExchange(Credentials(code)).map { response ->
      response.accessToken
    }

  override fun persistToken(token: String): Completable =
    Completable.fromCallable {
      preferences.edit { putString(AUTH_TOKEN, token) }
    }

  companion object {
    const val AUTH_TOKEN = "auth_token"
  }
}