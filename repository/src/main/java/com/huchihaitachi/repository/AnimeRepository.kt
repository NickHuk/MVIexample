package com.huchihaitachi.repository

import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.Page
import io.reactivex.Single

interface AnimeRepository {

  fun getAnimePage(pageNum: Int, perPage: Int): Single<Page>
  fun refresh(perPage: Int): Single<Page>
  fun getAnime(id: Int): Single<Anime>
}