package com.huchihaItachi.mviexample.repositoryImpl

import com.huchihaitachi.datasource.UserDataSource
import com.huchihaitachi.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
  private val userDataSource: UserDataSource
) : UserRepository {

  override fun isAuthToken(): Single<Boolean> = userDataSource.isAuthToken()

  override fun codeToTokenExchange(code: String): Completable =
    userDataSource.exchangeCodeToToken(code)
      .flatMapCompletable { token ->
        userDataSource.persistToken(token)
      }
}