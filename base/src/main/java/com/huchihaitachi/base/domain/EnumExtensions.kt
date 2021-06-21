package com.huchihaitachi.base.domain

import android.content.res.Resources
import com.huchihaitachi.base.R
import com.huchihaitachi.domain.Season
import com.huchihaitachi.domain.Type

val Type.stringRes: Int
  get() = when (this) {
    Type.MANGA -> R.string.manga
    else -> R.string.anime
  }

fun Season.localized(resources: Resources?): String? = when (this) {
  Season.WINTER -> resources?.getString(R.string.winter)
  Season.SPRING -> resources?.getString(R.string.spring)
  Season.SUMMER -> resources?.getString(R.string.summer)
  else -> resources?.getString(R.string.fall)
}

