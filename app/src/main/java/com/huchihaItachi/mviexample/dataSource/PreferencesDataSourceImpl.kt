package com.huchihaItachi.mviexample.dataSource

import android.content.SharedPreferences
import androidx.core.content.edit
import com.huchihaitachi.datasource.PreferencesDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesDataSourceImpl @Inject constructor(
  private val sharedPreferences: SharedPreferences
) : PreferencesDataSource {

  override var updateDeadline: Long
    get() = sharedPreferences.getLong(UPDATE_DEADLINE, 0L)
    set(value) = sharedPreferences.edit {
        putLong(UPDATE_DEADLINE,value)
    }

  override var lastLoadedPage: Int
    get() = sharedPreferences.getInt(LAST_LOADED_PAGE, 0)
    set(value) = sharedPreferences.edit {
    putInt(LAST_LOADED_PAGE ,value)
  }

  override var hasNextPage: Boolean
    get() = sharedPreferences.getBoolean(HAS_NEXT_PAGE, true)
    set(value) = sharedPreferences.edit {
      putBoolean(HAS_NEXT_PAGE, value)
    }

  companion object {
    const val LAST_LOADED_PAGE = "last_loaded_page"
    const val HAS_NEXT_PAGE = "has_next_page"
    const val UPDATE_DEADLINE = "update_deadline"
  }
}