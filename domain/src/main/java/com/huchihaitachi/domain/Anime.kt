package com.huchihaitachi.domain

data class Anime(
  val id: Int?,
  val title: String?,
  val type: Type?,
  val description: String?,
  val thumbnail: String?,
  val numEpisodes: Int?,
  val duration: Int? //minutes in general
)