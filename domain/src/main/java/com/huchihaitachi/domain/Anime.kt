package com.huchihaitachi.domain

open class Anime(
  val id: Int,
  val title: String? = null,
  val type: Type? = null,
  val description: String? = null,
  val season: Season? = null,
  val seasonYear: Int? = null,
  val episodes: Int?  = null,
  val duration: Int? = null, //minutes in general
  val coverImage: String? = null,
  val bannerImage: String? = null,
  override val timeOfBirth: Long = 0,
  override val timeToStale: Long = 0
) : Dirtyable {

  override fun equals(other: Any?) =
    this === other || (other is  Anime)
      && id == other.id
      && title == other.title
      && type == other.type
      && description == other.description
      && season == other.season
      && seasonYear == other.seasonYear
      && episodes == other.episodes
      && duration == other.duration
      && coverImage == other.coverImage
      && bannerImage == other.bannerImage
      && timeOfBirth == other.timeOfBirth
      && timeToStale == other.timeToStale

  override fun hashCode(): Int {
    return id * 31 +
      (title?.hashCode() ?: 0) * 31 +
      (type?.hashCode() ?: 0) * 31 +
      (description?.hashCode() ?: 0) * 31 +
      (season?.hashCode() ?: 0) * 31 +
      (seasonYear ?: 0) * 31 +
      (episodes ?: 0) * 31 +
      (duration ?: 0) * 31 +
      (coverImage?.hashCode() ?: 0) * 31 +
      (bannerImage?.hashCode() ?: 0) * 31 +
      timeOfBirth.hashCode() * 31 +
      timeToStale.hashCode() * 31
  }

  fun copy(
    id: Int = this.id,
    title: String? = this.title,
    type: Type? = this.type,
    description: String? = this.description,
    season: Season? = this.season,
    seasonYear: Int? = this.seasonYear,
    episodes: Int? = this.episodes,
    duration: Int? = this.duration, //minutes in general
    coverImage: String? = this.coverImage,
    bannerImage: String? = this.bannerImage,
    timeOfBirth: Long = this.timeOfBirth,
    timeToStale: Long = this.timeToStale
  ) = Anime(
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
}