package com.huchihaitachi.anilist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.bluelinelabs.conductor.Controller
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.card.MaterialCardView
import com.huchihaitachi.anilist.R
import com.huchihaitachi.anilist.databinding.ControllerAnilistBinding
import com.huchihaitachi.anilist.di.AnilistSubcomponentProvider
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.PAGE
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.RELOAD
import com.huchihaitachi.anilist.presentation.animeList.AnimeEpoxyController
import com.huchihaitachi.base.SpanFactory
import com.huchihaitachi.base.domain.stringRes
import com.huchihaitachi.base.setTextAndHighlight
import com.huchihaitachi.base.setUrl
import com.huchihaitachi.base.visible
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.Type
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AnilistController : Controller(), AnilistView {
  @Inject lateinit var presenter: AnilistPresenter
  @Inject lateinit var spanFactory: SpanFactory
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
  private val _showDetails: PublishSubject<Int> = PublishSubject.create()
  override val showDetails: Observable<Int>
    get() = _showDetails
  private val _hideDetails: PublishSubject<Unit> = PublishSubject.create()
  override val hideDetails: Observable<Unit>
    get() = _hideDetails

  private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>

  override fun render(state: AnilistViewState) {
    state.pageState?.anime?.let(animeEpoxyController::setData)
    binding.animeListL.pageLoadingPb.visible = state.loading == PAGE
    binding.animeListL.totalFooterTv.visible = state.loading != PAGE
    binding.animeListL.totalFooterTv.text = resources?.getString(R.string.total, state.pageState?.anime?.size)
    binding.animeListL.animeSrl.isRefreshing = state.loading == RELOAD
    binding.animeListL.errorFooterTv.apply {
      visible = state.error != null
      state.error?.let { text = it }
    }
    bottomSheetBehavior.state = if(state.details == null) {
      binding.animeListL.dimOverlayView.isClickable = false
      binding.animeListL.dimOverlayView.animate()
        .alpha(0f)
        .apply {
          duration = resources?.getInteger(R.integer.bottom_sheet_fade_duration)?.toLong() ?: 0L
          interpolator = AccelerateDecelerateInterpolator()
        }
      BottomSheetBehavior.STATE_COLLAPSED
    }
    else {
      bindDetailsData(state.details)
      binding.animeListL.dimOverlayView.isClickable = true
      binding.animeListL.dimOverlayView.animate()
        .alpha(1f)
        .apply {
          duration = resources?.getInteger(R.integer.bottom_sheet_fade_duration)?.toLong() ?: 0L
          interpolator = AccelerateDecelerateInterpolator()
        }
      BottomSheetBehavior.STATE_EXPANDED
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
    _loadAnimePage.onNext(Unit)
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
    setupContentOverlay()
    setupBottomSheet()
  }

  private fun setupSwipeToRefresh() {
    binding.animeListL.animeSrl.setOnRefreshListener {
      _reload.onNext(Unit)
    }
  }

  private fun setupRecyclerView() {
    animeEpoxyController = AnimeEpoxyController { details ->
      _showDetails.onNext(details)
    }
    binding.animeListL.animeErv.apply {
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
              } else {
                _hideDetails.onNext(Unit)
              }
            }
          }

          override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(!recyclerView.canScrollVertically(DIRECTION_DOWN)
              && newState == SCROLL_STATE_DRAGGING) { //can scroll down
              _loadAnimePage.onNext(Unit)
            }
          }
        }
      )
    }
  }

  private fun setupBottomSheet() {

    bottomSheetBehavior = BottomSheetBehavior.from(binding.detailsL.root)
    bottomSheetBehavior.addBottomSheetCallback(
      object : BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
          when(newState) {
            BottomSheetBehavior.STATE_COLLAPSED -> {
              _hideDetails.onNext(Unit) }
          }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }
      }
    )
  }

  private fun setupContentOverlay() {
    binding.animeListL.dimOverlayView.setOnClickListener {
      _hideDetails.onNext(Unit)
    }
  }

  private fun bindDetailsData(details: Anime) {
    resources?.let { res ->
      binding.detailsL.let { detailsBinding ->
        detailsBinding.titleTv.setTextAndHighlight(
          res,
          spanFactory.createBold(),
          R.string.title,
          R.string.highlighted_title,
          details.title,
          (details.type ?: Type.ANIME).stringRes.let(res::getString)
        )
        details.coverImage?.let(detailsBinding.detailsCoverIv::setUrl)
        detailsBinding.episodesTv.setTextAndHighlight(
          res,
          spanFactory.createBold(),
          R.string.num_episodes, R.string.highlighted_num_episodes,
          details.episodes
        )
        details.duration?.let { duration ->
          (duration / 60).let { hours ->
            if(hours == 0) {
              detailsBinding.durationTv.setTextAndHighlight(
                res,
                spanFactory.createBold(),
                R.string.episode_duration_minutes,
                R.string.highlighted_episode_duration,
                duration % 60
              )
            } else {
              detailsBinding.durationTv.setTextAndHighlight(
                res,
                spanFactory.createBold(),
                R.string.episode_duration_hours,
                R.string.highlighted_episode_duration,
                hours,
                duration % 60
              )
            }

          }
        }
        details.description?.let { description -> detailsBinding.descriptionTv.text = details.description }
      }
    }
  }

  companion object {
    const val ANIME_ITEMS_RESERVE = 4
    const val DIRECTION_DOWN = 1
  }
}