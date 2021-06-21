package com.huchihaItachi.mviexample.useCaseImpl

import com.huchihaitachi.domain.Page
import com.huchihaitachi.repository.AnimeRepository
import com.huchihaitachi.usecase.LoadPageUseCase
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadPageUseCaseImpl @Inject constructor(
  private val animeRepository: AnimeRepository
) : LoadPageUseCase {

  override fun invoke(page: Int, perPage: Int): Single<Page> =
    animeRepository.getAnimePage(page, perPage)
}