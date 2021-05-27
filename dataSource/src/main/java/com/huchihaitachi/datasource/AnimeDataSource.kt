package com.huchihaitachi.datasource

import com.huchihaitachi.domain.Page
import io.reactivex.Single

interface AnimeDataSource {

  fun loadAnimePage(page: Int, perPage: Int): Single<Page>
}