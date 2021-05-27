package com.huchihaItachi.mviexample.di.module

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface ApplicationModule {

  companion object {
    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
      context.getSharedPreferences(
        "${context.packageName}_preferences",
        Context.MODE_PRIVATE
      )

    @Singleton
    @Provides
    fun provideResources(context: Context): Resources = context.resources
  }
}