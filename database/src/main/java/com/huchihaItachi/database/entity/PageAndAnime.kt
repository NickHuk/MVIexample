package com.huchihaItachi.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PageAndAnime(
  @Embedded val page: PageEntity,
  @Relation(
    parentColumn = "currentPage",
    entityColumn = "pageNum"
  )
  val anime: List<AnimeEntity>
)