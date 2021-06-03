package com.huchihaitachi.loginwebview.di.module

import com.huchihaitachi.loginwebview.di.scope.LoginWebScope
import com.huchihaitachi.loginwebview.presentation.LoginWebViewState
import dagger.Module
import dagger.Provides

@Module
interface LoginWebModule {

  companion object {
    @LoginWebScope
    @Provides
    fun provideLoginWebInitialState(): LoginWebViewState =
      LoginWebViewState(false, null)
  }
}