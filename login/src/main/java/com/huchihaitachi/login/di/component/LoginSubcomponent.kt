package com.huchihaitachi.login.di.component

import com.huchihaitachi.login.di.LoginScope
import com.huchihaitachi.login.di.module.LoginModule
import com.huchihaitachi.login.presentation.LoginController
import dagger.Subcomponent

@LoginScope
@Subcomponent(
  modules = [
    LoginModule::class
  ]
)
interface LoginSubcomponent {

  @Subcomponent.Factory
  interface Factory {
    fun create(): LoginSubcomponent
  }

  fun inject(loginController: LoginController)
}