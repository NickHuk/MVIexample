package com.huchihaitachi.usecase

import com.huchihaitachi.domain.Anime
import io.reactivex.Single

interface LoadAnimeUseCase {
  operator fun invoke(id: Int): Single<Anime>
}