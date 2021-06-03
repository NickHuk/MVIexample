package com.huchihaItachi.database.di

import android.content.Context
import androidx.room.Room
import com.huchihaItachi.database.AnimeDatabase
import com.huchihaItachi.database.BuildConfig
import com.huchihaItachi.database.dao.AnimeDao
import com.huchihaItachi.database.dao.PageDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface DatabaseModule {

  companion object {

    @Singleton
    @Provides
    fun provideDatabase(context: Context) : AnimeDatabase =
      Room.databaseBuilder(context, AnimeDatabase::class.java, BuildConfig.DB_NAME)
        .build()

    @Singleton
    @Provides
    fun providePageDao(database: AnimeDatabase): PageDao = database.pageDao()

    @Singleton
    @Provides
    fun provideAnimeDao(database: AnimeDatabase): AnimeDao = database.animeDao()
  }
}