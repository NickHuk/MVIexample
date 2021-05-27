package com.huchihaitachi.usecase

import com.huchihaitachi.domain.Page
import io.reactivex.Single

interface LoadPageUseCase {

  operator fun invoke(page: Int, perPage: Int): Single<Page>
}