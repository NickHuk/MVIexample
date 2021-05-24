package com.huchihaItachi.mviexample.navigation

import com.huchihaitachi.base.di.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class LoginFlowCoordinator @Inject constructor(
  private val navigator: Navigator
) {

  fun start() {
    navigator.startFromLogin()
  }

  fun oauth() = navigator.openLoginWebView()
}