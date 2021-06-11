package com.huchihaItachi.mviexample.dataSource

import com.huchihaitachi.datasource.TimeDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeDataSourceImpl @Inject constructor() : TimeDataSource {

  override fun currentTimeMillis(): Long = System.currentTimeMillis()
}