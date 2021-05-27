package com.huchihaitachi.anilist.presentation

import com.apollographql.apollo.exception.ApolloNetworkException
import com.huchihaitachi.anilist.R
import com.huchihaitachi.anilist.di.scope.AnilistScope
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.PAGE
import com.huchihaitachi.anilist.presentation.AnilistViewState.LoadingType.RELOAD
import com.huchihaitachi.base.BasePresenter
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.usecase.GetStringResourceUseCase
import com.huchihaitachi.usecase.LoadPageUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AnilistScope
class AnilistPresenter @Inject constructor(
  private val loadPageUseCase: LoadPageUseCase,
  private val getStringResourceUseCase: GetStringResourceUseCase
) : BasePresenter<AnilistView, AnilistViewState>(
  AnilistViewState(false, PAGE, null, null, 0, null, true)
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
        .filter { _ ->
          state.hasNextPage == true
            && state.currentPage != null
            && !state.isLoading
        }
        .flatMap { _ -> loadPage(state.currentPage!! + 1) }
      //reload
      val reloadIntent = view.reload
        .observeOn(Schedulers.io())
        .filter { _ ->
          !state.isLoading
            || state.loadingType != RELOAD
        }
        .flatMap { unit -> loadPage(0) }
      val intents = Observable.merge(loadPageIntent, reloadIntent)
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
          loadingType = if (pageNum == 0) RELOAD else PAGE,
          anime = page.anime,
          total = page.total,
          currentPage = page.currentPage,
          lastPage = page.lastPage,
          hasNextPage = page.hasNextPage
        )
      }
      .startWith(
        AnilistPartialState(
          isLoading = true,
          loadingType = if (pageNum == 0) RELOAD else PAGE
        )
      )
      .onErrorReturn { throwable ->
        AnilistPartialState(
          loadingType = if (pageNum == 0) RELOAD else PAGE,
          error = when(throwable) {
            is ApolloNetworkException -> getStringResourceUseCase(R.string.no_connection)
            else -> throwable.message
          }
        )
      }

  private fun animeStateReducer(previousState: AnilistViewState, changes: AnilistPartialState) =
    previousState.copy(
      changes.isLoading,
      changes.loadingType,
      if (changes.loadingType == PAGE)
        mutableListOf<Anime>().apply {
          previousState.anime?.let(::addAll)
          changes.anime?.let(::addAll)
        }
      else
        changes.anime,
      changes.total,
      changes.currentPage,
      changes.lastPage,
      changes.hasNextPage,
      changes.error
    )

  companion object {
    const val PER_PAGE = 8
  }
}