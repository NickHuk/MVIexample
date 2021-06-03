package com.huchihaItachi.mviexample.di.module

import com.huchihaItachi.database.dataSource.AnimeLocalDataSourceImpl
import com.huchihaItachi.mviexample.dataSource.ResourcesDataSourceImpl
import com.huchihaItachi.mviexample.dataSource.TimeDataSourceImpl
import com.huchihaItachi.mviexample.dataSource.UserDataSourceImpl
import com.huchihaitachi.datasource.AnimeLocalDataSource
import com.huchihaitachi.datasource.AnimeRemoteDataSource
import com.huchihaitachi.datasource.ResourcesDataSource
import com.huchihaitachi.datasource.TimeDataSource
import com.huchihaitachi.datasource.UserDataSource
import com.huchihaitachi.remoteapi.dataSource.AnimeRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
interface DataSourceModule {

  @Binds
  fun bindUserDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource

  @Binds
  fun bindAnimeRemoteDataSource(animeRemoteDataSourceImpl: AnimeRemoteDataSourceImpl): AnimeRemoteDataSource

  @Binds
  fun bindAnimeLocalDataSource(animeLocalDataSourceImpl: AnimeLocalDataSourceImpl): AnimeLocalDataSource

  @Binds
  fun bindResourcesDataSource(resourcesDataSourceImpl: ResourcesDataSourceImpl): ResourcesDataSource

  @Binds
  fun bindTimeDataSource(timeDataSourceImpl: TimeDataSourceImpl): TimeDataSource
}