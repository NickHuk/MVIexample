package com.huchihaItachi.mviexample.di.module

import com.huchihaItachi.mviexample.repositoryImpl.UserRepositoryImpl
import com.huchihaitachi.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

  @Binds
  fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}