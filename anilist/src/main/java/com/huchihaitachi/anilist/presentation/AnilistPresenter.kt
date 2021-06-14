package com.huchihaitachi.anilist.presentation

import com.apollographql.apollo.exception.ApolloNetworkException
import com.huchihaitachi.anilist.R
import com.huchihaitachi.anilist.di.scope.AnilistScope
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.NOT_LOADING
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.PAGE
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.REFRESH
import com.huchihaitachi.anilist.presentation.AnilistViewState.PageState
import com.huchihaitachi.base.BasePresenter
import com.huchihaitachi.base.RxSchedulers
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.usecase.GetStringResourceUseCase
import com.huchihaitachi.usecase.LoadAnimeUseCase
import com.huchihaitachi.usecase.LoadPageUseCase
import com.huchihaitachi.usecase.RefreshPageUseCase
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.pow

@AnilistScope
class AnilistPresenter @Inject constructor(
  private val loadPageUseCase: LoadPageUseCase,
  private val refreshPageUseCase: RefreshPageUseCase,
  private val loadAnimeUseCase: LoadAnimeUseCase,
  private val getStringResourceUseCase: GetStringResourceUseCase,
  initialViewState: AnilistViewState,
  rxSchedulers: RxSchedulers
) : BasePresenter<AnilistView, AnilistViewState>(initialViewState, rxSchedulers) {

  override fun bindIntents() {
    view?.let { view ->
      // load page
      val loadPageIntent = view.loadPage
        .observeOn(rxSchedulers.io)
        .filter { _ ->
          state.pageState?.hasNextPage == true
            && state.loading != REFRESH
            && state.loading != PAGE
            && state.loadingEnabled
        }
        .flatMap { _ -> loadPage(state.pageState?.currentPage!! + 1) }
      //refresh
      val refreshIntent = view.refresh
        .observeOn(rxSchedulers.io)
        .filter { _ -> state.loading != REFRESH }
        .flatMap { unit -> refreshPage() }
      //details
      val detailsIntent = view.showDetails
        .observeOn(rxSchedulers.io)
        .switchMap { id ->
          loadAnimeUseCase(id)
            .toObservable()
            .map { details -> state.copy(details = details) }
            .onErrorReturn { throwable ->
              state.copy(
                error = when (throwable) {
                  is ApolloNetworkException -> getStringResourceUseCase(R.string.no_connection)
                  else -> throwable.message
                }
              )
            }
        }
      //hide details
      val hideDetailsIntent = view.hideDetails
        .filter { state.loading == NOT_LOADING }
        .map { state.copy(details = null) }
      Observable.merge(loadPageIntent, refreshIntent, detailsIntent, hideDetailsIntent)
        .subscribe { s -> state = s }
        .let(disposables::add)
    }
  }

  private fun loadPage(pageNum: Int): Observable<AnilistViewState> =
    loadPageUseCase(pageNum, PER_PAGE)
      .toObservable()
      .map { page ->
        state.copy(
          loading = NOT_LOADING,
          pageState = PageState(
            mutableListOf<Anime>().apply {
              state.pageState?.anime?.let(::addAll)
              page.anime?.let(::addAll)
            },
            page.currentPage,
            page.hasNextPage
          ),
          loadingEnabled = page.hasNextPage ?: true,
          backoff = 0
        )
      }
      .startWith(state.copy(loading = PAGE))
      .onErrorResumeNext(::loadPageErrorHandler)

  private fun loadPageErrorHandler(throwable: Throwable): ObservableSource<AnilistViewState> =
    when (throwable) {
      is ApolloNetworkException ->
        Observable.timer(
          if (state.backoff < 4) {
            (2.0.pow(state.backoff.toDouble()) * 1000L).toLong()
          } else {
            MAX_BACKOFF
          },
          TimeUnit.MILLISECONDS
        )
          .map { state.copy(
            error = null,
            loadingEnabled = true
          ) }
          .startWith(
            state.copy(
              loading = NOT_LOADING,
              error = getStringResourceUseCase(R.string.no_connection),
              loadingEnabled = false,
              backoff = state.backoff + 1
            )
          )
      else -> Observable.just(state.copy(
        loading = NOT_LOADING,
        error = throwable.message)
      )
    }

  private fun refreshPage(): Observable<AnilistViewState> =
    refreshPageUseCase(PER_PAGE)
      .toObservable()
      .map { page ->
        state.copy(
          loading = NOT_LOADING,
          pageState = PageState(page.anime, page.currentPage, page.hasNextPage),
          loadingEnabled = page.hasNextPage ?: true,
          backoff = 0
        )
      }
      .startWith(state.copy(loading = REFRESH))
      .onErrorReturn { throwable ->
        state.copy(
          loading = NOT_LOADING,
          error = when(throwable) {
            is ApolloNetworkException -> getStringResourceUseCase(R.string.no_connection)
            else -> throwable.message
          }
        )
      }

  companion object {
    const val PER_PAGE = 8
    const val MAX_BACKOFF = 16000L
  }
}