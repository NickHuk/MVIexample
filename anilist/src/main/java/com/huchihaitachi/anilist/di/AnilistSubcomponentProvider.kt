package com.huchihaitachi.anilist.di

import com.huchihaitachi.anilist.di.component.AnilistSubcomponent

interface AnilistSubcomponentProvider {
  fun provideAnilistSubcomponent(): AnilistSubcomponent
}