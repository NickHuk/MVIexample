package com.huchihaitachi.anilist.presentation

import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.PAGE
import com.huchihaitachi.base.BaseViewState
import com.huchihaitachi.domain.Anime

data class AnilistViewState(
  val isLoading: Boolean = false,
  val loadingType: LoadingType = PAGE,
  val details: Anime? = null,
  val anime: List<Anime>? = null,
  val total: Int? = null,
  val currentPage: Int? = null,
  val lastPage: Int? = null,
  val hasNextPage: Boolean? = null,
  val error: String? = null
) : BaseViewState {

  enum class LoadingType {
    PAGE,
    RELOAD,
    DETAILS
  }
}