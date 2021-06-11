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
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

class AnilistPresenterTest {
  @MockK private lateinit var loadPageUseCase: LoadPageUseCase
  @MockK private lateinit var loadAnimeUseCase: LoadAnimeUseCase
  @MockK private lateinit var getStringResourceUseCase: GetStringResourceUseCase
  @MockK private lateinit var anilistView: AnilistView
  @MockK private lateinit var rxSchedulers: RxSchedulers

  @Before
  fun setup() {
    MockKAnnotations.init(this)
    mockIntents()
  }

  @Test
  fun `load first page test`() {
    val initialState = AnilistViewState()
    val anilistPresenter = AnilistPresenter(
      loadPageUseCase,
      loadAnimeUseCase,
      getStringResourceUseCase,
      AnilistViewState(),
      rxSchedulers
    )
    val anime = listOf(
      Anime(1), Anime(2), Anime(3), Anime(4), Anime(1), Anime(2), Anime(3), Anime(4)
    )
    val testScheduler = TestScheduler()
    every { rxSchedulers.io } returns testScheduler
    every { rxSchedulers.ui } returns testScheduler
    anilistPresenter.bind(anilistView)
    val loadAnimePageMock: PublishSubject<Unit> = PublishSubject.create()
    every { anilistView.loadAnimePage } returns loadAnimePageMock
    every { loadPageUseCase(any(), any()) } returns Single.just(Page(8, 1, true, anime))
    every { anilistView.render(any()) } returns testScheduler.advanceTimeBy(100L, MILLISECONDS)
    val expectedResult = AnilistViewState(PAGE, null, PageState(anime, 1, true), null)
    anilistPresenter.bindIntents()
    testScheduler.advanceTimeBy(10000L, TimeUnit.MILLISECONDS)
    loadAnimePageMock.onNext(Unit)
    verifySequence {
      anilistView.render(initialState)
      anilistView.render(expectedResult)
    }
  }

  private fun mockIntents() {
    every { anilistView.loadAnimePage } returns PublishSubject.create()
    every { anilistView.reload } returns PublishSubject.create()
    every { anilistView.showDetails } returns PublishSubject.create()
    every { anilistView.hideDetails } returns PublishSubject.create()
  }
}