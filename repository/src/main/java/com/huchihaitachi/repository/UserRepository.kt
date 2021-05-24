package com.huchihaitachi.repository

import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
  fun isAuthToken(): Single<Boolean>
  fun codeToTokenExchange(code: String): Completable
}