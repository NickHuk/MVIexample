package com.huchihaItachi.mviexample.useCaseImpl

import com.huchihaitachi.domain.Anime
import com.huchihaitachi.repository.AnimeRepository
import com.huchihaitachi.usecase.LoadAnimeUseCase
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadAnimeUseCaseImpl @Inject constructor(
  private val animeRepository: AnimeRepository
) : LoadAnimeUseCase {

  override fun invoke(id: Int): Single<Anime> = animeRepository.getAnime(id)
}