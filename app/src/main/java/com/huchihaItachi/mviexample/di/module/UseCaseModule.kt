package com.huchihaItachi.mviexample.di.module

import com.huchihaItachi.mviexample.useCaseImpl.GetStringResourceUseCaseImpl
import com.huchihaItachi.mviexample.useCaseImpl.IsLoggedInUseCaseImpl
import com.huchihaItachi.mviexample.useCaseImpl.LoadAnimeUseCaseImpl
import com.huchihaItachi.mviexample.useCaseImpl.LoadPageUseCaseImpl
import com.huchihaItachi.mviexample.useCaseImpl.LoginUseCaseImpl
import com.huchihaitachi.usecase.GetStringResourceUseCase
import com.huchihaitachi.usecase.IsLoggedInUseCase
import com.huchihaitachi.usecase.LoadAnimeUseCase
import com.huchihaitachi.usecase.LoadPageUseCase
import com.huchihaitachi.usecase.LoginUseCase
import dagger.Binds
import dagger.Module

@Module
interface UseCaseModule {

  @Binds
  fun bindIsLoggedInUseCase(isLoggedInUseCaseImpl: IsLoggedInUseCaseImpl): IsLoggedInUseCase

  @Binds
  fun bindLoginUseCase(loginUseCaseImpl: LoginUseCaseImpl): LoginUseCase

  @Binds
  fun bindLoadPageUseCase(loadPageUseCaseImpl: LoadPageUseCaseImpl): LoadPageUseCase

  @Binds
  fun bindGetStringResourceUseCase(getStringResourceUseCaseImpl: GetStringResourceUseCaseImpl): GetStringResourceUseCase

  @Binds
  fun bindLoadAnimeUse(loadAnimeUseCaseImpl: LoadAnimeUseCaseImpl): LoadAnimeUseCase
}