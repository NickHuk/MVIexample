package com.huchihaitachi.usecase

import com.huchihaitachi.domain.Page
import io.reactivex.Single

interface RefreshPageUseCase {

  operator fun invoke(perPage: Int): Single<Page>
}