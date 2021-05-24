package com.huchihaitachi.loginwebview.di

import com.huchihaitachi.loginwebview.di.component.LoginWebSubcomponent

interface LoginWebSubcomponentProvider {

  fun provideLoginWebSubcomponent(): LoginWebSubcomponent
}