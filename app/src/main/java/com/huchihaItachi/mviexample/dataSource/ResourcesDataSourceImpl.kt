package com.huchihaItachi.mviexample.dataSource

import android.content.res.Resources
import androidx.annotation.StringRes
import com.huchihaitachi.datasource.ResourcesDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesDataSourceImpl @Inject constructor(
  private val resources: Resources
) : ResourcesDataSource {

  override fun getString(@StringRes resId: Int, vararg params: Any): String =
    resources.getString(resId, *params)
}