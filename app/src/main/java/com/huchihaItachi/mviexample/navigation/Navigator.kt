package com.huchihaItachi.mviexample.navigation

import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.huchihaitachi.base.di.scope.ActivityScope
import com.huchihaitachi.login.LoginController
import com.huchihaitachi.loginwebview.LoginWebController
import javax.inject.Inject

@ActivityScope
class Navigator @Inject constructor(
) {
  lateinit var router: Router

  fun startFromLogin() {
    router.setRoot(RouterTransaction.with(LoginController()))
  }

  fun openLoginWebView() {
    router.pushController(RouterTransaction.with(LoginWebController()))
  }

  /*fun startFromAnimeList() {
      router.setRoot(Route)
  }*/
}