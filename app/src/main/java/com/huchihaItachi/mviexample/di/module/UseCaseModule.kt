package com.huchihaItachi.mviexample.di.module

import com.huchihaItachi.mviexample.useCaseImpl.IsLoggedInUseCaseImpl
import com.huchihaItachi.mviexample.useCaseImpl.LoginUseCaseImpl
import com.huchihaitachi.usecase.IsLoggedInUseCase
import com.huchihaitachi.usecase.LoginUseCase
import dagger.Binds
import dagger.Module

@Module
interface UseCaseModule {

  @Binds
  fun bindIsLoggedInUseCase(isLoggedInUseCaseImpl: IsLoggedInUseCaseImpl): IsLoggedInUseCase

  @Binds
  fun bindLoginUseCase(loginUseCaseImpl: LoginUseCaseImpl): LoginUseCase
}