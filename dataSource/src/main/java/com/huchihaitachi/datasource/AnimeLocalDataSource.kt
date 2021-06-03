package com.huchihaitachi.datasource

import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.Page
import io.reactivex.Completable
import io.reactivex.Single

interface AnimeLocalDataSource {

  fun createPage(page: Page): Completable
  fun loadPage(pageNum: Int): Single<Page>
  fun deletePages(): Completable
  fun updateAnime(anime: Anime): Completable
  fun loadAnime(id: Int): Single<Anime>
}