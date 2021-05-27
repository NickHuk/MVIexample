package com.huchihaItachi.mviexample.di.module

import com.huchihaItachi.mviexample.dataSource.ResourcesDataSourceImpl
import com.huchihaItachi.mviexample.dataSource.UserDataSourceImpl
import com.huchihaitachi.datasource.AnimeDataSource
import com.huchihaitachi.datasource.ResourcesDataSource
import com.huchihaitachi.datasource.UserDataSource
import com.huchihaitachi.remoteapi.dataSource.AnimeDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
interface DataSourceModule {

  @Binds
  fun bindUserDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource

  @Binds
  fun bindAnimeDataSource(animeDataSourceImpl: AnimeDataSourceImpl): AnimeDataSource

  @Binds
  fun bindResourcesDataSource(resourcesDataSourceImpl: ResourcesDataSourceImpl): ResourcesDataSource
}