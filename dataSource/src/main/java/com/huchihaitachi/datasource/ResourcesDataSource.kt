package com.huchihaitachi.datasource

interface ResourcesDataSource {
  fun getString(resId: Int, vararg params: Any = emptyArray()): String
}