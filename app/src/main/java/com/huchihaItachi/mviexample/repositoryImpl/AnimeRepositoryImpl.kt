package com.huchihaItachi.mviexample.repositoryImpl

import com.huchihaitachi.datasource.AnimeDataSource
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.Page
import com.huchihaitachi.repository.AnimeRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepositoryImpl @Inject constructor(
  private val animeDataSource: AnimeDataSource
) : AnimeRepository {

  override fun getAnimePage(page: Int, perPage: Int): Single<Page> =
    animeDataSource.loadAnimePage(page, perPage)

  override fun getAnime(id: Int): Single<Anime> =
    animeDataSource.loadAnime(id)
}