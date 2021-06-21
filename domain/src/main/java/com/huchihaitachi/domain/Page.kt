package com.huchihaitachi.domain

open class Page(
  val currentPage: Int,
  val perPage: Int?,
  val hasNextPage: Boolean?,
  val anime: List<Anime>?,
  override val timeOfBirth: Long,
  override val timeToStale: Long,
) : Dirtyable