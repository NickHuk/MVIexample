package com.huchihaitachi.login.di.module

import com.huchihaitachi.login.di.LoginScope
import com.huchihaitachi.login.presentation.LoginViewState
import dagger.Module
import dagger.Provides

@Module
interface LoginModule {

  companion object {
    @LoginScope
    @Provides
    fun provideLoginInitialState(): LoginViewState =
      LoginViewState(false, null)
  }
}