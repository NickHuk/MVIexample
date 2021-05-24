package com.huchihaItachi.mviexample.di.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
interface ApplicationModule {

  companion object {
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
      context.getSharedPreferences(
        "${context.packageName}_preferences",
        Context.MODE_PRIVATE
      )
  }
}