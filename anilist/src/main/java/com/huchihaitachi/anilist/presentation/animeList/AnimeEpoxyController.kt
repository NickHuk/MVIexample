package com.huchihaitachi.anilist.presentation.animeList

import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.TypedEpoxyController
import com.huchihaitachi.domain.Anime

class AnimeEpoxyController: TypedEpoxyController<List<Anime>>(
  EpoxyAsyncUtil.getAsyncBackgroundHandler(),
  EpoxyAsyncUtil.getAsyncBackgroundHandler()
) {

  override fun buildModels(anime: List<Anime>?) {
    anime?.forEach { a ->
      AnimeViewModel_()
        .id(a.id)
        .name(a.title ?: "")
        .cover(a.coverImage ?: "")
        .addTo(this)
    }
  }
}