package com.huchihaItachi.mviexample.repositoryImpl

import com.huchihaitachi.datasource.ResourcesDataSource
import com.huchihaitachi.repository.ResourcesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesRepositoryImpl @Inject constructor(
  private val resourcesDataSource: ResourcesDataSource
) : ResourcesRepository {

  override fun getString(resId: Int, vararg params: Any): String =
    resourcesDataSource.getString(resId, *params)
}