package com.huchihaItachi.database.dataSource

import androidx.room.EmptyResultSetException
import com.huchihaItachi.database.dao.AnimeDao
import com.huchihaItachi.database.dao.PageDao
import com.huchihaItachi.database.entity.AnimeEntity
import com.huchihaItachi.database.entity.PageAndAnime
import com.huchihaItachi.database.entity.toDbEntity
import com.huchihaItachi.database.entity.toDomain
import com.huchihaitachi.datasource.AnimeLocalDataSource
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.EmptyAnime
import com.huchihaitachi.domain.EmptyDirtyable
import com.huchihaitachi.domain.EmptyPage
import com.huchihaitachi.domain.Page
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeLocalDataSourceImpl @Inject constructor(
  private val pageDao: PageDao,
  private val animeDao: AnimeDao
) : AnimeLocalDataSource {

  override fun createPage(page: Page): Completable =
    pageDao.create(page.toDbEntity())
      .andThen(
        page.anime
          ?.mapIndexed { i, anime -> anime.toDbEntity(page.currentPage, i) }
          ?.let { anime -> animeDao.create(anime) }
      )

  override fun loadPage(pageNum: Int): Single<Page> =
    pageDao.loadPage(pageNum)
      .map(PageAndAnime::toDomain)
      .onErrorReturn { throwable ->
        when(throwable) {
          is EmptyResultSetException -> EmptyPage
          else -> throw throwable
        }
      }

  override fun deletePages(): Completable =
    animeDao.deleteAll().andThen(
      pageDao.deleteAll()
    )

  override fun updateAnime(anime: Anime): Completable =
    animeDao.updateAnime(
      anime.id,
      anime.title,
      anime.type,
      anime.description,
      anime.season,
      anime.seasonYear,
      anime.episodes,
      anime.duration,
      anime.coverImage,
      anime.bannerImage,
      anime.timeOfBirth,
      anime.timeToStale
    )

  override fun loadAnime(id: Int): Single<Anime> =
    animeDao.loadAnime(id)
      .map(AnimeEntity::toDomain)
      .onErrorReturn { throwable ->
        when(throwable) {
          is EmptyResultSetException -> EmptyAnime
          else -> throw throwable
        }
      }
}