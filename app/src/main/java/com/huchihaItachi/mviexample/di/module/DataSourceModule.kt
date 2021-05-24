package com.huchihaItachi.mviexample.di.module

import com.huchihaItachi.mviexample.dataSource.UserDataSourceImpl
import com.huchihaitachi.datasource.UserDataSource
import dagger.Binds
import dagger.Module

@Module
interface DataSourceModule {

  @Binds
  fun bindUserDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource
}