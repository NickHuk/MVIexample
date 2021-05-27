package com.huchihaitachi.remoteapi.di

import com.apollographql.apollo.ApolloClient
import com.huchihaitachi.remoteapi.API_CLIENT
import com.huchihaitachi.remoteapi.AUTH_CLIENT
import com.huchihaitachi.remoteapi.BuildConfig
import com.huchihaitachi.remoteapi.HEADER_INTERCEPTOR
import com.huchihaitachi.remoteapi.LOGGIN_INTERCEPTOR
import com.huchihaitachi.remoteapi.service.AnimeAuthService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Logger
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton

@Module
interface RemoteAPIModule {

  companion object {
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideLogger(): Logger = Logger { message ->
      Timber.tag(OKHTTP).d(message)
    }

    @Singleton
    @Provides
    @Named(LOGGIN_INTERCEPTOR)
    fun provideLoggingInterceptor(logger: Logger): Interceptor =
      HttpLoggingInterceptor(logger).apply {
        level = HttpLoggingInterceptor.Level.BODY
      }

    @Singleton
    @Provides
    @Named(HEADER_INTERCEPTOR)
    fun provideHeaderInterceptor(): Interceptor = Interceptor { chain ->
      chain.request().newBuilder()
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json")
        .build()
        .let(chain::proceed)
    }

    @Singleton
    @Provides
    fun provideRxCallAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

    @Singleton
    @Provides
    @Named(AUTH_CLIENT)
    fun provideAuthOkhttpClient(@Named(LOGGIN_INTERCEPTOR) interceptor: Interceptor): OkHttpClient =
      OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    @Named(API_CLIENT)
    fun provideApiOkhttpClient(@Named(HEADER_INTERCEPTOR) interceptor: Interceptor): OkHttpClient =
      OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
      @Named(AUTH_CLIENT) client: OkHttpClient,
      converterFactory: Converter.Factory,
      rx2CallAdapterFactory: CallAdapter.Factory
    ): Retrofit = Retrofit.Builder()
      .baseUrl(BuildConfig.ANIMELIST_AUTH_URL)
      .client(client)
      .addConverterFactory(converterFactory)
      .addCallAdapterFactory(rx2CallAdapterFactory)
      .build()

    @Singleton
    @Provides
    fun provideApolloClient(
      @Named(API_CLIENT) client: OkHttpClient
    ): ApolloClient = ApolloClient.builder()
      .serverUrl(BuildConfig.ANIMELIST_GRAPH_URL)
      .okHttpClient(client)
      .build()

    @Singleton
    @Provides
    fun provideAuthService(retrofit: Retrofit): AnimeAuthService =
      retrofit.create(AnimeAuthService::class.java)

    const val OKHTTP = "OkHttp"
  }
}