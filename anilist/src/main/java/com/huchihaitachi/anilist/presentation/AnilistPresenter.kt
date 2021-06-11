package com.huchihaitachi.anilist.presentation

import com.apollographql.apollo.exception.ApolloNetworkException
import com.huchihaitachi.anilist.R
import com.huchihaitachi.anilist.di.scope.AnilistScope
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.NOT_LOADING
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.PAGE
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.RELOAD
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
            && state.loading != RELOAD
            && state.loading != PAGE
            && state.loadingEnabled
        }
        .flatMap { _ -> loadPage(state.pageState?.currentPage!! + 1) }
      //reload
      val reloadIntent = view.refresh
        .observeOn(rxSchedulers.io)
        .filter { _ -> state.loading != RELOAD }
        .flatMap { unit -> refreshPage() }
      //details
      val detailsIntent = view.showDetails
        .observeOn(rxSchedulers.io)
        .switchMap { id ->
          loadAnimeUseCase(id)
            .toObservable()
            .map { details ->
              AnilistPartialState(
                details = details
              )
            }
            .onErrorReturn { throwable ->
              AnilistPartialState(
                error = when (throwable) {
                  is ApolloNetworkException -> getStringResourceUseCase(R.string.no_connection)
                  else -> throwable.message
                }
              )
            }
        }
      val hideDetailsIntent = view.hideDetails
        .filter { state.loading == NOT_LOADING }
        .map {
          AnilistPartialState(error = state.error)
        }
      val intents = Observable.merge(loadPageIntent, reloadIntent, detailsIntent, hideDetailsIntent)
      intents.scan(state, ::animeStateReducer)
        .subscribe { s ->
          state = s
        }
        .let(disposables::add)
    }
  }

  private fun loadPage(pageNum: Int): Observable<AnilistPartialState> =
    loadPageUseCase(pageNum, PER_PAGE)
      .toObservable()
      .map { page ->
        AnilistPartialState(
          pageState = PageState(page.anime, page.currentPage, page.hasNextPage)
        )
      }
      .startWith(AnilistPartialState(loading = PAGE))
      .onErrorResumeNext(::loadPageErrorHandler)

  private fun loadPageErrorHandler(throwable: Throwable): ObservableSource<AnilistPartialState> =
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
          .map { AnilistPartialState() }
          .startWith(
            AnilistPartialState(
              error = getStringResourceUseCase(R.string.no_connection),
              loadingEnabled = false,
              backoff = state.backoff + 1
            )
          )
      else -> Observable.just(AnilistPartialState(error = throwable.message))
    }

  private fun refreshPage(): Observable<AnilistPartialState> =
    refreshPageUseCase(PER_PAGE)
      .toObservable()
      .map { page ->
        AnilistPartialState(
          pageState = PageState(page.anime, page.currentPage, page.hasNextPage)
        )
      }
      .startWith(AnilistPartialState(loading = RELOAD))
      .onErrorReturn { throwable ->
        AnilistPartialState(
          error = when(throwable) {
            is ApolloNetworkException -> getStringResourceUseCase(R.string.no_connection)
            else -> throwable.message
          }
        )
      }

  private fun animeStateReducer(previousState: AnilistViewState, changes: AnilistPartialState) =
    if (changes.error == null) {
      when (previousState.loading) {
        PAGE -> previousState.copy(
          changes.loading,
          changes.details,
          changes.pageState?.copy(
            mutableListOf<Anime>().apply {
              previousState.pageState?.anime?.let(::addAll)
              changes.pageState.anime?.let(::addAll)
            }
          ),
          changes.error,
          changes.loadingEnabled,
          changes.backoff
        )
        RELOAD ->
          previousState.copy(
            changes.loading,
            changes.details,
            changes.pageState?.copy(),
            changes.error,
            changes.loadingEnabled,
            changes.backoff
          )
        NOT_LOADING -> previousState.copy(
          changes.loading,
          changes.details,
          previousState.pageState?.copy(),
          changes.error,
          changes.loadingEnabled,
          previousState.backoff
        )
      }
    } else {
      previousState.copy(
        changes.loading,
        changes.details,
        previousState.pageState?.copy(),
        changes.error,
        changes.loadingEnabled,
        changes.backoff
      )
    }

  companion object {
    const val PER_PAGE = 8
    const val MAX_BACKOFF = 16000L
  }
}