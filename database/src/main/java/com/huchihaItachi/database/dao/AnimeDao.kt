package com.huchihaItachi.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huchihaItachi.database.entity.AnimeEntity
import com.huchihaitachi.domain.Season
import com.huchihaitachi.domain.Type
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface AnimeDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(anime: List<AnimeEntity>): Completable

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(anime: AnimeEntity): Completable

  @Query("""SELECT * FROM AnimeEntity WHERE id=:id""")
  fun loadAnime(id: Int): Single<AnimeEntity>

  @Query(
    """UPDATE AnimeEntity SET 
    title=:title,
    type=:type,
    description=:description,
    season=:season,
    seasonYear=:seasonYear,
    episodes=:episodes,
    duration=:duration,
    coverImage=:coverImage,
    bannerImage=:bannerImage,
    timeOfBirth=:timeOfBirth,
    timeToStale=:timeToStale
    WHERE id=:id"""
  )
  fun updateAnime(
    id: Int,
    title: String?,
    type: Type?,
    description: String?,
    season: Season?,
    seasonYear: Int?,
    episodes: Int?,
    duration: Int?,
    coverImage: String?,
    bannerImage: String?,
    timeOfBirth: Long,
    timeToStale: Long
  ): Completable

  @Query("""DELETE FROM AnimeEntity""")
  fun deleteAll(): Completable
}