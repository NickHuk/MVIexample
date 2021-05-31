package com.huchihaitachi.anilist.presentation.animeList

import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.TypedEpoxyController
import com.huchihaitachi.domain.Anime

class AnimeEpoxyController(val onClickListener: (Int) -> Unit): TypedEpoxyController<List<Anime>>(
  EpoxyAsyncUtil.getAsyncBackgroundHandler(),
  EpoxyAsyncUtil.getAsyncBackgroundHandler()
) {

  override fun buildModels(anime: List<Anime>?) {
    anime?.forEachIndexed { i, anm ->
      AnimeViewModel_()
        .id(anm.id)
        .name(anm.title ?: "")
        .cover(anm.coverImage ?: "")
        .onClickListener { view -> onClickListener(anm.id) }
        .addTo(this)
    }
  }
}