package com.huchihaitachi.anilist.di.component

import com.huchihaitachi.anilist.presentation.AnilistController
import com.huchihaitachi.anilist.di.moule.AnilistModule
import com.huchihaitachi.anilist.di.scope.AnilistScope
import dagger.Subcomponent

@AnilistScope
@Subcomponent(
  modules = [
    AnilistModule::class
  ]
)
interface AnilistSubcomponent {

  @Subcomponent.Factory
  interface Factory {
    fun create(): AnilistSubcomponent
  }

  fun inject(anilistController: AnilistController)
}