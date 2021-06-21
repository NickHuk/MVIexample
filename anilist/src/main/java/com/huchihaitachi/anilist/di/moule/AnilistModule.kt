package com.huchihaitachi.anilist.di.moule

import com.huchihaitachi.anilist.di.scope.AnilistScope
import com.huchihaitachi.anilist.presentation.AnilistViewState
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.VIEW_CONTENT
import com.huchihaitachi.anilist.presentation.AnilistViewState.PageState
import dagger.Module
import dagger.Provides

@Module
interface AnilistModule {

  companion object {
    @AnilistScope
    @Provides
    fun provideAnilistState(): AnilistViewState = AnilistViewState(
      VIEW_CONTENT,
      null,
      PageState(currentPage = 0, hasNextPage = true),
      null
    )
  }
}