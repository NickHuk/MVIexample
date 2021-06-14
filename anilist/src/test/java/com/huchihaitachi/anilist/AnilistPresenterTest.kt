package com.huchihaitachi.anilist

import com.huchihaitachi.anilist.presentation.AnilistPresenter
import com.huchihaitachi.anilist.presentation.AnilistView
import com.huchihaitachi.anilist.presentation.AnilistViewState
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.PAGE
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.REFRESH
import com.huchihaitachi.anilist.presentation.AnilistViewState.PageState
import com.huchihaitachi.base.RxSchedulers
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.Page
import com.huchihaitachi.domain.Season.FALL
import com.huchihaitachi.domain.Type
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
  @MockK private lateinit var view: AnilistView
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
    val loadPageIntentMock: PublishSubject<Unit> = PublishSubject.create()
    mockIntents(loadPageIntent = loadPageIntentMock)
    every { loadPage(any(), any()) } returns Single.just(
      Page(1, anime.size, true, anime, 0, 0)
    )
    presenter.bind(view)
    presenter.bindIntents()
    val expectedInitial = AnilistViewState(pageState = PageState(null, 0, true))
    val expectedLoading = AnilistViewState(
      loading = PAGE,
      pageState = PageState(null, 0, true),
    )
    val expectedResult = AnilistViewState(pageState = PageState(anime, 1, true))
    loadPageIntentMock.onNext(Unit)
    verifyOrder {
      view.render(expectedInitial)
      view.render(expectedLoading)
      view.render(expectedResult)
    }
  }

  @Test
  fun `refresh test`() {
    val initialState = AnilistViewState(pageState = PageState(anime, 1, true))
    val presenter = AnilistPresenter(
      loadPage, refreshPage, loadAnime, getStringResource, initialState, rxSchedulers
    )
    val refreshIntentMock: PublishSubject<Unit> = PublishSubject.create()
    mockIntents(refreshIntent = refreshIntentMock)
    val freshAnime = listOf(
      Anime(11), Anime(21), Anime(31), Anime(41), Anime(51), Anime(61)
    )
    every { refreshPage(any()) } returns Single.just(
      Page(1, freshAnime.size, true, freshAnime, 0, 0)
    )
    presenter.bind(view)
    presenter.bindIntents()
    val expectedInitial = AnilistViewState(pageState = PageState(anime, 1, true))
    val expectedLoading = AnilistViewState(
      loading = REFRESH,
      pageState = PageState(anime, 1, true),
    )
    val expectedResult = AnilistViewState(pageState = PageState(freshAnime, 1, true))
    refreshIntentMock.onNext(Unit)
    verifyOrder {
      view.render(expectedInitial)
      view.render(expectedLoading)
      view.render(expectedResult)
    }
  }

  @Test
  fun `show details while refreshing`() {
    val initialState = AnilistViewState(
      loading = REFRESH,
      pageState = PageState(anime, 1, true)
    )
    val presenter = AnilistPresenter(
      loadPage, refreshPage, loadAnime, getStringResource, initialState, rxSchedulers
    )
    val showDetailsIntentMock: PublishSubject<Int> = PublishSubject.create()
    mockIntents(showDetailsIntent = showDetailsIntentMock)
    val animeDetails = Anime(
      1,
      "title",
      Type.ANIME,
      "test description",
      FALL,
      1998,
      22,
      17,
      "image/test/url",
      "banner/test/url",
      0,
      0
    )
    val expectedDetails = animeDetails.copy()
    every { loadAnime(any()) } returns Single.just(animeDetails)
    presenter.bind(view)
    presenter.bindIntents()
    showDetailsIntentMock.onNext(1)
    val expectedInitial = AnilistViewState(
      loading = REFRESH,
      pageState = PageState(anime, 1, true)
    )
    val expectedResult = AnilistViewState(
      REFRESH,
      expectedDetails,
      PageState(anime, 1, true)
    )
    verifyOrder {
      view.render(expectedInitial)
      view.render(expectedResult)
    }
  }

  //TODO: test backoff

  private fun mockIntents(
    loadPageIntent: PublishSubject<Unit> = PublishSubject.create(),
    refreshIntent: PublishSubject<Unit> = PublishSubject.create(),
    showDetailsIntent: PublishSubject<Int> = PublishSubject.create(),
    hideDetailsIntent: PublishSubject<Unit> = PublishSubject.create()
  ) {
    every { view.loadPage } returns loadPageIntent
    every { view.refresh } returns refreshIntent
    every { view.showDetails } returns showDetailsIntent
    every { view.hideDetails } returns hideDetailsIntent
  }

  private fun setupSchedulers() {
    testScheduler = Schedulers.trampoline()
    every { rxSchedulers.io } answers { testScheduler }
    every { rxSchedulers.ui } answers { testScheduler }
  }

  private fun setupView() {
    every { view.render(any()) } just runs
  }
}