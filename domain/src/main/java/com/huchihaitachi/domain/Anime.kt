package com.huchihaitachi.domain

data class Anime(
  val id: Int,
  val title: String? = null,
  val type: Type? = null,
  val description: String? = null,
  val season: Season? = null,
  val seasonYear: Int? = null,
  val episodes: Int?  = null,
  val duration: Int? = null, //minutes in general
  val coverImage: String? = null,
  val bannerImage: String? = null
)