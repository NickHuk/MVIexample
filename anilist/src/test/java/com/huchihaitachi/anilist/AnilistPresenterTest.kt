package com.huchihaitachi.anilist

import com.huchihaitachi.anilist.presentation.AnilistPresenter
import com.huchihaitachi.anilist.presentation.AnilistView
import com.huchihaitachi.anilist.presentation.AnilistViewState
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.PAGE
import com.huchihaitachi.anilist.presentation.AnilistViewState.PageState
import com.huchihaitachi.base.RxSchedulers
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.Page
import com.huchihaitachi.usecase.GetStringResourceUseCase
import com.huchihaitachi.usecase.LoadAnimeUseCase
import com.huchihaitachi.usecase.LoadPageUseCase
import com.huchihaitachi.usecase.RefreshPageUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verifyOrder
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class AnilistPresenterTest {
  @MockK private lateinit var loadPage: LoadPageUseCase
  @MockK private lateinit var refreshPage: RefreshPageUseCase
  @MockK private lateinit var loadAnime: LoadAnimeUseCase
  @MockK private lateinit var getStringResource: GetStringResourceUseCase
  @MockK private lateinit var anilistView: AnilistView
  @MockK private lateinit var rxSchedulers: RxSchedulers
  private lateinit var anime: List<Anime>
  private lateinit var testScheduler: Scheduler

  @Before
  fun setup() {
    MockKAnnotations.init(this)
    setupSchedulers()
    setupView()
    anime = listOf(Anime(1), Anime(2), Anime(3), Anime(4), Anime(5), Anime(6))
  }

  @Test
  fun `load first page test`() {
    val initialState = AnilistViewState(pageState = PageState(null, 0, true))
    val presenter = AnilistPresenter(
      loadPage, refreshPage, loadAnime, getStringResource, initialState, rxSchedulers
    )
    val loadAnimePageMock: PublishSubject<Unit> = PublishSubject.create()
    every { anilistView.loadAnimePage } returns loadAnimePageMock
    every { anilistView.reload } returns PublishSubject.create()
    every { anilistView.showDetails } returns PublishSubject.create()
    every { anilistView.hideDetails } returns PublishSubject.create()
    every { loadPage(any(), any()) } returns Single.just(
      Page(1, 1, true, anime, 0, 0)
    )
    presenter.bind(anilistView)
    presenter.bindIntents()
    val expectedInitial = AnilistViewState(pageState = PageState(null, 0, true))
    val expectedLoading = AnilistViewState(
      loading = PAGE,
      pageState = PageState(null, 0, true),
    )
    val expectedResult = AnilistViewState(pageState = PageState(anime, 1, true))
    loadAnimePageMock.onNext(Unit)
    verifyOrder {
      anilistView.render(expectedInitial)
      anilistView.render(expectedLoading)
      anilistView.render(expectedResult)
    }
  }

  private fun setupSchedulers() {
    testScheduler = Schedulers.trampoline()
    every { rxSchedulers.io } answers { testScheduler }
    every { rxSchedulers.ui } answers { testScheduler }
  }

  private fun setupView() {
    every { anilistView.render(any()) } just runs
  }
}