package com.huchihaItachi.mviexample.navigation

import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.RouterTransaction.Companion
import com.huchihaitachi.anilist.presentation.AnilistController
import com.huchihaitachi.base.di.scope.ActivityScope
import com.huchihaitachi.login.presentation.LoginController
import com.huchihaitachi.loginwebview.presentation.LoginWebController
import javax.inject.Inject

@ActivityScope
class Navigator @Inject constructor(
) {
  lateinit var router: Router

  fun openLogin() {
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(LoginController()))
    } else {
      router.pushController(RouterTransaction.with(LoginController()))
    }
  }

  fun openLoginWebView() {
    router.pushController(RouterTransaction.with(LoginWebController()))
  }

  fun openAnimeList() {
    if (!router.hasRootController()) {
      router.setRoot(Companion.with(AnilistController()))
    } else {
      router.pushController(Companion.with(AnilistController()))
    }
  }
}