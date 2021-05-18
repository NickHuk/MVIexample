package com.huchihaitachi.remoteapi.di

import com.apollographql.apollo.ApolloClient
import com.huchihaitachi.remoteapi.BuildConfig
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
interface RemoteAPIModule {

    companion object {
        @Singleton
        @Provides
        fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.ANIMELIST_AUTH_URL)
            .build()

        @Singleton
        @Provides
        fun provideApolloClient(): ApolloClient = ApolloClient.builder()
            .serverUrl(BuildConfig.ANIMELIST_GRAPH_URL)
            .build()

    }
}