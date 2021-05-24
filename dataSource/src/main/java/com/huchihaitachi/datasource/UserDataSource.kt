package com.huchihaitachi.datasource

import io.reactivex.Completable
import io.reactivex.Single

interface UserDataSource {

  fun isAuthToken(): Single<Boolean>

  fun exchangeCodeToToken(code: String): Single<String>

  fun persistToken(token: String): Completable
}