package com.huchihaItachi.database.entity

import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.Page

fun Page.toDbEntity(): PageEntity = PageEntity(currentPage, perPage, hasNextPage, timeOfBirth, timeToStale)

fun PageAndAnime.toDomain() =
    Page(
      page.currentPage,
      page.perPage,
      page.hasNextPage,
      anime.map(AnimeEntity::toDomain),
      page.timeOfBirth,
      page.timeToStale
    )

fun Anime.toDbEntity(pageNum: Int, index: Int): AnimeEntity =
  AnimeEntity(
    id,
    pageNum,
    index,
    title,
    type,
    description,
    season,
    seasonYear,
    episodes,
    duration,
    coverImage,
    bannerImage,
    timeOfBirth,
    timeToStale
  )

fun AnimeEntity.toDomain(): Anime =
  Anime(
    id,
    title,
    type,
    description,
    season,
    seasonYear,
    episodes,
    duration,
    coverImage,
    bannerImage,
    timeOfBirth,
    timeToStale
  )