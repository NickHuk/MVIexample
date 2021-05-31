package com.huchihaitachi.remoteapi.dataSource

import com.huchihaitachi.domain.Season
import com.huchihaitachi.domain.Type
import com.huchihaitachi.remoteapi.type.MediaSeason

import com.huchihaitachi.remoteapi.type.MediaType

fun MediaType.toDomain() = when (this) {
  MediaType.MANGA -> Type.MANGA
  else -> Type.ANIME
}

fun MediaSeason.toDomain() = when (this) {
  MediaSeason.WINTER -> Season.WINTER
  MediaSeason.SPRING -> Season.SPRING
  MediaSeason.SUMMER -> Season.SUMMER
  else -> Season.FALL
}