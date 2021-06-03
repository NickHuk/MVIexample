package com.huchihaItachi.mviexample.useCaseImpl

import com.huchihaitachi.domain.Page
import com.huchihaitachi.repository.AnimeRepository
import com.huchihaitachi.usecase.RefreshPageUseCase
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshPageUseCaseImpl @Inject constructor(
  private val animeRepository: AnimeRepository
) : RefreshPageUseCase {

  override fun invoke(perPage: Int): Single<Page> = animeRepository.refresh(perPage)
}