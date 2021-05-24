package com.huchihaItachi.mviexample.useCaseImpl

import com.huchihaitachi.repository.UserRepository
import com.huchihaitachi.usecase.LoginUseCase
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCaseImpl @Inject constructor(
  private val userRepository: UserRepository
) : LoginUseCase {

  override fun invoke(code: String): Completable = userRepository.codeToTokenExchange(code)
}