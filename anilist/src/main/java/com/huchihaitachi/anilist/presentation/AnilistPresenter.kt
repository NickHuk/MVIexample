package com.huchihaitachi.anilist.presentation

import com.apollographql.apollo.exception.ApolloNetworkException
import com.huchihaitachi.anilist.R
import com.huchihaitachi.anilist.di.scope.AnilistScope
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.NOT_LOADING
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.PAGE
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.RELOAD
import com.huchihaitachi.anilist.presentation.AnilistViewState.PageState
import com.huchihaitachi.base.BasePresenter
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.usecase.GetStringResourceUseCase
import com.huchihaitachi.usecase.LoadAnimeUseCase
import com.huchihaitachi.usecase.LoadPageUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AnilistScope
class AnilistPresenter @Inject constructor(
  private val loadPageUseCase: LoadPageUseCase,
  private val loadAnimeUseCase: LoadAnimeUseCase,
  private val getStringResourceUseCase: GetStringResourceUseCase
) : BasePresenter<AnilistView, AnilistViewState>(
  AnilistViewState(NOT_LOADING, null, null, null)
) {

  init {
    disposables.add(loadPage(0)
      .map { partialState ->
        partialState.createState()
      }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { s -> state = s }
    )
  }

  override fun bindIntents() {
    view?.let { view ->
      // load page
      val loadPageIntent = view.loadAnimePage
        .observeOn(Schedulers.io())
        .filter { _ -> state.pageState?.hasNextPage == true && state.loading != RELOAD && state.loading != PAGE }
        .flatMap { _ -> loadPage(state.pageState?.currentPage!! + 1) }
      //reload
      val reloadIntent = view.reload
        .observeOn(Schedulers.io())
        .filter { _ -> state.loading != RELOAD }
        .flatMap { unit -> loadPage(0) }
      //details
      val detailsIntent = view.showDetails
        .observeOn(Schedulers.io())
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
          AnilistPartialState()
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
      .startWith(
        AnilistPartialState(
          loading = if(pageNum == 0) RELOAD else PAGE
        )
      )
      .onErrorReturn { throwable ->
        AnilistPartialState(
          error = when(throwable) {
            is ApolloNetworkException -> getStringResourceUseCase(R.string.no_connection)
            else -> throwable.message
          }
        )
      }

  private fun animeStateReducer(previousState: AnilistViewState, changes: AnilistPartialState) =
    previousState.copy(
      changes.loading,
      changes.details,
      when (previousState.loading) {
        PAGE -> changes.pageState?.copy(
          mutableListOf<Anime>().apply {
            previousState.pageState?.anime?.let(::addAll)
            changes.pageState.anime?.let(::addAll)
          }
        )
        RELOAD ->
          changes.pageState?.copy()
        NOT_LOADING ->
          previousState.pageState?.copy()
      },
      changes.error
    )

  companion object {
    const val PER_PAGE = 8
  }
}