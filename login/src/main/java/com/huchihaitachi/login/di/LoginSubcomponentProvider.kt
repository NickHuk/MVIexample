package com.huchihaitachi.login.di

import com.huchihaitachi.login.di.component.LoginSubcomponent

interface LoginSubcomponentProvider {
  fun provideLoginSubcomponent(): LoginSubcomponent
}