package com.huchihaitachi.anilist.presentation

import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.NOT_LOADING
import com.huchihaitachi.base.BaseViewState
import com.huchihaitachi.domain.Anime

data class AnilistViewState(
  val loading: LoadingType = NOT_LOADING,
  val details: Anime? = null,
  val pageState: PageState? = null,
  val error: String? = null,
  val loadingEnabled: Boolean = true,
  val backoff: Int = 0
) : BaseViewState {

  data class PageState(
    val anime: List<Anime>? = null,
    val currentPage: Int? = null,
    val hasNextPage: Boolean? = null
  )

  enum class LoadingType {
    PAGE,
    REFRESH,
    NOT_LOADING
  }
}