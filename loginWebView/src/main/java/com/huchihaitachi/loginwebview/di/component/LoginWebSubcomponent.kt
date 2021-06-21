package com.huchihaitachi.loginwebview.di.component

import com.huchihaitachi.loginwebview.di.module.LoginWebModule
import com.huchihaitachi.loginwebview.di.scope.LoginWebScope
import com.huchihaitachi.loginwebview.presentation.LoginWebController
import dagger.Subcomponent

@LoginWebScope
@Subcomponent(
  modules = [
    LoginWebModule::class
  ]
)
interface LoginWebSubcomponent {

  @Subcomponent.Factory
  interface Factory {
    fun create(): LoginWebSubcomponent
  }

  fun inject(loginWebController: LoginWebController)
}