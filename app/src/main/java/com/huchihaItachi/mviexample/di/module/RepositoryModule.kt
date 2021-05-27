package com.huchihaItachi.mviexample.di.module

import com.huchihaItachi.mviexample.repositoryImpl.AnimeRepositoryImpl
import com.huchihaItachi.mviexample.repositoryImpl.ResourcesRepositoryImpl
import com.huchihaItachi.mviexample.repositoryImpl.UserRepositoryImpl
import com.huchihaitachi.repository.AnimeRepository
import com.huchihaitachi.repository.ResourcesRepository
import com.huchihaitachi.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

  @Binds
  fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

  @Binds
  fun bindAnimeRepository(animeRepositoryImpl: AnimeRepositoryImpl): AnimeRepository

  @Binds
  fun bindResourcesRepository(resourcesRepositoryImpl: ResourcesRepositoryImpl): ResourcesRepository
}