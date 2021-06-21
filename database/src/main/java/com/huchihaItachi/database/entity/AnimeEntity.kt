package com.huchihaItachi.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.huchihaitachi.domain.Season
import com.huchihaitachi.domain.Type

@Entity
data class AnimeEntity(
  @PrimaryKey val id: Int,
  val pageNum: Int,
  val index: Int,
  val title: String? = null,
  val type: Type? = null,
  val description: String? = null,
  val season: Season? = null,
  val seasonYear: Int? = null,
  val episodes: Int? = null,
  val duration: Int? = null, //minutes in general
  val coverImage: String? = null,
  val bannerImage: String? = null,
  val timeOfBirth: Long,
  val timeToStale: Long
)