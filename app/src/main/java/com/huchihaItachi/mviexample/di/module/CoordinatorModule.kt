package com.huchihaItachi.mviexample.di.module

import com.huchihaItachi.mviexample.navigation.LoginFlowCoordinator
import com.huchihaitachi.base.OAUTH
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
interface CoordinatorModule {

  companion object {
    @Provides
    @Named(OAUTH)
    fun provideOauth(loginFlowCoordinator: LoginFlowCoordinator): (() -> Unit) =
      loginFlowCoordinator::oauth
  }
}