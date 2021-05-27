package com.huchihaitachi.repository

import androidx.annotation.StringRes

interface ResourcesRepository {

  fun getString(@StringRes resId: Int, vararg params: Any = emptyArray()): String
}