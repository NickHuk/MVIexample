package com.huchihaitachi.remoteapi.dataSource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.rx2.Rx2Apollo
import com.huchihaitachi.datasource.AnimeDataSource
import com.huchihaitachi.domain.Anime
import com.huchihaitachi.domain.Page
import com.huchihaitachi.remoteapi.PageQuery
import com.huchihaitachi.remoteapi.service.AnimeAuthService
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeDataSourceImpl @Inject constructor(
  private val apolloClient: ApolloClient,
  private val animeAuthService: AnimeAuthService
) : AnimeDataSource {

  override fun loadAnimePage(
    page: Int,
    perPage: Int
  ): Single<Page> =
    Rx2Apollo.from(
      apolloClient.query(
        PageQuery(Input.fromNullable(page), Input.fromNullable(perPage))
      )
    )
      .singleOrError()
      .map { response ->
        Page(
          response.data?.page?.pageInfo?.total,
          response.data?.page?.pageInfo?.perPage,
          response.data?.page?.pageInfo?.currentPage,
          response.data?.page?.pageInfo?.lastPage,
          response.data?.page?.pageInfo?.hasNextPage,
          response.data?.page?.media?.map { anime ->
            Anime(
              anime?.id ?: 0,
              anime?.title?.english,
              anime?.type?.toDomain(),
              anime?.description,
              anime?.coverImage?.large,
              anime?.episodes,
              anime?.duration
            )
          }
        )
      }
}