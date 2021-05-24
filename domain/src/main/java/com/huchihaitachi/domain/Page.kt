package com.huchihaitachi.domain

data class Page(
  val total: Int?,
  val perPage: Int?,
  val currentPage: Int?,
  val lastPage: Int?,
  val hasNextPage: Boolean?,
  val anime: List<Anime>?
)