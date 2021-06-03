package com.huchihaitachi.datasource

interface PreferencesDataSource {

  var updateDeadline: Long
  var lastLoadedPage: Int
  var hasNextPage: Boolean
}