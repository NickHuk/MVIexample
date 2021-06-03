package com.huchihaitachi.remoteapi.dataSource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.rx2.Rx2Apollo
import com.huchihaitachi.datasource.AnimeRemoteDataSource
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.Page
import com.huchihaitachi.domain.TimeProvider
import com.huchihaitachi.remoteapi.MediaQuery
import com.huchihaitachi.remoteapi.PageQuery
import com.huchihaitachi.remoteapi.type.MediaType.ANIME
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRemoteDataSourceImpl @Inject constructor(
  private val apolloClient: ApolloClient,
  private val timeProvider: TimeProvider
) : AnimeRemoteDataSource {

  override fun loadAnimePage(page: Int, perPage: Int): Single<Page> =
    Rx2Apollo.from(
      apolloClient.query(
        PageQuery(Input.fromNullable(page), Input.fromNullable(perPage))
      )
    )
      .singleOrError()
      .map { response ->
        Page(
          response.data?.page?.pageInfo?.currentPage ?: page,
          response.data?.page?.pageInfo?.perPage,
          response.data?.page?.pageInfo?.hasNextPage,
          response.data?.page?.media?.map { anime ->
            Anime(
              id = anime?.id ?: 0,
              title = anime?.title?.english ?: anime?.title?.romaji,
              coverImage = anime?.coverImage?.extraLarge,
              timeOfBirth = timeProvider.currentTimeInMillis(),
              timeToStale = TIME_TO_STALE
            )
          },
          timeProvider.currentTimeInMillis(),
          TIME_TO_STALE
        )
      }

  override fun loadAnime(id: Int): Single<Anime> =
    Rx2Apollo.from(
      apolloClient.query(
        MediaQuery(Input.fromNullable(id))
      )
    )
      .singleOrError()
      .map { response ->
        Anime(
          response.data?.media?.id ?: id,
          response.data?.media?.title?.english ?: response.data?.media?.title?.romaji,
          response.data?.media?.type?.toDomain(),
          response.data?.media?.description,
          response.data?.media?.season?.toDomain(),
          response.data?.media?.seasonYear,
          when(response.data?.media?.type) {
            ANIME -> response.data?.media?.episodes
            else -> response.data?.media?.chapters
          },
          response.data?.media?.duration,
          response.data?.media?.coverImage?.extraLarge,
          response.data?.media?.bannerImage,
          timeProvider.currentTimeInMillis(),
          TIME_TO_STALE
        )
      }

  companion object {
    const val TIME_TO_STALE = 86400000L
  }
}