package com.huchihaItachi.mviexample.repositoryImpl

import com.huchihaitachi.datasource.AnimeLocalDataSource
import com.huchihaitachi.datasource.AnimeRemoteDataSource
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.Page
import com.huchihaitachi.domain.TimeProvider
import com.huchihaitachi.domain.isEmptyOrDirty
import com.huchihaitachi.repository.AnimeRepository
import io.reactivex.Observable
import io.reactivex.Single
import java.util.Optional.empty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepositoryImpl @Inject constructor(
  private val animeRemoteDataSource: AnimeRemoteDataSource,
  private val animeLocalDataSource: AnimeLocalDataSource,
  private val timeProvider: TimeProvider
) : AnimeRepository {

  override fun getAnimePage(pageNum: Int, perPage: Int): Single<Page> =
    Single.concat(
      animeLocalDataSource.loadPage(pageNum),
      animeRemoteDataSource.loadAnimePage(pageNum, perPage)
        .flatMap { page ->
          animeLocalDataSource.createPage(page)
            .andThen(
              animeLocalDataSource.loadPage(pageNum)
            )
        }
    )
      .filter { page -> !page.isEmptyOrDirty(timeProvider.currentTimeInMillis()) }
      .firstOrError()

  override fun refresh(perPage: Int): Single<Page> =
    animeRemoteDataSource.loadAnimePage(1, perPage)
      .flatMap { page ->
        animeLocalDataSource.deletePages()
          .andThen(
            animeLocalDataSource.createPage(page)
          )
          .andThen(
            animeLocalDataSource.loadPage(page.currentPage)
          )
      }

  override fun getAnime(id: Int): Single<Anime> =
    Single.concat(
      animeLocalDataSource.loadAnime(id),
      animeRemoteDataSource.loadAnime(id)
        .flatMap { anime ->
          animeLocalDataSource.updateAnime(anime)
            .andThen(animeLocalDataSource.loadAnime(id))
        }
    )
      .filter{ anime -> !anime.isEmptyOrDirty(timeProvider.currentTimeInMillis())
        && !(anime.type == null && anime.description == null && anime.season == null
        && anime.seasonYear == null && anime.episodes == null && anime.duration == null) }
      .firstOrError()
}