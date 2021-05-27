package com.huchihaItachi.mviexample.di.module

import com.huchihaItachi.mviexample.navigation.LoginFlowCoordinator
import com.huchihaItachi.mviexample.navigation.RootFlowCoordinator
import com.huchihaitachi.login.presentation.coordination.LoginTransaction
import com.huchihaitachi.loginwebview.presentation.coordination.RootTransaction
import dagger.Binds
import dagger.Module

@Module
interface CoordinatorModule {

  @Binds
  fun bindLoginTransaction(loginFlowCoordinator: LoginFlowCoordinator): LoginTransaction

  @Binds
  fun bindRootTransaction(rootFlowCoordinator: RootFlowCoordinator): RootTransaction

  companion object {
  }
}