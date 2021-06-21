package com.huchihaitachi.anilist

import com.apollographql.apollo.exception.ApolloNetworkException
import com.huchihaitachi.anilist.presentation.AnilistPresenter
import com.huchihaitachi.anilist.presentation.AnilistView
import com.huchihaitachi.anilist.presentation.AnilistViewState
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.VIEW_CONTENT
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
import io.mockk.verify
import io.mockk.verifyOrder
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit.MILLISECONDS

class AnilistPresenterTest {
  @MockK private lateinit var loadPage: LoadPageUseCase
  @MockK private lateinit var refreshPage: RefreshPageUseCase
  @MockK private lateinit var loadAnime: LoadAnimeUseCase
  @MockK private lateinit var getStringResource: GetStringResourceUseCase
  @MockK private lateinit var view: AnilistView
  @MockK private lateinit var rxSchedulers: RxSchedulers
  private lateinit var anime: List<Anime>
  private lateinit var trampoline: Scheduler
  private lateinit var testScheduler: TestScheduler

  @Before
  fun setup() {
    MockKAnnotations.init(this)
    setupView()
    anime = listOf(Anime(1), Anime(2), Anime(3), Anime(4), Anime(5), Anime(6))
  }

  @Test
  fun `load first page test`() {
    setupTrampoline()
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
    setupTrampoline()
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
  fun `show details while refreshing test`() {
    setupTrampoline()
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

  @Test
  fun `show details while loading page after error test`() {
    setupTrampoline()
    val initialState = AnilistViewState(
      loading = VIEW_CONTENT,
      pageState = PageState(anime, 1, true)
    )
    every { getStringResource(R.string.no_connection) } returns "No connection"
    val presenter = AnilistPresenter(
      loadPage, refreshPage, loadAnime, getStringResource, initialState, rxSchedulers
    )
    val loadPageIntentMock: PublishSubject<Unit> = PublishSubject.create()
    val showDetailsIntentMock: PublishSubject<Int> = PublishSubject.create()
    val hideDetailsIntent: PublishSubject<Unit> = PublishSubject.create()
    mockIntents(
      loadPageIntent = loadPageIntentMock,
      showDetailsIntent = showDetailsIntentMock,
      hideDetailsIntent = hideDetailsIntent
    )
    val details = Anime(
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
    val expectedDetails = details.copy()
    every { loadPage(any(), any()) } returns Single.error(ApolloNetworkException("no connection"))
    every { loadAnime(any()) } returns Single.just(details)
    presenter.bind(view)
    presenter.bindIntents()
    loadPageIntentMock.onNext(Unit)
    showDetailsIntentMock.onNext(1)
    hideDetailsIntent.onNext(Unit)
    loadPageIntentMock.onNext(Unit)

    val expectedInitial = AnilistViewState(
      loading = VIEW_CONTENT,
      pageState = PageState(anime, 1, true)
    )
    //loading
    val expectedSecondState = AnilistViewState(
      loading = PAGE,
      pageState = PageState(anime, 1, true)
    )
    //error
    val expectedThirdState = AnilistViewState(
      pageState = PageState(anime, 1, true),
      error = "No connection",
      loadingEnabled = false,
      backoff = 1
    )
    //view details while error
    val expectedFourthState = AnilistViewState(
      pageState = PageState(anime, 1, true),
      details = expectedDetails,
      error = "No connection",
      loadingEnabled = false,
      backoff = 1
    )
    //hide details while error
    val expectedFifthState = AnilistViewState(
      pageState = PageState(anime, 1, true),
      error = "No connection",
      loadingEnabled = false,
      backoff = 1
    )
    verifyOrder {
      view.render(expectedInitial)
      view.render(expectedSecondState)
      view.render(expectedThirdState)
      view.render(expectedFourthState)
      view.render(expectedFifthState)
    }
    verify(exactly = 5) { view.render(any()) }
  }

  @Test
  fun `load after backoff`() {
    setupTestScheduler()
    val initialState = AnilistViewState(
      loading = VIEW_CONTENT,
      pageState = PageState(anime, 1, true)
    )
    every { getStringResource(R.string.no_connection) } returns "No connection"
    val presenter = AnilistPresenter(
      loadPage, refreshPage, loadAnime, getStringResource, initialState, rxSchedulers
    )
    val loadPageIntentMock: PublishSubject<Unit> = PublishSubject.create()
    mockIntents(loadPageIntent = loadPageIntentMock)
    every { loadPage(any(), any()) } returns Single.error(ApolloNetworkException("no connection"))
    presenter.bind(view)
    presenter.bindIntents()
    loadPageIntentMock.onNext(Unit)
    testScheduler.advanceTimeBy(3000L, MILLISECONDS)
    val moreAnime = listOf(
      Anime(11), Anime(21), Anime(31), Anime(41), Anime(51), Anime(61)
    )
    every { loadPage(any(), any()) } returns Single.just(
      Page(2, moreAnime.size, true, moreAnime, 0,0)
    )
    loadPageIntentMock.onNext(Unit)
    testScheduler.triggerActions()
    val expectedFourthState = AnilistViewState(
      pageState = PageState(anime, 1, true),
      backoff = 1
    )
    val expectedFifthState = AnilistViewState(
      PAGE,
      pageState = PageState(anime, 1, true),
      backoff = 1
    )
    val expectedAnime = listOf(
      Anime(1), Anime(2), Anime(3), Anime(4), Anime(5), Anime(6), Anime(11),
      Anime(21), Anime(31), Anime(41), Anime(51), Anime(61)
    )
    val expectedSixthState = AnilistViewState(
      pageState = PageState(expectedAnime, 2, true),
    )
    val expectedInitial = AnilistViewState(
      loading = VIEW_CONTENT,
      pageState = PageState(anime, 1, true)
    )
    val expectedSecondState = AnilistViewState(
      loading = PAGE,
      pageState = PageState(anime, 1, true)
    )
    val expectedThirdState = AnilistViewState(
      pageState = PageState(anime, 1, true),
      error = "No connection",
      loadingEnabled = false,
      backoff = 1
    )
    verifyOrder {
      view.render(expectedInitial)
      view.render(expectedSecondState)
      view.render(expectedThirdState)
      view.render(expectedFourthState)
      view.render(expectedFifthState)
      view.render(expectedSixthState)
    }

  }

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

  private fun setupTrampoline() {
    trampoline = Schedulers.trampoline()
    every { rxSchedulers.io } answers { trampoline }
    every { rxSchedulers.ui } answers { trampoline }
  }

  private fun setupTestScheduler() {
    testScheduler = TestScheduler()
    every { rxSchedulers.io } answers { testScheduler }
    every { rxSchedulers.computation } answers { testScheduler }
    every { rxSchedulers.ui } answers { testScheduler }
  }

  private fun setupView() {
    every { view.render(any()) } just runs
  }
}