package com.huchihaItachi.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PageEntity(
  @PrimaryKey val currentPage: Int,
  val perPage: Int?,
  val hasNextPage: Boolean?,
  val timeOfBirth: Long,
  val timeToStale: Long
)