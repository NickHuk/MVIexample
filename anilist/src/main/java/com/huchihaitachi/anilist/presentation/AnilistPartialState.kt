package com.huchihaitachi.anilist.presentation

import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.VIEW_CONTENT
import com.huchihaitachi.anilist.presentation.AnilistViewState.PageState
import com.huchihaitachi.domain.Anime

data class AnilistPartialState (
  val loading: LoadingType = VIEW_CONTENT,
  val details: Anime? = null,
  val pageState: PageState? = null,
  val error: String? = null,
  val loadingEnabled: Boolean = true,
  val backoff: Int = 0
) {

  fun createState() = AnilistViewState(
    loading,
    details,
    pageState,
    error
  )
}