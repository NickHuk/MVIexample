package com.huchihaitachi.anilist.presentation

import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.NOT_LOADING
import com.huchihaitachi.anilist.presentation.AnilistViewState.PageState
import com.huchihaitachi.domain.Anime

data class AnilistPartialState (
  val loading: LoadingType = NOT_LOADING,
  val details: Anime? = null,
  val pageState: PageState? = null,
  val error: String? = null
) {

  fun createState() = AnilistViewState(
    loading,
    details,
    pageState,
    error
  )
}