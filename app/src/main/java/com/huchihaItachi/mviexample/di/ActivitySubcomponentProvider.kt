package com.huchihaItachi.mviexample.di

import com.huchihaItachi.mviexample.di.component.ActivitySubcomponent

interface ActivitySubcomponentProvider {
  fun provideActivitySubcomponentFactory(): ActivitySubcomponent.Factory
}