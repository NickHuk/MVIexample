package com.huchihaitachi.anilist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Controller
import com.huchihaitachi.anilist.databinding.ControllerAnilistBinding
import com.huchihaitachi.anilist.di.AnilistSubcomponentProvider
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.PAGE
import com.huchihaitachi.anilist.presentation.animeList.AnimeEpoxyController
import com.huchihaitachi.base.visible
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.Type
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AnilistController : Controller(), AnilistView {
  @Inject lateinit var presenter: AnilistPresenter
  lateinit var animeEpoxyController: AnimeEpoxyController

  private var _binding: ControllerAnilistBinding? = null
  private val binding: ControllerAnilistBinding
    get() = _binding!!
  private val _loadAnimePage: PublishSubject<Unit> = PublishSubject.create()
  override val loadAnimePage: Observable<Unit>
    get() = _loadAnimePage
  private val _reload: PublishSubject<Unit> = PublishSubject.create()
  override val reload: Observable<Unit>
    get() = _reload

  override fun render(state: AnilistViewState) {
    state.anime?.let(animeEpoxyController::setData)
    binding.pageLoadingPb.visible = state.isLoading && state.loadingType == PAGE
    if(!state.isLoading) {
      binding.animeSrl.isRefreshing = false
    }
    binding.errorFooterTv.apply {
      visible = state.error != null
      state.error?.let { text = it }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup,
    savedViewState: Bundle?
  ): View {
    (activity as AnilistSubcomponentProvider).provideAnilistSubcomponent()
      .inject(this)
    _binding = ControllerAnilistBinding.inflate(inflater, container, false)
    setupViews()
    presenter.bind(this)
    presenter.bindIntents()
    return binding.root
  }

  override fun onDestroyView(view: View) {
    _binding = null
    presenter.unbind()
    super.onDestroyView(view)
  }

  private fun setupViews() {
    setupRecyclerView()
    setupSwipeToRefresh()
  }

  private fun setupSwipeToRefresh() {
    binding.animeSrl.setOnRefreshListener {
      _reload.onNext(Unit)
    }
  }

  private fun setupRecyclerView() {
    animeEpoxyController = AnimeEpoxyController()
    binding.animeErv.apply {
      layoutManager = LinearLayoutManager(context)
      adapter = animeEpoxyController.adapter
      addOnScrollListener(
        object: RecyclerView.OnScrollListener() {
          override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            (layoutManager as LinearLayoutManager).let { linearLayoutManager ->
              val totalItemsCount = linearLayoutManager.itemCount
              val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
              if(lastVisibleItemPosition != RecyclerView.NO_POSITION
                && lastVisibleItemPosition + ANIME_ITEMS_RESERVE >= totalItemsCount) {
                _loadAnimePage.onNext(Unit)
              }
            }
          }
        }
      )
    }
  }

  companion object {
    const val ANIME_ITEMS_RESERVE = 4
  }
}