package com.huchihaitachi.domain

open class Anime(
  val id: Int,
  val title: String? = null,
  val type: Type? = null,
  val description: String? = null,
  val season: Season? = null,
  val seasonYear: Int? = null,
  val episodes: Int? = null,
  val duration: Int? = null, //minutes in general
  val coverImage: String? = null,
  val bannerImage: String? = null,
  override val timeOfBirth: Long,
  override val timeToStale: Long
) : Dirtyable