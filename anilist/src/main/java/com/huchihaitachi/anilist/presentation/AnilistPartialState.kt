package com.huchihaitachi.anilist.presentation

import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.PAGE
import com.huchihaitachi.domain.Anime

data class AnilistPartialState (
  val isLoading: Boolean = false,
  val loadingType: LoadingType = PAGE,
  val anime: List<Anime>? = null,
  val total: Int? = null,
  val currentPage: Int? = null,
  val lastPage: Int? = null,
  val hasNextPage: Boolean? = null,
  val error: String? = null
) {

  fun createState() = AnilistViewState(
    isLoading,
    loadingType,
    anime,
    total,
    currentPage,
    lastPage,
    hasNextPage,
    error
  )
}