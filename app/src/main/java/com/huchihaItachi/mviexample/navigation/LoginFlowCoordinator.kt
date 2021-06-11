package com.huchihaItachi.mviexample.navigation

import com.huchihaitachi.base.di.scope.ActivityScope
import com.huchihaitachi.login.presentation.coordination.LoginTransaction
import javax.inject.Inject

@ActivityScope
class LoginFlowCoordinator @Inject constructor(
  private val navigator: Navigator
) : LoginTransaction {

  fun start() {
    navigator.openLogin()
  }

  override fun oauth() = navigator.openLoginWebView()
}