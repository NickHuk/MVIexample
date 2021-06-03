package com.huchihaitachi.anilist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.huchihaitachi.base.domain.localized
import com.huchihaitachi.base.domain.stringRes
import com.huchihaitachi.base.setUrl
import com.huchihaitachi.base.visible
import com.huchihaitachi.domain.Anime
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
      BottomSheetBehavior.STATE_COLLAPSED
    }
    else {
      bindDetailsData(state.details)
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
            BottomSheetBehavior.STATE_COLLAPSED -> { _hideDetails.onNext(Unit) }
          }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }
      }
    )
  }

  private fun bindDetailsData(details: Anime) {
    binding.detailsL.let { detailsBinding ->
      details.coverImage?.let(detailsBinding.detailsCoverIv::setUrl)
      details.title?.let { title ->
        detailsBinding.titleTv.text = resources?.getString(
          R.string.title,
          title,
          details.type?.stringRes?.let { resources?.getString(it) }
        )
      }
      detailsBinding.beginningTv.text = resources?.getString(
        R.string.beginning,
        details.season?.localized(resources),
        details.seasonYear
      )
      details.episodes?.let { episodes ->
        detailsBinding.episodesTv.text = resources?.getString(R.string.num_episodes, episodes)
      }
      details.duration?.let { duration ->
        (duration / 60).let { hours ->
          detailsBinding.durationTv.text = if(hours == 0) {
            resources?.getString(R.string.episode_duration_minutes, duration % 60)
          } else {
            resources?.getString(R.string.episode_duration_hours, hours, duration % 60)
          }
        }
      }
      details.description?.let { description -> detailsBinding.descriptionTv.text = details.description }
    }
  }

  companion object {
    const val ANIME_ITEMS_RESERVE = 4
  }
}